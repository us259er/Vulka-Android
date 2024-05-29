package io.github.vulka.impl.librus

import io.github.vulka.core.api.LoginClient
import io.github.vulka.core.api.RequestData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class LibrusLoginClient : LoginClient {
    private val client = HttpClient(OkHttp) {
        install(HttpCookies)
    }

    override suspend fun login(data: RequestData): LibrusLoginResponse {
        val loginData = data as LibrusLoginData

        client.get("https://api.librus.pl/OAuth/Authorization?client_id=46&response_type=code&scope=mydata")

        client.submitForm(
            url = "https://api.librus.pl/OAuth/Authorization?client_id=46",
            formParameters = parameters {
                append("action", "login")
                append("login", loginData.login)
                append("pass", loginData.password)
            }
        )

        client.get("https://api.librus.pl/OAuth/Authorization/2FA?client_id=46")

        val cookies = client.cookies("https://synergia.librus.pl")

        return LibrusLoginResponse(cookies)
    }
}
