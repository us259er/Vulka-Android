package io.github.vulka.impl.librus.api

import com.google.gson.Gson
import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.applyCookie
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

internal suspend inline fun <reified T> LibrusUserClient.sendAPI(
    endpoint: String
): T {
    val response = client.get("https://synergia.librus.pl/gateway/api/2.0$endpoint") {
        credentials.cookies.forEach {
            applyCookie(it)
        }
    }

    println(response.bodyAsText())

    return Gson().fromJson(response.bodyAsText(), T::class.java)
}
