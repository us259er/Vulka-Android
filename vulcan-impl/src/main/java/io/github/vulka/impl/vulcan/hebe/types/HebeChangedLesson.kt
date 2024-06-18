package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

data class HebeChangedLesson(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("UnitId")
    val unitId: Int,

    @SerializedName("ScheduleId")
    val scheduleId: Int,

    @SerializedName("LessonDate")
    val lessonDate: HebeDate,

    @SerializedName("Note")
    val note: String? = null,

    @SerializedName("Reason")
    val reason: String? = null,

    @SerializedName("TimeSlot")
    val time: HebeTimeSlot? = null,

    @SerializedName("Room")
    val room: HebeLessonRoom? = null,

    @SerializedName("TeacherPrimary")
    val teacher: HebeTeacher? = null,

    @SerializedName("TeacherSecondary")
    val secondTeacher: HebeTeacher? = null,

    @SerializedName("Subject")
    val subject: HebeSubject? = null,

    @SerializedName("Event")
    val event: String? = null,

    @SerializedName("Change")
    val changes: HebeLessonChanges? = null,

    @SerializedName("ChangeDate")
    val changeDate: HebeDate? = null,

    @SerializedName("Clazz")
    val teamClass: HebeTeamClass? = null,

    @SerializedName("Distribution")
    val group: HebeTeamVirtual? = null
)