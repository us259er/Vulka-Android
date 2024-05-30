package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.response.AccountInfo

class VulcanUserClient : UserClient{
    override suspend fun getAccountInfo(): AccountInfo {
        TODO("Not yet implemented")
    }
}