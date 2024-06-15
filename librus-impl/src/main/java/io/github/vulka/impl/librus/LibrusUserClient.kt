package io.github.vulka.impl.librus

import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.core.api.types.Parent
import io.github.vulka.core.api.types.Student
import io.github.vulka.core.api.types.StudentImpl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cookies.ConstantCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.renderCookieHeader
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.Date

class LibrusUserClient(
    private var credentials: LibrusLoginCredentials
) : UserClient {
    private lateinit var client: HttpClient

    internal fun initClient(cookies: List<Cookie>) {
        client = HttpClient(OkHttp) {
            install(HttpCookies) {
                storage = ConstantCookiesStorage(*cookies.toTypedArray())
            }
        }
    }

    suspend fun renewCredentials() {
        val loginData = credentials.request
        credentials = LibrusLoginClient().login(loginData) as LibrusLoginCredentials
        initClient(credentials.cookies)
    }

    override suspend fun getStudents(): Array<Student> {
        val document = parse("/informacja")

        val fullName = document.select("#body > div > div > table > tbody > tr:nth-child(1) > td")
        val className = document.select("#body > div > div > table > tbody > tr:nth-child(2) > td")

        // check if the account is a parent
        var parent: Parent? = null
        try {
            // "My account" ("Moje konto") section is only on the parent page, if it exists it is a parent
            val myAccountSection = document.select("#body > div > div > table > tbody > tr:nth-child(10) > td")

            if (myAccountSection.isNotEmpty()) {
                val parentName = document.select("#body > div > div > table > tbody > tr:nth-child(7) > td")

                parent = Parent(
                    name = parentName.text()
                )
            }
        } catch (_: Exception) {
            // ignore
        }

        return arrayOf(
            Student(
                fullName = fullName.text(),
                isParent = parent != null,
                parent = parent,
                classId = className.text(),
                impl = StudentImpl()
            )
        )
    }

    override suspend fun getLuckyNumber(student: Student, date: Date): Int {
        val document = parse("${getBaseEndpoint(student)}/index")

        val number = document.select(".luckyNumber")
        return number.text().toInt()
    }

    override suspend fun getAccountInfo(): AccountInfo {
        val document = parse("/informacja")

        val fullName = document.select("#body > div > div > table > tbody > tr:nth-child(1) > td")
        val className = document.select("#body > div > div > table > tbody > tr:nth-child(2) > td")
        val index = document.select("#body > div > div > table > tbody > tr:nth-child(3) > td")
        val educator = document.select("#body > div > div > table > tbody > tr:nth-child(4) > td")

        return AccountInfo(
            fullName = fullName.text(),
            className = className.text(),
            index = index.text().toInt(),
            educator = educator.text()
        )
    }

    private suspend fun parse(endpoint: String): Document {
        val response = client.get("https://synergia.librus.pl$endpoint") {
            credentials.cookies.forEach {
                applyCookie(it)
            }
        }

        val html: String = response.body()

        return Jsoup.parse(html)
    }

    private fun getBaseEndpoint(student: Student): String {
        return if (student.isParent) "/rodzic" else "/uczen"
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
