package dev.matsem.bpm.injection.module

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.matsem.bpm.data.database.AppDatabase
import dev.matsem.bpm.tool.FileProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.io.path.absolutePathString

internal actual fun platformDatabaseModule(): Module = module {
    factory<RoomDatabase.Builder<AppDatabase>> {
        val dbFile = FileProvider.getApplicationDataPath().resolve("app_database.db").absolutePathString()
        Room.databaseBuilder<AppDatabase>(
            name = dbFile
        )
    }
}