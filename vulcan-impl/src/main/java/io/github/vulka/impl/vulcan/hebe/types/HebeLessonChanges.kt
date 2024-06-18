package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

class HebeLessonChanges(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Type")
    val type: Int,
    @SerializedName("Separation")
    val separation: Boolean
)
