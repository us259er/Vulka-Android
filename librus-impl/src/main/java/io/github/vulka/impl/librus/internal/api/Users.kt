package io.github.vulka.impl.librus.internal.api

import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.internal.api.types.UsersResponse

internal suspend fun LibrusUserClient.internalRequestUsers() =
    apiGET<UsersResponse>("Users").users
