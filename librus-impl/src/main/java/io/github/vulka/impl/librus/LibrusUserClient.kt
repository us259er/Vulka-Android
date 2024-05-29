package io.github.vulka.impl.librus

import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.response.AccountInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.jsoup.Jsoup

class LibrusUserClient(
    private val cookies: List<Cookie>
) : UserClient {
    private val client = HttpClient(OkHttp) {
        install(HttpCookies) {
            storage = ConstantCookiesStorage(*cookies.toTypedArray())
        }
    }

    override suspend fun getAccountInfo(): AccountInfo {
        val response = client.get("https://synergia.librus.pl/informacja") {
            cookies.forEach {
                applyCookie(it)
            }
        }

        val html: String = response.body()

        val doc = Jsoup.parse(html)

        val fullName = doc.select("#body > div > div > table > tbody > tr:nth-child(1) > td")
        val className = doc.select("#body > div > div > table > tbody > tr:nth-child(2) > td")
        val index = doc.select("#body > div > div > table > tbody > tr:nth-child(3) > td")
        val educator = doc.select("#body > div > div > table > tbody > tr:nth-child(4) > td")

        return AccountInfo(
            fullName = fullName.text(),
            className = className.text(),
            index = index.text().toInt(),
            educator = educator.text()
        )
    }
}

fun HttpRequestBuilder.applyCookie(cookie: Cookie) = cookie.run {
    val renderedCookie = cookie.let(::renderCookieHeader)
    if (HttpHeaders.Cookie !in headers) {
        headers.append(HttpHeaders.Cookie, renderedCookie)
        return
    }
    // Client cookies are stored in a single header "Cookies" and multiple values are separated with ";"
    headers[HttpHeaders.Cookie] = headers[HttpHeaders.Cookie] + "; " + renderedCookie
}
