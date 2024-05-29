package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.LoginClient
import io.github.vulka.core.api.RequestData

class VulcanLoginClient : LoginClient {
    override fun login(data: RequestData): VulcanLoginResponse {
        println(data as VulcanLoginData)
        return VulcanLoginResponse(data.symbol)
    }
}
