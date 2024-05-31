package io.github.vulka.core.api.types


/**
 * Represent student account in e-journal
 */
data class Student(
    val fullName: String,
    val isParent: Boolean,
    val parent: Parent?,
    val classId: String,
)

/**
 * Represent parent of student
 */
data class Parent(
    val name: String
)