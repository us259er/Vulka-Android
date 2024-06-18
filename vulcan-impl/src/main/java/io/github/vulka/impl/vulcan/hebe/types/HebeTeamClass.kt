package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

class HebeTeamClass(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Key")
    val key: String,
    @SerializedName("DisplayName")
    val displayName: String,
    @SerializedName("Symbol")
    val symbol: String
)