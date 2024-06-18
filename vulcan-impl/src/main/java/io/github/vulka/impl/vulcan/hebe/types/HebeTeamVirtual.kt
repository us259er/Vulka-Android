package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName


class HebeTeamVirtual(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Key")
    val key: String,
    @SerializedName("Shortcut")
    val shortcut: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("PartType")
    val partType: String
)