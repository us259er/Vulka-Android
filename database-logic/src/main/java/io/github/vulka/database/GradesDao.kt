package io.github.vulka.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface GradesDao {
    @Insert
    suspend fun insert(grades: Grades)

    @Update
    suspend fun update(grades: Grades)

    @Delete
    fun delete(grades: Grades)

    @Query("DELETE FROM grades WHERE credentialsId = :id")
    fun deleteByCredentialsId(id: UUID)

    @Query("SELECT * FROM grades WHERE credentialsId = :id")
    fun getByCredentialsId(id: UUID): List<Grades>?

    @Query("SELECT COUNT(*) FROM grades WHERE subjectName = :subjectName AND credentialsId = :id")
    fun countBySubjectAndCredentials(id: UUID,subjectName: String): Int
}