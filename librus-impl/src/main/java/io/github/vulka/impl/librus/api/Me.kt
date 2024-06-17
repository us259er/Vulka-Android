package io.github.vulka.impl.librus.api

import com.google.gson.annotations.SerializedName
import io.github.vulka.impl.librus.LibrusUserClient

private const val MeEndpoint = "Me"

data class MeResponse(
    @SerializedName("Me")
    val data: MeResponseData
)

data class MeResponseData(
    @SerializedName("Account")
    val account: MeResponseDataAccount
)

data class MeResponseDataAccount(
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("LastName")
    val lastName: String
)

internal suspend fun LibrusUserClient.meAPI() =
    sendAPI<MeResponse>(MeEndpoint).data
