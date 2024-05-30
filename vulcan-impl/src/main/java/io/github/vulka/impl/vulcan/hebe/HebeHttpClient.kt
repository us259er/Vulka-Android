package io.github.vulka.impl.vulcan.hebe

import com.google.gson.Gson
import io.github.vulka.impl.*
import io.github.vulka.impl.vulcan.*
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore
import io.github.vulka.impl.vulcan.hebe.types.ApiRequest
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class HebeHttpClient(private val keystore: HebeKeystore) {
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
    private fun buildHeaders(fullUrl: String, body: String?): Headers {
        val time = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)
        val (_,fingerprint,privateKey) = keystore.getData()

//        val headersMap = getSignatureHeaders(keystore.fingerprint, keystore.privateKey, body ?: "", fullUrl, ZonedDateTime.now())
        val (digest, canonicalUrl, signature) = getSignatureValues(fingerprint,privateKey, body, fullUrl, Date())
        val headersBuilder = Headers.Builder()
            .add("User-Agent", APP_USER_AGENT)
            .add("vOS", APP_OS)
            .add("vDeviceModel", keystore.deviceModel)
            .add("vAPI", "1")
            .add("vDate", time)
            .add("vCanonicalUrl", canonicalUrl )
            .add("Signature", signature)

        body?.let {
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
    fun post(url: String, body: Any): Response {
        val payload = buildPayload(body)
        val payloadString = Gson().toJson(payload)
        val headers = buildHeaders(url, payloadString)

        val request = Request.Builder()
            .url(url)
            .headers(headers)
            .post(payloadString.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        val response = client.newCall(request).execute()

        when (response.code) {
//            200 -> {
////                logger.debug("Throw InvalidTokenException")
//                throw InvalidTokenException()
//            }
//            108 -> {
////                logger.debug("Throw UnauthorizedCertificateException")
//                throw UnauthorizedCertificateException()
//            }
//            203 -> {
////                logger.debug("Throw InvalidPINException")
//                throw InvalidPINException()
//            }
//            204 -> {
////                logger.debug("Throw ExpiredTokenException")
//                throw ExpiredTokenException()
//            }
//            -1 -> {
////                logger.debug("Throw InvalidSymbolException")
//                throw InvalidSymbolException()
//            }
//            0 -> {
////                logger.debug("Throw VulcanAPIException")
//                throw VulcanAPIException("")
//            }
        }

        return response
    }
}
