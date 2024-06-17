package io.github.vulka.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.vulka.core.api.types.Grade
import java.util.UUID

@Entity
data class Grades(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @Embedded
    val grade: Grade,
    val credentialsId: UUID
)