package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.RequestData
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore

class VulcanLoginData(
    val symbol: String,
    val token: String,
    val pin: String,
    val keystore: HebeKeystore
) : RequestData