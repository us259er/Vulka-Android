package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

data class HebeDate(
    @SerializedName("Timestamp")
    val timestamp: Long,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Time")
    val time: String
)