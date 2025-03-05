package dev.matsem.bpm.data.database

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import dev.matsem.bpm.data.database.dao.JiraIssueDao
import dev.matsem.bpm.data.database.dao.TimerDao
import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.database.model.FavouriteIssue
import dev.matsem.bpm.data.database.model.JiraIssue
import dev.matsem.bpm.data.database.model.Timer
import dev.matsem.bpm.data.database.model.User
import dev.matsem.bpm.data.database.typeConverter.JsonTypeConverters
import org.koin.core.component.KoinComponent

@Database(
    entities = [User::class, JiraIssue::class, FavouriteIssue::class, Timer::class],
    version = 7,
    autoMigrations = [
        AutoMigration(from = 6, to = 7, spec = AutoMigrationSpecFrom6to7::class)
    ]
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

class AutoMigrationSpecFrom6to7 : AutoMigrationSpec, KoinComponent {
    override fun onPostMigrate(connection: SQLiteConnection) {
        // For each entry in table 'jira_issue', take a 'key' field and set the field 'browse_url' to the string "https://example.com/browse/$key"
        connection.execSQL(
            "UPDATE jira_issue SET browse_url = 'https://example.com/browse/' || key"
        )
    }
}
