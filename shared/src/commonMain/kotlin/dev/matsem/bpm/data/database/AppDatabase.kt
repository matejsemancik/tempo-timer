package dev.matsem.bpm.data.database

import androidx.room.*
import dev.matsem.bpm.data.database.dao.JiraIssueDao
import dev.matsem.bpm.data.database.dao.TimerDao
import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.database.model.FavouriteIssue
import dev.matsem.bpm.data.database.model.JiraIssue
import dev.matsem.bpm.data.database.model.Timer
import dev.matsem.bpm.data.database.model.User
import dev.matsem.bpm.data.database.typeConverter.JsonTypeConverters

@Database(
    entities = [User::class, JiraIssue::class, FavouriteIssue::class, Timer::class],
    version = 5
)
@TypeConverters(JsonTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun jiraIssueDao(): JiraIssueDao

    abstract fun timerDao(): TimerDao
}

internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}