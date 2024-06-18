package io.github.vulka.core.api.types

import java.time.LocalDate

data class Lesson(
    val subjectName: String,
    val position: Int,
    val room: String? = null,
    val teacherName: String,
    val groupName: String? = null,
    val change: LessonChange? = null,

    val date: LocalDate,
    val startTime: String,
    val endTime: String,
)
// TODO: Add more fields, e.g change type
data class LessonChange(
    val reason: String? = null,
    val room: String? = null,
)