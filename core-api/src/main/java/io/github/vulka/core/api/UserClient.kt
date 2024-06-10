package io.github.vulka.core.api

import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.core.api.types.Student
import java.util.*

interface UserClient {
    suspend fun getStudents(): Array<Student>
    suspend fun getLuckyNumber(student: Student,date: Date): Int
    @LibrusOnly
    suspend fun getAccountInfo(): AccountInfo
}
