package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.core.api.types.Parent
import io.github.vulka.core.api.types.Student
import io.github.vulka.impl.vulcan.hebe.VulcanHebeApi

class VulcanUserClient(
    credentials: VulcanLoginResponse
) : UserClient {
    val api = VulcanHebeApi()

    init {
        api.setup(credentials)
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
            ))
        }

        return students.toArray(arrayOfNulls(students.size))
    }

    override suspend fun getAccountInfo(): AccountInfo {
        TODO("Not yet implemented")
    }
}