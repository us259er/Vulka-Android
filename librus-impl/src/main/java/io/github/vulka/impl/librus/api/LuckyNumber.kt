package io.github.vulka.impl.librus.api

import com.google.gson.annotations.SerializedName
import io.github.vulka.impl.librus.LibrusUserClient

private const val LuckyNumberEndpoint = "LuckyNumbers"

data class LuckyNumberResponse(
    @SerializedName("LuckyNumber")
    val data: LuckyNumberResponseData
)

data class LuckyNumberResponseData(
    @SerializedName("LuckyNumber")
    val luckyNumber: Int
//    @SerializedName("LuckyNumberDay")
//    val luckyNumberDay: String
)

internal suspend fun LibrusUserClient.luckyNumberAPI() =
    sendAPI<LuckyNumberResponse>(LuckyNumberEndpoint).data
