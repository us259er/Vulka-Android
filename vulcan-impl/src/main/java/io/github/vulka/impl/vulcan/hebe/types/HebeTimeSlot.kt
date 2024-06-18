package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

class HebeTimeSlot(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Start")
    val from: String,
    @SerializedName("End")
    val to: String,
    @SerializedName("Display")
    val displayedTime: String,
    @SerializedName("Position")
    val position: Int
)