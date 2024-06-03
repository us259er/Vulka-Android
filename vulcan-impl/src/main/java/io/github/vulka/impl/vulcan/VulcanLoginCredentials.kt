package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.LoginCredentials
import io.github.vulka.impl.vulcan.hebe.HebeAccount
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore

data class VulcanLoginCredentials(
    val account: HebeAccount,
    val keystore: HebeKeystore
) : LoginCredentials()
