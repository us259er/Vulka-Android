package io.github.vulka.core.api.response

import io.github.vulka.core.api.LibrusOnly

@LibrusOnly
data class AccountInfo(
    val fullName: String,
    val className: String,
    val index: Int,
    val educator: String
)
