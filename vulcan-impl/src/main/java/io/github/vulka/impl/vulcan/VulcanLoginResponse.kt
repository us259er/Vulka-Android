package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.ResponseData
import io.github.vulka.impl.vulcan.hebe.HebeAccount
import io.github.vulka.impl.vulcan.hebe.login.Keystore

data class VulcanLoginResponse(
    val account: HebeAccount?,
    val keystore: Keystore?
) : ResponseData
