package io.github.vulka.impl.vulcan.hebe.types

import com.google.gson.annotations.SerializedName
import io.github.vulka.core.api.types.StudentImpl

data class HebeStudent(
    @SerializedName("ClassDisplay")
    val classDisplay: String,

    @SerializedName("Login")
    val login: Login,

    @SerializedName("TopLevelPartition")
    val symbol: String,

    @SerializedName("Partition")
    val symbolCode: String,

    @SerializedName("State")
    val state: Int,

    @SerializedName("Pupil")
    val pupil: Pupil,

    @SerializedName("Unit")
    val unit: SchoolUnit,

    @SerializedName("ConstituentUnit")
    val school: School,

    @SerializedName("MessageBox")
    val messageBox: MessageBox,

    @SerializedName("Periods")
    val periods: List<HebePeriod>
) : StudentImpl()

data class Login(
    @SerializedName("Value")
    val email: String,
    @SerializedName("DisplayName")
    val name: String,
    @SerializedName("LoginRole")
    val role: String
)

data class SchoolUnit(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Symbol")
    val code: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Short")
    val shortName: String,

    @SerializedName("DisplayName")
    val displayName: String,

    @SerializedName("RestURL")
    val restUrl: String,

    @SerializedName("Address")
    val address: String? = null
)

data class Pupil(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("LoginId")
    val loginId: Int,

    @SerializedName("FirstName")
    val firstName: String,

    @SerializedName("Surname")
    val lastName: String,

    @SerializedName("Sex")
    val gender: Boolean,

    @SerializedName("SecondName")
    val secondName: String? = null,

    @SerializedName("LoginValue")
    val loginValue: String? = null
)

data class MessageBox(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("GlobalKey")
    val globalKey: String,

    @SerializedName("Name")
    val name: String
)

data class School(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Short")
    val shortName: String,

    @SerializedName("Address")
    val address: String? = null
)

data class HebePeriod(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Level")
    val level: Int,

    @SerializedName("Number")
    val number: Int,

    @SerializedName("Current")
    val current: Boolean,

    @SerializedName("Last")
    val last: Boolean,

    @SerializedName("Start")
    val start: HebeDate,

    @SerializedName("End")
    val end: HebeDate
)

