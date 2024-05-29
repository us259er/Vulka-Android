package io.github.vulka.impl.vulcan

import com.google.gson.Gson
import io.github.vulka.impl.vulcan.hebe.ApiEndpoints
import io.github.vulka.impl.vulcan.hebe.HebeAccount
import io.github.vulka.impl.vulcan.hebe.HebeHttpClient
import io.github.vulka.impl.vulcan.hebe.login.Keystore
import io.github.vulka.impl.vulcan.hebe.login.PfxRequest
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

class VulcanApi @Throws(Exception::class) constructor() {
    private val logger: Logger = LoggerFactory.getLogger(VulcanApi::class.java)

    private lateinit var client: HebeHttpClient
    lateinit var session: VulcanLoginResponse

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
    fun register(symbol: String, token: String, pin: String) {
        val upperToken = token.uppercase()
        val lowerSymbol = symbol.lowercase()

        val keystore = Keystore.create("", "Vulka SDK 0.0.1")
        client = HebeHttpClient(keystore)

        // https://lekcjaplus.vulcan.net.pl
        val baseUrl = getBaseUrl(token);

        val fullUrl = "$baseUrl/$lowerSymbol/${ApiEndpoints.DEVICE_REGISTER}"

        val pfxRequest = PfxRequest(
            OS = "Android",
            deviceModel = keystore.deviceModel,
            certificate = keystore.certificate,
            certificateType = "X509",
            certificateThumbprint = keystore.fingerprint,
            pin = pin,
            securityToken = upperToken,
            selfIdentifier = Utils.uuid(keystore.fingerprint)
        )

        logger.info("URL: $fullUrl")
        logger.info("Registering to $lowerSymbol")

        client.post(fullUrl, pfxRequest).use { response ->

            logger.debug("Response code ${response.code}")
            val body = response.body?.string()
            logger.debug("Response body $body")

            val accountJson = Gson().fromJson(body, HebeAccount::class.java)
            session = VulcanLoginResponse(accountJson,keystore)
        }
    }
}
