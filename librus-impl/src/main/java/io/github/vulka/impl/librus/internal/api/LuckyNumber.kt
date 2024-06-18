package io.github.vulka.impl.librus.internal.api

import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.internal.api.types.LuckyNumberResponse

internal suspend fun LibrusUserClient.internalRequestLuckyNumber() =
    apiGET<LuckyNumberResponse>("LuckyNumbers").luckyNumber
