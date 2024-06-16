package io.github.vulka.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface CredentialsDao {
    @Insert
    suspend fun insert(credentials: Credentials)

    @Update
    suspend fun update(credentials: Credentials)

    @Query("SELECT * FROM credentials LIMIT 1")
    fun get(): Credentials?

    @Query("SELECT * FROM credentials WHERE id=:id LIMIT 1")
    fun getById(id: UUID): Credentials?

    @Query("SELECT * FROM credentials")
    fun getAll(): List<Credentials>
}
