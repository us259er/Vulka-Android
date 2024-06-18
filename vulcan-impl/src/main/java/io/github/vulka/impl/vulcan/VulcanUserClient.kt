package io.github.vulka.impl.vulcan

import io.github.vulka.core.api.LoginCredentials
import io.github.vulka.core.api.UserClient
import io.github.vulka.core.api.types.Grade
import io.github.vulka.core.api.types.Lesson
import io.github.vulka.core.api.types.Parent
import io.github.vulka.core.api.types.Student
import io.github.vulka.impl.vulcan.hebe.VulcanHebeApi
import io.github.vulka.impl.vulcan.hebe.types.HebeStudent
import java.time.LocalDate
import java.time.LocalTime
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

    override suspend fun getGrades(student: Student): Array<Grade> {
        val hebeStudent = student.impl as HebeStudent
        val currentPeriod = hebeStudent.periods.find { it.current }!!
        val response = api.getGrades(hebeStudent,currentPeriod)

        val grades = ArrayList<Grade>()

        for (grade in response) {
            grades.add(Grade(
                value = grade.value,
                weight = grade.column.weight,
                name = grade.column.name,
                date = LocalDate.parse(grade.dateCreated.date),
                subjectName = grade.column.subject.name,
                subjectCode = grade.column.subject.code,
                teacherName = grade.teacherCreated.displayName
            ))
        }

        return grades.toTypedArray()
    }

    override suspend fun getLessons(student: Student, dateFrom: LocalDate, dateTo: LocalDate): Array<Lesson> {
        val hebeStudent = student.impl as HebeStudent

        val lessonsResponse = api.getLessons(hebeStudent,dateFrom,dateTo)
        val changedLesson = api.getChangedLessons(hebeStudent,dateFrom,dateTo)

        val lessons = ArrayList<Lesson>()

        for (lesson in lessonsResponse) {
            // Skip lessons that is not for this student
            if (!lesson.visible)
                continue

            lessons.add(
                Lesson(
                    subjectName = lesson.subject.name,
                    startTime = lesson.time.from,
                    endTime = lesson.time.to,
                    room = lesson.room?.code,
                    position = lesson.time.position,
                    change = null,
                    date = LocalDate.parse(lesson.date.date),
                    teacherName = lesson.teacher.name
                )
            )
        }

        return lessons.toTypedArray()
    }
}