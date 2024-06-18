package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

class HebeLessonRoom(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Code")
    val code: String?
)