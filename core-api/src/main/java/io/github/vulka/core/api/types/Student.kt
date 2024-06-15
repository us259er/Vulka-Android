package io.github.vulka.core.api.types

/**
 * Student platform specific class
 */
open class StudentImpl

/**
 * Represent student account in e-journal
 */
data class Student(
    val fullName: String,
    val isParent: Boolean,
    val parent: Parent?,
    val classId: String?,

    // Platform specific data (should be cast), don't use directly
    val impl: StudentImpl
)

/**
 * Represent parent of student
 */
data class Parent(
    val name: String
)