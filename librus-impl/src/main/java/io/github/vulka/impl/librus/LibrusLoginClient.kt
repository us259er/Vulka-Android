package io.github.vulka.impl.librus

import io.github.vulka.core.api.LoginClient
import io.github.vulka.core.api.LoginCredentials
import io.github.vulka.core.api.LoginData
import io.github.vulka.impl.librus.internal.api.internalRequestMe
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters

class LibrusLoginClient : LoginClient {
    private val client = HttpClient(OkHttp) {
        install(HttpCookies)
    }

    override suspend fun login(data: LoginData): LoginCredentials {
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

        val credentials = LibrusLoginCredentials(
            cookies = cookies,
            request = loginData
        )

        // check if user is logged in
        val userClient = LibrusUserClient(credentials)
        userClient.initClient(cookies)
        userClient.internalRequestMe()

        return credentials
    }
}
