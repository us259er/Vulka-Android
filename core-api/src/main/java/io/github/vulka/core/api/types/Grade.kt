package io.github.vulka.core.api.types

data class Grade(
    val value: Float?,
    val weight: Float,
    val name: String,
    val date: String,
    val subjectName: String,
    /** Subject code for filtering grades, usually subject name in lowercase*/
    val subjectCode: String,
    val teacherName: String
)