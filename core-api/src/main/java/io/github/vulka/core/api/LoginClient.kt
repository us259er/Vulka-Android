package io.github.vulka.core.api

interface LoginClient {
    fun login(data: RequestData): ResponseData
}
