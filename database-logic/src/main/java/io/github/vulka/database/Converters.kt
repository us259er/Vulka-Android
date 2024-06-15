package io.github.vulka.database

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import io.github.vulka.core.api.types.Parent
import java.lang.reflect.Type
import io.github.vulka.core.api.types.Student
import io.github.vulka.core.api.types.StudentImpl
import io.github.vulka.impl.vulcan.hebe.types.HebeStudent

class Converters {
    @TypeConverter
    fun fromStudent(student: Student): String {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Student::class.java, StudentTypeAdapter())
            .create()
        val s = gson.toJson(student)
        Log.d("Room Converters",s)
        return s
    }

    @TypeConverter
    fun toStudent(studentString: String): Student {
        Log.d("Room Converters",studentString)
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Student::class.java, StudentTypeAdapter())
            .create()
        val type = object : TypeToken<Student>() {}.type
        val student = gson.fromJson<Student>(studentString, type)
        return student
    }
}

// TODO: Make simpler Student deserialized
class StudentTypeAdapter : JsonSerializer<Student>, JsonDeserializer<Student> {
    override fun serialize(src: Student, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("fullName", src.fullName)
        jsonObject.addProperty("isParent", src.isParent)
        jsonObject.add("parent", context.serialize(src.parent))
        jsonObject.addProperty("classId", src.classId)

        val implObject = when (src.impl) {
            is HebeStudent -> context.serialize(src.impl, HebeStudent::class.java)
            else -> JsonObject()
        }
        jsonObject.add("impl", implObject)

        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Student {
        val jsonObject = json.asJsonObject

        val fullName = jsonObject.get("fullName").asString
        val isParent = jsonObject.get("isParent").asBoolean
        val parent = context.deserialize<Parent>(jsonObject.get("parent"), Parent::class.java)
        val classId = jsonObject.get("classId").asString

        val implObject = jsonObject.get("impl")
        val impl = if (implObject.asJsonObject.has("ClassDisplay")) {
            context.deserialize<HebeStudent>(implObject, HebeStudent::class.java)
        } else {
            StudentImpl()
        }

        return Student(fullName, isParent, parent, classId, impl)
    }
}