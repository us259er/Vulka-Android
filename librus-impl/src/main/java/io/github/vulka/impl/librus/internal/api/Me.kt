package io.github.vulka.impl.librus.internal.api

import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.internal.api.types.MeResponse

internal suspend fun LibrusUserClient.internalRequestMe() =
    apiGET<MeResponse>("Me").me
