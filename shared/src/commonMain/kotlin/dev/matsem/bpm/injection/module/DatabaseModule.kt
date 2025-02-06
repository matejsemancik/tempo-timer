package dev.matsem.bpm.injection.module

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.matsem.bpm.data.database.AppDatabase
import org.koin.dsl.module

fun databaseModule() = module {
//    single<AppDatabase> {
//        Room.databaseBuilder<AppDatabase>(
//            name = "files/database.db",
//            factory = {
//                AppDatabase::class.instantiateImpl()
//            }
//        ).setDriver(
//            BundledSQLiteDriver()
//        ).build()
//    }
}