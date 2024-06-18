package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

data class HebeLesson(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Date")
    val date: HebeDate,
    @SerializedName("TimeSlot")
    val time: HebeTimeSlot,
    @SerializedName("Room")
    val room: HebeLessonRoom?,
    @SerializedName("TeacherPrimary")
    val teacher: HebeTeacher,
    @SerializedName("TeacherSecondary")
    val secondTeacher: HebeTeacher? = null,
    @SerializedName("Subject")
    val subject: HebeSubject,
    @SerializedName("Event")
    val event: String? = null,
    @SerializedName("Change")
    val changes: HebeLessonChanges? = null,
    @SerializedName("Clazz")
    val teamClass: HebeTeamClass? = null,
    @SerializedName("PupilAlias")
    val pupilAlias: String? = null,
    @SerializedName("Distribution")
    val group: HebeTeamVirtual? = null,
    @SerializedName("Visible")
    val visible: Boolean
)



