package io.github.vulka.ui.utils

import io.github.vulka.core.api.types.Student

fun Student.getInitials(): String {
    return fullName.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
}