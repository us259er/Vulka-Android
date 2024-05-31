package io.github.vulka.impl.librus

import io.github.vulka.core.api.ResponseData
import io.ktor.http.*

class LibrusLoginResponse(
    val cookies: List<Cookie>,
    val request: LibrusLoginData
) : ResponseData
