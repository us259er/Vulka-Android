package io.github.vulka.impl.vulcan.hebe

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.vulka.core.api.log.Logger
import io.github.vulka.core.api.log.LoggerFactory
import io.github.vulka.impl.*
import io.github.vulka.impl.vulcan.*
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore
import io.github.vulka.impl.vulcan.hebe.types.ApiRequest
import io.github.vulka.impl.vulcan.hebe.types.ApiResponse
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*
import java.util.regex.Pattern

class HebeHttpClient(private val keystore: HebeKeystore) {
    private val logger: Logger = LoggerFactory.get(HebeHttpClient::class.java)

    companion object {
        const val APP_NAME = "DzienniczekPlus 2.0"
        const val APP_VERSION = "1.4.2"
        const val APP_OS = "Android"
        const val APP_USER_AGENT = "Dart/2.10 (dart:io)"
    }

    private val client = HttpClient(OkHttp)

    private fun getEncodedPath(fullUrl: String): String {
        val pattern = Pattern.compile("api/mobile/.+")
        val matcher = pattern.matcher(fullUrl)
        if (!matcher.find()) {
            throw IllegalArgumentException("The URL does not seem correct (does not match `(api/mobile/.+)` regex)")
        }
        return URLEncoder.encode(matcher.group(), "UTF-8").lowercase()
    }

    @Throws(IOException::class)
    private fun buildHeaders(fullUrl: String, body: String? = null): Map<String, String> {
        val date = Date.from(ZonedDateTime.now().toInstant())
        val time = SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }.format(date) + " GMT"
        val (_,fingerprint,privateKey) = keystore.getData()

        val (digest, canonicalUrl, signature) = getSignatureValues(fingerprint,privateKey, body, fullUrl,date )

        val headers = mutableMapOf(
            "User-Agent" to APP_USER_AGENT,
            "vOS" to APP_OS,
            "vDeviceModel" to keystore.deviceModel,
            "vAPI" to "1",
            "vDate" to time,
            "vCanonicalUrl" to canonicalUrl,
            "Signature" to signature
        )

        body?.let {
            headers["Digest"] = digest
            headers["Content-Type"] = "application/json"
        }
        return headers
    }

    private fun buildPayload(body: Any): ApiRequest {
        val (_,fingerprint,_) = keystore.getData()
        return ApiRequest(
            appName = APP_NAME,
            appVersion = APP_VERSION,
            certificateId = fingerprint,
            envelope = body,
            firebaseToken = keystore.firebaseToken,
            api = 1,
            requestId = UUID.randomUUID(),
            timestamp = System.currentTimeMillis(),
            timestampFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        )
    }

    @Throws(IOException::class)
    fun <T> post(url: String, body: Any, clazz: Class<T>): T? = runBlocking {
        val payload = buildPayload(body)
        val payloadString = Gson().toJson(payload)
        val headers = buildHeaders(url, payloadString)

        val response: HttpResponse = client.post(url) {
            headers {
                headers.forEach { (key, value) -> append(key, value) }
            }
            contentType(ContentType.Application.Json)
            setBody(payloadString)
        }

        logger.debug("HTTP: POST")
        logger.debug("HTTP Request URL: $url")
        logger.debug("HTTP Response code: ${response.status.value}")

        val type = TypeToken.getParameterized(ApiResponse::class.java, clazz).type
        val responseBody = response.bodyAsText()
        val apiResponse = Gson().fromJson<ApiResponse<T>>(responseBody, type)

        logger.debug("API Response code: ${apiResponse.status.code}")

        checkErrors(apiResponse)

        return@runBlocking apiResponse.envelope
    }

    @Throws(IOException::class)
    fun <T> get(url: String, clazz: Class<T>, query: Map<String, String>? = null): T? = runBlocking {
        val urlBuilder = URLBuilder(url)

        query?.forEach { (key, value) ->
            urlBuilder.parameters.append(key, value)
        }

        val buildedUrl = urlBuilder.buildString()
        val headers = buildHeaders(buildedUrl)

        val response: HttpResponse = client.get(buildedUrl) {
            headers {
                headers.forEach { (key, value) -> append(key, value) }
            }
        }

        logger.debug("HTTP: GET")
        logger.debug("HTTP Request URL: $buildedUrl")
        logger.debug("HTTP Response code: ${response.status.value}")

        val responseBody = response.bodyAsText()

        logger.debug("HTTP Response body: $responseBody")

        val type = TypeToken.getParameterized(ApiResponse::class.java, clazz).type
        val apiResponse = Gson().fromJson<ApiResponse<T>>(responseBody, type)

        logger.debug("API Response code: ${apiResponse.status.code}")

        checkErrors(apiResponse)

        return@runBlocking apiResponse.envelope
    }

    @Throws(IOException::class)
    fun getDebug(url: String, query: Map<String, String>? = null): HttpResponse = runBlocking {
        val urlBuilder = URLBuilder(url)

        query?.forEach { (key, value) ->
            urlBuilder.parameters.append(key, value)
        }

        val buildedUrl = urlBuilder.buildString()
        val headers = buildHeaders(buildedUrl)

        val response: HttpResponse = client.get(buildedUrl) {
            headers {
                headers.forEach { (key, value) -> append(key, value) }
            }
        }

        logger.debug("HTTP: GET (DEBUG)")
        logger.debug("HTTP Request URL: $buildedUrl")

        return@runBlocking response
    }

    private fun checkErrors(apiResponse: ApiResponse<*>) {
        if (apiResponse.status.code == 100 && apiResponse.status.message.contains(": "))
            throw InvalidSignatureValuesException()

        when (apiResponse.status.code) {
            200 -> {
                logger.debug("Throw InvalidTokenException")
                throw InvalidTokenException(apiResponse.status.message)
            }
            108 -> {
                logger.debug("Throw UnauthorizedCertificateException")
                throw UnauthorizedCertificateException(apiResponse.status.message)
            }
            203 -> {
                logger.debug("Throw InvalidPINException")
                throw InvalidPINException(apiResponse.status.message)
            }
            204 -> {
                logger.debug("Throw ExpiredTokenException")
                throw ExpiredTokenException(apiResponse.status.message)
            }
            -1 -> {
                logger.debug("Throw InvalidSymbolException")
                throw InvalidSymbolException(apiResponse.status.message)
            }
        }

        if (apiResponse.status.code != 0) {
            logger.debug("Throw VulcanAPIException")
            throw VulcanAPIException("")
        }
    }
}
