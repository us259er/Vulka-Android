package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.LoginClient
import io.github.vulka.core.api.RequestData
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

class VulcanLoginClient : LoginClient {
    private val client = HttpClient(OkHttp)

    override suspend fun login(data: RequestData): VulcanLoginResponse {
        println(data as VulcanLoginData)
        return VulcanLoginResponse(data.symbol)
    }
}
