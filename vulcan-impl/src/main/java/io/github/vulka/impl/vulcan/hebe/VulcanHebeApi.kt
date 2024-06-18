package io.github.vulka.impl.vulcan.hebe

import io.github.vulka.core.api.log.LoggerFactory
import io.github.vulka.impl.vulcan.Utils
import io.github.vulka.impl.vulcan.VulcanLoginCredentials
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore
import io.github.vulka.impl.vulcan.hebe.login.PfxRequest
import io.github.vulka.impl.vulcan.hebe.types.HebeAccount
import io.github.vulka.impl.vulcan.hebe.types.HebeChangedLesson
import io.github.vulka.impl.vulcan.hebe.types.HebeGrade
import io.github.vulka.impl.vulcan.hebe.types.HebeLesson
import io.github.vulka.impl.vulcan.hebe.types.HebeLuckyNumber
import io.github.vulka.impl.vulcan.hebe.types.HebePeriod
import io.github.vulka.impl.vulcan.hebe.types.HebeStudent
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class VulcanHebeApi {
    private val log = LoggerFactory.get(VulcanHebeApi::class.java)

    private lateinit var client: HebeHttpClient
    private lateinit var credentials: VulcanLoginCredentials

    fun setup(credentials: VulcanLoginCredentials) {
        client = HebeHttpClient(credentials.keystore)
        this.credentials = credentials
    }


    @Throws(IOException::class)
    fun getBaseUrl(token: String): String? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://komponenty.vulcan.net.pl/UonetPlusMobile/RoutingRules.txt")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val content = response.body?.string() ?: return null
            val lines = content.lines()

            for (line in lines) {
                if (line.startsWith(token.substring(0,3)))
                    return line.substring(line.indexOf(",") + 1)
            }
        }

        return null
    }

    private fun getRestUrl(student: HebeStudent): String {
        return credentials.account.restUrl + student.unit.code
    }

    @Throws(Exception::class)
    fun register(keystore: HebeKeystore, symbol: String, token: String, pin: String): HebeAccount {
        val upperToken = token.uppercase()
        val lowerSymbol = symbol.lowercase()

        client = HebeHttpClient(keystore)

        val baseUrl = getBaseUrl(token)

        val fullUrl = "$baseUrl/$lowerSymbol/${ApiEndpoints.DEVICE_REGISTER}"

        val (certificate,fingerprint,_) = keystore.getData()

        val pfxRequest = PfxRequest(
            OS = "Android",
            deviceModel = keystore.deviceModel,
            certificate = certificate,
            certificateType = "X509",
            certificateThumbprint = fingerprint,
            pin = pin,
            securityToken = upperToken,
            selfIdentifier = Utils.uuid(fingerprint)
        )

        log.info("URL: $fullUrl")
        log.info("Registering to $lowerSymbol")

        val response = client.post(fullUrl, pfxRequest,HebeAccount::class.java)

        credentials = VulcanLoginCredentials(response!!,keystore)
        return response
    }

    fun getStudents(): Array<HebeStudent> {
        val baseUrl = credentials.account.restUrl

        val fullUrl = "$baseUrl${ApiEndpoints.STUDENT_LIST}"

        log.info("Students URL: $fullUrl")

        return client.get(fullUrl, Array<HebeStudent>::class.java)!!
    }

    fun getLuckyNumber(student: HebeStudent,date: Date): Int {
        val baseUrl = getRestUrl(student)
        val response = client.get(
            url = "$baseUrl/${ApiEndpoints.DATA_ROOT}/${ApiEndpoints.DATA_LUCKY_NUMBER}",
            query = mapOf(
                "constituentId" to student.school.id.toString(),
                "day" to SimpleDateFormat("yyyy-MM-dd").format(date)
            ),
            clazz = HebeLuckyNumber::class.java
        )
        return response!!.number
    }

    fun getGrades(student: HebeStudent,period: HebePeriod): Array<HebeGrade> = runBlocking {
        val baseUrl = getRestUrl(student)

        val response = client.get(
            url = "$baseUrl/${ApiEndpoints.DATA_ROOT}/${ApiEndpoints.DATA_GRADE}/${ApiEndpoints.DATA_BY_PUPIL}",
            query = mapOf(
                "unitId" to student.unit.id.toString(),
                "pupilId" to student.pupil.id.toString(),
                "periodId" to period.id.toString(),

                "lastId" to "-2147483648",  // don't ask, it's just Vulcan
                "pageSize" to 500.toString()
            ),
            clazz = Array<HebeGrade>::class.java
        )!!

        return@runBlocking response
    }

    fun getLessons(student: HebeStudent,dateFrom: LocalDate = LocalDate.parse("2024-06-12"),dateTo: LocalDate = dateFrom): Array<HebeLesson> = runBlocking {
        val baseUrl = getRestUrl(student)

        val currentPeriod = student.periods.find { it.current }!!

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val response = client.get(
            url = "$baseUrl/${ApiEndpoints.DATA_ROOT}/${ApiEndpoints.DATA_TIMETABLE}/${ApiEndpoints.DATA_BY_PUPIL}",
            query = mapOf(
                "unitId" to student.unit.id.toString(),
                "pupilId" to student.pupil.id.toString(),
                "periodId" to currentPeriod.id.toString(),

                "lastId" to "-2147483648",  // don't ask, it's just Vulcan
                "pageSize" to 500.toString(),

                "dateFrom" to dateFrom.format(dateFormatter),
                "dateTo" to dateTo.format(dateFormatter)
            ),
            clazz = Array<HebeLesson>::class.java
        )!!

        return@runBlocking response
    }

    fun getChangedLessons(student: HebeStudent,dateFrom: LocalDate = LocalDate.parse("2024-06-12"),dateTo: LocalDate = dateFrom): Array<HebeChangedLesson> = runBlocking {
        val baseUrl = getRestUrl(student)

        val currentPeriod = student.periods.find { it.current }!!

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val response = client.get(
            url = "$baseUrl/${ApiEndpoints.DATA_ROOT}/${ApiEndpoints.DATA_TIMETABLE_CHANGES}/${ApiEndpoints.DATA_BY_PUPIL}",
            query = mapOf(
                "unitId" to student.unit.id.toString(),
                "pupilId" to student.pupil.id.toString(),
                "periodId" to currentPeriod.id.toString(),

                "lastId" to "-2147483648",  // don't ask, it's just Vulcan
                "pageSize" to 500.toString(),

                "dateFrom" to dateFrom.format(dateFormatter),
                "dateTo" to dateTo.format(dateFormatter)
            ),
            clazz = Array<HebeChangedLesson>::class.java
        )!!

        return@runBlocking response
    }
}
