package dev.matsem.bpm.injection.module

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.matsem.bpm.data.database.AppDatabase
import dev.matsem.bpm.data.database.dao.JiraIssueDao
import dev.matsem.bpm.data.database.dao.TimerDao
import dev.matsem.bpm.data.database.dao.UserDao
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun platformDatabaseModule(): Module

internal fun databaseModule() = module {
    includes(platformDatabaseModule())
    single<AppDatabase> {
        val databaseBuilder = get<RoomDatabase.Builder<AppDatabase>>()
        databaseBuilder
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }
    single<JiraIssueDao> { get<AppDatabase>().jiraIssueDao() }
    single<TimerDao> { get<AppDatabase>().timerDao() }
}