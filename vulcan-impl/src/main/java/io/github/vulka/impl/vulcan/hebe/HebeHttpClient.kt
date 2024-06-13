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
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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

    private val client = OkHttpClient()

    private fun getEncodedPath(fullUrl: String): String {
        val pattern = Pattern.compile("api/mobile/.+")
        val matcher = pattern.matcher(fullUrl)
        if (!matcher.find()) {
            throw IllegalArgumentException("The URL does not seem correct (does not match `(api/mobile/.+)` regex)")
        }
        return URLEncoder.encode(matcher.group(), "UTF-8").lowercase()
    }


    @Throws(IOException::class)
    private fun buildHeaders(fullUrl: String, body: String? = null): Headers {
        val date = Date.from(ZonedDateTime.now().toInstant())
        val time = SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }.format(date) + " GMT"
        val (_,fingerprint,privateKey) = keystore.getData()

//        val headersMap = getSignatureHeaders(keystore.fingerprint, keystore.privateKey, body ?: "", fullUrl, ZonedDateTime.now())
        val (digest, canonicalUrl, signature) = getSignatureValues(fingerprint,privateKey, body, fullUrl,date )
        val headersBuilder = Headers.Builder()
            .add("User-Agent", APP_USER_AGENT)
            .add("vOS", APP_OS)
            .add("vDeviceModel", keystore.deviceModel)
            .add("vAPI", "1")
            .add("vDate", time)
            .add("vCanonicalUrl", canonicalUrl )
            .add("Signature", signature)

        if (body != null) {
            headersBuilder
                .add("Digest", digest)
                .add("Content-Type", "application/json")
        }
        return headersBuilder.build()
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
    fun <T> post(url: String, body: Any, clazz: Class<T>): T? {
        val payload = buildPayload(body)
        val payloadString = Gson().toJson(payload)
        val headers = buildHeaders(url, payloadString)

        val request = Request.Builder()
            .url(url)
            .headers(headers)
            .post(payloadString.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        logger.debug("HTTP: POST")
        logger.debug("HTTP Request URL: $url")

        val response = client.newCall(request).execute()

        logger.debug("HTTP Response code: ${response.code}")

        when (response.code) {
            // TODO: Fix problems with login
//            200 -> {
//                logger.debug("Throw InvalidTokenException")
//                throw InvalidTokenException()
//            }
            108 -> {
                logger.debug("Throw UnauthorizedCertificateException")
                throw UnauthorizedCertificateException()
            }
            203 -> {
                logger.debug("Throw InvalidPINException")
                throw InvalidPINException()
            }
            204 -> {
                logger.debug("Throw ExpiredTokenException")
                throw ExpiredTokenException()
            }
            -1 -> {
                logger.debug("Throw InvalidSymbolException")
                throw InvalidSymbolException()
            }
            0 -> {
                logger.debug("Throw VulcanAPIException")
                throw VulcanAPIException("")
            }
        }

        val responseBody = response.body?.string()

        val type = TypeToken.getParameterized(ApiResponse::class.java, clazz).type
        return Gson().fromJson<ApiResponse<T>>(responseBody, type).envelope
    }

    @Throws(IOException::class)
    fun <T> get(url: String, clazz: Class<T>, query: Map<String, String>? = null): T? {


        val urlBuilder = url.toHttpUrlOrNull()?.newBuilder()

        if (query != null) {
            for ((key, value) in query) {
                urlBuilder!!.addQueryParameter(key, value)
            }
        }

        val buildedUrl = urlBuilder!!.build()
        val headers = buildHeaders(buildedUrl.toString())

        val request = Request.Builder()
            .url(buildedUrl)
            .headers(headers)
            .get()
            .build()

        logger.debug("HTTP: GET")
        logger.debug("HTTP Request URL: $buildedUrl")

        val response = client.newCall(request).execute()

        logger.debug("HTTP Response code: ${response.code}")

        val responseBody = response.body?.string()

        logger.debug("HTTP Response body: $responseBody")

        val type = TypeToken.getParameterized(ApiResponse::class.java, clazz).type
        return Gson().fromJson<ApiResponse<T>>(responseBody, type).envelope
    }

    @Throws(IOException::class)
    fun getDebug(url: String,query: Map<String, String>? = null): Response {


        val urlBuilder = url.toHttpUrlOrNull()?.newBuilder()

        if (query != null) {
            for ((key, value) in query) {
                urlBuilder!!.addQueryParameter(key, value)
            }
        }

        val buildedUrl = urlBuilder!!.build()
        val headers = buildHeaders(buildedUrl.toString())

        val request = Request.Builder()
            .url(buildedUrl)
            .headers(headers)
            .get()
            .build()

        logger.debug("HTTP: GET (DEBUG)")
        logger.debug("HTTP Request URL: $buildedUrl")

        return client.newCall(request).execute()
    }

}
