package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.LoginClient
import io.github.vulka.core.api.RequestData
import io.github.vulka.impl.vulcan.hebe.VulcanHebeApi

class VulcanLoginClient : LoginClient {
    private val api = VulcanHebeApi()

    override suspend fun login(data: RequestData): VulcanLoginResponse {
        val loginData = data as VulcanLoginData
        val response = api.register(loginData.keystore, loginData.symbol, loginData.token, loginData.pin)

        return VulcanLoginResponse(response.envelope!!,data.keystore)
    }
}