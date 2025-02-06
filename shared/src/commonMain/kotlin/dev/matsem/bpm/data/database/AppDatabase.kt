package dev.matsem.bpm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.model.database.User

@Database(
    entities = [User::class],
    version = 1,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}