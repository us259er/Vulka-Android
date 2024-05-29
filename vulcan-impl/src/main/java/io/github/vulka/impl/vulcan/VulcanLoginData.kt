package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.RequestData

class VulcanLoginData(
    val symbol: String,
    val token: String,
    val pin: String
) : RequestData
