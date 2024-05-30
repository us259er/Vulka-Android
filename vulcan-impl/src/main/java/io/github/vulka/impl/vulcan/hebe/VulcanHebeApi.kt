package io.github.vulka.impl.vulcan.hebe

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.vulka.impl.vulcan.Utils
import io.github.vulka.impl.vulcan.VulcanLoginResponse
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore
import io.github.vulka.impl.vulcan.hebe.login.PfxRequest
import io.github.vulka.impl.vulcan.hebe.types.ApiResponse
import okhttp3.*
import java.io.IOException

class VulcanHebeApi @Throws(Exception::class) constructor() {
    private lateinit var client: HebeHttpClient
    lateinit var session: VulcanLoginResponse

    @Throws(IOException::class)
    fun getBaseUrl(token: String): String? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://komponenty.vulcan.net.pl/UonetPlusMobile/RoutingRules.txt")
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
            OS = "Android",
            deviceModel = keystore.deviceModel,
            certificate = certificate,
            certificateType = "X509",
            certificateThumbprint = fingerprint,
            pin = pin,
            securityToken = upperToken,
            selfIdentifier = Utils.uuid(fingerprint)
        )

        Log.i("Vulcan API","URL: $fullUrl")
        Log.i("Vulcan API","Registering to $lowerSymbol")



        client.post(fullUrl, pfxRequest).use { response ->
            Log.i("Vulcan API","Response code ${response.code}")
            val body = response.body?.string()
            Log.i("Vulcan API","Response body $body")

            val apiResponse = Gson().fromJson<ApiResponse<HebeAccount>>(body, object : TypeToken<ApiResponse<HebeAccount>>() {}.type)
            session = VulcanLoginResponse(apiResponse.envelope,keystore)

            return apiResponse
        }
    }
}
