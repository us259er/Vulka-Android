package io.github.vulka.core.api

import io.github.vulka.core.api.response.AccountInfo

interface UserClient {
    suspend fun getAccountInfo(): AccountInfo
}
