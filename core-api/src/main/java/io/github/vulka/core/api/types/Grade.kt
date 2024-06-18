package io.github.vulka.core.api.types

import java.time.LocalDate

data class Grade(
    val value: Value,
    val weight: Float,
    val name: String,
    val date: LocalDate,
    val subjectName: String,
    /** Subject code for filtering grades, usually subject name in lowercase */
    val subjectCode: String,
    val teacherName: String
) {
    enum class Value(val str: String, val f: Float) {
        One("1", 1f),
        OnePlus("1+", 1.5f),
        TwoMinus("2-", 1.75f),
        Two("2", 2f),
        TwoPlus("2+", 2.5f),
        ThreeMinus("3-", 2.75f),
        Three("3", 3f),
        ThreePlus("3+", 3.5f),
        FourMinus("4-", 3.75f),
        Four("4", 4f),
        FourPlus("4+", 4.5f),
        FiveMinus("5-", 4.75f),
        Five("5", 5f),
        FivePlus("5+", 5.5f),
        SixMinus("6-", 5.75f),
        Six("6", 6f),
        Plus("+", 0f),
        Minus("-", 0f),
        Other("", 0f);

        companion object {
            fun fromValue(str: String) = entries.find { it.str == str } ?: Other
            fun fromValue(f: Float) = entries.find { it.f == f } ?: Other
        }
    }
}
