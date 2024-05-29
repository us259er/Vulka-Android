package io.github.vulka.impl.vulcan.hebe

import com.google.gson.annotations.SerializedName

data class HebeAccount(
    @SerializedName("LoginId")
    val loginId: Int,
    @SerializedName("RestURL")
    val restUrl: String,
    @SerializedName("UserLogin")
    val userLogin: String,
    @SerializedName("UserName")
    val userName: String
)
