package io.github.vulka.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [Credentials::class],
    exportSchema = false
)
abstract class VulkaDatabase : RoomDatabase() {
    abstract fun credentialsDao(): CredentialsDao
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
