package io.github.vulka.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import io.github.vulka.core.api.Platform
import java.util.*

@Entity
data class Credentials(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val platform: Platform,
    @field:TypeConverters(CredentialsDataConverter::class)
    val data: CredentialsData
)

class CredentialsDataConverter {
    @TypeConverter
    fun fromCredentialsData(credentialsData: CredentialsData): String {
        return Gson().toJson(credentialsData)
    }

    @TypeConverter
    fun toCredentialsData(json: String): CredentialsData {
        return Gson().fromJson(json, CredentialsData::class.java)
    }
}
