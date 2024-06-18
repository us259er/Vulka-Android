package io.github.vulka.impl.librus

import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.types.Grade
import io.github.vulka.core.api.types.Parent
import io.github.vulka.core.api.types.Student
import io.github.vulka.core.api.types.StudentImpl
import io.github.vulka.impl.librus.internal.api.internalRequestClass
import io.github.vulka.impl.librus.internal.api.internalRequestGrades
import io.github.vulka.impl.librus.internal.api.internalRequestLuckyNumber
import io.github.vulka.impl.librus.internal.api.internalRequestMe
import io.github.vulka.impl.librus.internal.api.internalRequestSubjects
import io.github.vulka.impl.librus.internal.api.internalRequestUserProfile
import io.github.vulka.impl.librus.internal.api.internalRequestUsers
import io.github.vulka.impl.librus.internal.api.types.UserProfile
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cookies.ConstantCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.renderCookieHeader
import java.util.Date

class LibrusUserClient(
    internal var credentials: LibrusLoginCredentials
) : UserClient {
    internal lateinit var client: HttpClient

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
        val userProfile = internalRequestUserProfile()
        val me = internalRequestMe()
        val classInfo = internalRequestClass(me.class_.id)

        return arrayOf(
            Student(
                // maybe "user" for parent account and "account" for student account
                fullName = "${me.user.firstName} ${me.user.lastName}",
                isParent = userProfile.accountType == UserProfile.AccountType.PARENT,
                parent = Parent(
                    name = "${me.account.firstName} ${me.account.lastName}"
                ),
                classId = "${classInfo.number}${classInfo.symbol}",
                impl = StudentImpl()
            )
        )
    }

    override suspend fun getLuckyNumber(student: Student, date: Date): Int {
        val response = internalRequestLuckyNumber()
        return response.luckyNumber
    }

    override suspend fun getGrades(student: Student): Array<Grade> {
        val grades = internalRequestGrades()
//        val categories = internalRequestGradesCategories()
        val subjects = internalRequestSubjects()
        val teachers = internalRequestUsers()

        val gradesList = ArrayList<Grade>()

        for (grade in grades) {
            val teacher = teachers.find { it.id == grade.addedBy.id }
            val subject = subjects.find { it.id == grade.subject.id }

            gradesList.add(
                Grade(
                    value = 1f,
                    weight = 1.0f,
                    name = grade.grade,
                    date = grade.date,
                    subjectName = subject?.name ?: "stub",
                    subjectCode = "stub",
                    teacherName = "${teacher?.firstName} ${teacher?.lastName}"
                )
            )
        }

        return gradesList.toTypedArray()

//        val grades = ArrayList<Grade>()
//        grades.add(
//            Grade(
//                value = 1.0f,
//                weight = 1.0f,
//                name = "Stub",
//                date = "2024-04-04",
//                subjectName = "Stub",
//                subjectCode = "stub",
//                teacherName = "Stub Stub"
//            )
//        )
//        return grades.toTypedArray()
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
