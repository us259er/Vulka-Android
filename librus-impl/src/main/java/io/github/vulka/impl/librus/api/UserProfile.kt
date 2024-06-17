package io.github.vulka.impl.librus.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.github.vulka.impl.librus.LibrusUserClient
import java.lang.reflect.Type

private const val UserProfileEndpoint = "UserProfile"

data class UserProfileResponse(
    @SerializedName("UserProfile")
    val data: UserProfileResponseData
)

data class UserProfileResponseData(
    @SerializedName("AccountType")
    val accountType: AccountType
)

@JsonAdapter(AccountType.Serializer::class)
enum class AccountType(val value: String) {
    Parent("parent"),
    Student("student");

    class Serializer : JsonSerializer<AccountType>, JsonDeserializer<AccountType> {
        override fun serialize(
            src: AccountType,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return context.serialize(src.value)
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): AccountType {
            val value = context.deserialize<String>(json, String::class.java)
            return entries.first { it.value ==  value}
        }
    }
}

internal suspend fun LibrusUserClient.userProfileAPI() =
    sendAPI<UserProfileResponse>(UserProfileEndpoint).data
