package io.github.vulka.impl.vulcan.hebe

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.vulka.core.api.log.LoggerFactory
import io.github.vulka.impl.vulcan.Utils
import io.github.vulka.impl.vulcan.VulcanLoginResponse
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore
import io.github.vulka.impl.vulcan.hebe.login.PfxRequest
import io.github.vulka.impl.vulcan.hebe.types.ApiResponse
import io.github.vulka.impl.vulcan.hebe.types.HebeStudent
import okhttp3.*
import java.io.IOException

class VulcanHebeApi {
    private val log = LoggerFactory.get(VulcanHebeApi::class.java)

    private lateinit var client: HebeHttpClient
    private lateinit var credentials: VulcanLoginResponse

    fun setup(credentials: VulcanLoginResponse) {
        client = HebeHttpClient(credentials.keystore)
        this.credentials = credentials
    }

    @Throws(IOException::class)
    fun getBaseUrl(token: String): String? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://komponenty.vulcan.net.pl/UonetPlusMobile/RoutingRules.txt")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val content = response.body?.string() ?: return null
            val lines = content.lines()

            for (line in lines) {
                if (line.startsWith(token.substring(0,3)))
                    return line.substring(line.indexOf(",") + 1)
            }
        }

        return null
    }

    @Throws(Exception::class)
    fun register(keystore: HebeKeystore, symbol: String, token: String, pin: String): ApiResponse<HebeAccount> {
        val upperToken = token.uppercase()
        val lowerSymbol = symbol.lowercase()

        client = HebeHttpClient(keystore)

        // https://lekcjaplus.vulcan.net.pl
        val baseUrl = getBaseUrl(token);

        val fullUrl = "$baseUrl/$lowerSymbol/${ApiEndpoints.DEVICE_REGISTER}"

        val (certificate,fingerprint,_) = keystore.getData()

        val pfxRequest = PfxRequest(
            os = "Android",
            deviceModel = keystore.deviceModel,
            certificate = certificate,
            certificateType = "X509",
            certificateThumbprint = fingerprint,
            pin = pin,
            securityToken = upperToken,
            selfIdentifier = Utils.uuid(fingerprint)
        )

        log.info("URL: $fullUrl")
        log.info("Registering to $lowerSymbol")


        client.post(fullUrl, pfxRequest).use { response ->
            log.info("Response code ${response.code}")
            val body = response.body?.string()
            log.info("Response body $body")

            val apiResponse = Gson().fromJson<ApiResponse<HebeAccount>>(body, object : TypeToken<ApiResponse<HebeAccount>>() {}.type)
            if (apiResponse.envelope != null)
                credentials = VulcanLoginResponse(apiResponse.envelope!!,keystore)

            return apiResponse
        }
    }

    fun getStudents(): Array<HebeStudent> {
        val baseUrl = credentials.account.restUrl

        val fullUrl = "$baseUrl${ApiEndpoints.STUDENT_LIST}"

        log.info("Students URL: $fullUrl")

        client.get(fullUrl).use {
            val body = it.body?.string()
            log.debug(body!!)
            val apiResponse = Gson().fromJson<ApiResponse<Array<HebeStudent>>>(body, object : TypeToken<ApiResponse<Array<HebeStudent>>>() {}.type)
            log.info("Code: ${it.code}")
            return apiResponse.envelope!!
        }
    }
}
