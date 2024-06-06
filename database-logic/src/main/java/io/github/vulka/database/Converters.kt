package io.github.vulka.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.vulka.core.api.types.Parent
import io.github.vulka.core.api.types.Student

class Converters {
    @TypeConverter
    fun fromStudent(student: Student?): String? {
        if (student == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(student)
    }

    @TypeConverter
    fun toStudent(studentString: String?): Student? {
        if (studentString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<Student>() {}.type
        return gson.fromJson(studentString, type)
    }
}
