package io.github.vulka.impl.librus

import io.github.vulka.core.api.LoginClient
import io.github.vulka.core.api.RequestData

class LibrusLoginClient : LoginClient {
    override fun login(data: RequestData): LibrusLoginResponse {
        println(data as LibrusLoginData)
        return LibrusLoginResponse()
    }
}
