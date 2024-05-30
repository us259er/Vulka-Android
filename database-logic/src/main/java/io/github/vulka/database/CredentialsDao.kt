package io.github.vulka.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CredentialsDao {
    @Insert
    suspend fun insert(credentials: Credentials)

    @Query("SELECT * FROM credentials LIMIT 1")
    fun get(): Credentials?
}
