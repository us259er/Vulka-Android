package io.github.vulka.core.api

interface LoginClient {
    suspend fun login(data: RequestData): ResponseData
}
