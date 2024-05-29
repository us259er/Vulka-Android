package io.github.vulka.impl.vulcan.hebe

import io.github.vulka.impl.vulcan.hebe.login.Keystore

data class VulcanSession(
    val account: HebeAccount,
    val keystore: Keystore
)