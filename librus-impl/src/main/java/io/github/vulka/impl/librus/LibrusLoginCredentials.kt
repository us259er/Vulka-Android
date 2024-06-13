package io.github.vulka.impl.librus

import io.github.vulka.core.api.LoginCredentials
import io.ktor.http.*

class LibrusLoginCredentials(
    @Transient
    val cookies: List<Cookie>,
    val request: LibrusLoginData
) : LoginCredentials()
