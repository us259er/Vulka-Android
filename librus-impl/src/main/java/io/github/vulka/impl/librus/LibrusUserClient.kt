package io.github.vulka.impl.librus

import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.types.Grade
import io.github.vulka.core.api.types.Lesson
import io.github.vulka.core.api.types.Parent
import io.github.vulka.core.api.types.Student
import io.github.vulka.core.api.types.StudentImpl
import io.github.vulka.impl.librus.internal.api.internalRequestClass
import io.github.vulka.impl.librus.internal.api.internalRequestGrades
import io.github.vulka.impl.librus.internal.api.internalRequestGradesCategories
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
import java.time.LocalDate
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
        val categories = internalRequestGradesCategories()
        val subjects = internalRequestSubjects()
        val teachers = internalRequestUsers()

        val gradesList = ArrayList<Grade>()

        for (grade in grades) {
            val teacher = teachers.find { it.id == grade.addedBy.id }
            val subject = subjects.find { it.id == grade.subject.id }!!
            val category = categories.find { it.id == grade.category.id }!!

            gradesList.add(
                Grade(
                    value = Grade.Value.fromValue(grade.grade),
                    weight = category.weight,
                    name = category.name,
                    date = LocalDate.parse(grade.date),
                    subjectName = subject.name,
                    subjectCode = subject.short,
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

    // Some advanced stub
    override suspend fun getLessons(
        student: Student,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): Array<Lesson> {
        val lessons = ArrayList<Lesson>()

        var currentDate = dateFrom
        while (!currentDate.isAfter(dateTo)) {
            for (i in 1..7) {
                if (currentDate.isAfter(dateTo))
                    break // Stop adding lessons beyond dateTo

                val startTime = when (i) {
                    1 -> "08:00"
                    2 -> "09:00"
                    3 -> "10:00"
                    4 -> "11:00"
                    5 -> "13:00"
                    6 -> "14:00"
                    7 -> "15:00"
                    else -> "08:00"
                }

                val endTime = when (i) {
                    1 -> "08:45"
                    2 -> "09:45"
                    3 -> "10:45"
                    4 -> "11:45"
                    5 -> "13:45"
                    6 -> "14:45"
                    7 -> "15:45"
                    else -> "08:45"
                }

                lessons.add(
                    Lesson(
                        subjectName = "Stub subject",
                        position = i,
                        teacherName = "Stub teacher",
                        room = "100",
                        groupName = "stub.group",
                        date = currentDate,
                        startTime = startTime,
                        endTime = endTime
                    )
                )
            }

            currentDate = currentDate.plusDays(1)
        }

        return lessons.toTypedArray()
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
