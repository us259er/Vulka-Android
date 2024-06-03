package io.github.vulka.core.api

open class LoginData
open class LoginCredentials

interface LoginClient {
    suspend fun login(data: LoginData): LoginCredentials
}
