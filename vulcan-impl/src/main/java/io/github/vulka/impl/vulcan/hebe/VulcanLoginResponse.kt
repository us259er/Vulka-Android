package io.github.vulka.impl.vulcan.hebe

import io.github.vulka.core.api.ResponseData
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore

data class VulcanLoginResponse(
    val account: HebeAccount,
    val keystore: HebeKeystore
) : ResponseData
