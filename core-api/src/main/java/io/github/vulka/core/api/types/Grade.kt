package io.github.vulka.core.api.types

import java.time.LocalDate

data class Grade(
    val value: Float?,
    val weight: Float,
    val name: String,
    val date: LocalDate,
    val subjectName: String,
    /** Subject code for filtering grades, usually subject name in lowercase*/
    val subjectCode: String,
    val teacherName: String
)