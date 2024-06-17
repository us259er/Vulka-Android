package io.github.vulka.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 2,
    entities = [Credentials::class, LuckyNumber::class, Grades::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VulkaDatabase : RoomDatabase() {
    abstract fun credentialsDao(): CredentialsDao
    abstract fun luckyNumberDao(): LuckyNumberDao
    abstract fun gradesDao(): GradesDao
}

/**
 * Database provider singleton class.
 */
object DatabaseProvider {
    private var database: VulkaDatabase? = null

    /**
     * Get database instance. If database is not initialized, it will be initialize.
     *
     * @param context Application context.
     * @return Database instance.
     */
    fun getInstance(context: Context): VulkaDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context,
                VulkaDatabase::class.java,
                "vulka.db"
            )
                .allowMainThreadQueries()
                .build()
        }

        return database as VulkaDatabase
    }
}
