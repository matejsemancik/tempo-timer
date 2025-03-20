package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.repo.model.AppThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferenceRepo {
    suspend fun saveAppThemeMode(mode: AppThemeMode)
    fun observeAppThemeMode(): Flow<AppThemeMode>
}

internal class PreferenceRepoImpl(
    private val applicationPersistence: ApplicationPersistence,
) : PreferenceRepo {

    override suspend fun saveAppThemeMode(mode: AppThemeMode) = when (mode) {
        AppThemeMode.SYSTEM -> applicationPersistence.clearDarkMode()
        AppThemeMode.DARK -> applicationPersistence.saveDarkMode(true)
        AppThemeMode.LIGHT -> applicationPersistence.saveDarkMode(false)
    }

    override fun observeAppThemeMode(): Flow<AppThemeMode> = applicationPersistence.observeDarkMode().map { isDark ->
        when (isDark) {
            null -> AppThemeMode.SYSTEM
            true -> AppThemeMode.DARK
            false -> AppThemeMode.LIGHT
        }
    }
}
