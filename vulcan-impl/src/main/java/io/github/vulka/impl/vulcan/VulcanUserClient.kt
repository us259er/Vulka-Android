package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.LoginCredentials
import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.core.api.types.Parent
import io.github.vulka.core.api.types.Student
import io.github.vulka.impl.vulcan.hebe.VulcanHebeApi
import io.github.vulka.impl.vulcan.hebe.types.HebeStudent
import java.util.Date

class VulcanUserClient(
    credentials: LoginCredentials
) : UserClient {
    val api = VulcanHebeApi()

    init {
        api.setup(credentials as VulcanLoginCredentials)
    }

    override suspend fun getStudents(): Array<Student> {
        val students = ArrayList<Student>()
        val response = api.getStudents()

        for (student in response) {
            val isParent = student.login.role == "Opiekun"
            students.add(Student(
                fullName = "${student.pupil.firstName} ${student.pupil.lastName}",
                classId = student.classDisplay,
                isParent = isParent,
                parent = if (isParent) Parent(
                    name = student.login.name
                ) else null,
                impl = student
            ))
        }

        return students.toArray(arrayOfNulls(students.size))
    }

    override suspend fun getLuckyNumber(student: Student, date: Date): Int {
        val hebeStudent = student.impl as HebeStudent
        return api.getLuckyNumber(hebeStudent,date)
    }

    override suspend fun getAccountInfo(): AccountInfo {
        TODO("Not yet implemented")
    }
}