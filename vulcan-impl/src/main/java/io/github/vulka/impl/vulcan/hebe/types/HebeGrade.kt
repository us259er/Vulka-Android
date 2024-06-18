package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName

data class HebeGrade(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Key")
    val key: String,
    @SerializedName("PupilId")
    val pupilId: Int,
    @SerializedName("ContentRaw")
    val contentRaw: String,
    @SerializedName("Content")
    val content: String,
    @SerializedName("DateCreated")
    val dateCreated: HebeDate,
    @SerializedName("DateModify")
    val dateModified: HebeDate,
    @SerializedName("Creator")
    val teacherCreated: HebeTeacher,
    @SerializedName("Modifier")
    val teacherModified: HebeTeacher,
    @SerializedName("Column")
    val column: HebeGradeColumn,
    @SerializedName("Value")
    val value: Float? = null,
    @SerializedName("Comment")
    val comment: String? = null,
    @SerializedName("Numerator")
    val numerator: Float? = null,
    @SerializedName("Denominator")
    val denominator: Float? = null
)

data class HebeGradeColumn(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Key")
    val key: String,
    @SerializedName("PeriodId")
    val periodId: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Code")
    val code: String,
    @SerializedName("Number")
    val number: Int,
    @SerializedName("Weight")
    val weight: Float,
    @SerializedName("Subject")
    val subject: HebeSubject,
    @SerializedName("Group")
    val group: String? = null,
    @SerializedName("Category")
    val category: HebeGradeCategory? = null,
    @SerializedName("Period")
    val period: HebePeriod? = null
)

data class HebeGradeCategory(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Code")
    val code: String
)
