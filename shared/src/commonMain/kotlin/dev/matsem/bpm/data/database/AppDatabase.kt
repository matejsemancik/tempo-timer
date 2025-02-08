package dev.matsem.bpm.data.database

import androidx.room.*
import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.database.model.User
import dev.matsem.bpm.data.database.typeConverter.JsonTypeConverters

@Database(
    entities = [User::class],
    version = 2
)
@TypeConverters(JsonTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}