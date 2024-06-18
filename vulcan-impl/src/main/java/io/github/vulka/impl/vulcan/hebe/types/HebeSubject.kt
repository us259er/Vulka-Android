package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName


data class HebeSubject(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Key")
    val key: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Kod")
    val code: String,
    @SerializedName("Position")
    val position: Int
)