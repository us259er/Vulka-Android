package io.github.vulka.core.api

import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.core.api.types.Student

interface UserClient {
    suspend fun getStudents(): Array<Student>
    @LibrusOnly
    suspend fun getAccountInfo(): AccountInfo
}
