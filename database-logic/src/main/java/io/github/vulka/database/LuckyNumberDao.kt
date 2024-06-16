package io.github.vulka.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface LuckyNumberDao {
    @Insert
    suspend fun insert(luckyNumber: LuckyNumber)

    @Update
    suspend fun update(luckyNumber: LuckyNumber)

    @Query("SELECT * FROM luckynumber LIMIT 1")
    fun get(): LuckyNumber?

    @Query("SELECT * FROM luckynumber WHERE credentialsId = :id LIMIT 1")
    fun getByCredentialsId(id: UUID): LuckyNumber?
}