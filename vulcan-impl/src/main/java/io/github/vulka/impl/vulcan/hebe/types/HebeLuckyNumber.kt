package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

data class HebeLuckyNumber(
    @SerializedName("Day")
    val day: String,
    @SerializedName("Number")
    val number: Int
)