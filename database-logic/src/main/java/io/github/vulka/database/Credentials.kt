package io.github.vulka.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.vulka.core.api.Platform
import java.util.*

@Entity
data class Credentials(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val platform: Platform,
    val data: String
)
