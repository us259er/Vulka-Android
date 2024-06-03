package io.github.vulka.impl.librus

import io.github.vulka.core.api.LoginCredentials
import io.ktor.http.*

class LibrusLoginCredentials(
    val cookies: List<Cookie>,
    val request: LibrusLoginData
) : LoginCredentials()
