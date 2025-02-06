package dev.matsem.bpm.injection.module

import dev.matsem.bpm.feature.settings.presentation.SettingsModel
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.tracker.presentation.TrackerModel
import dev.matsem.bpm.feature.tracker.presentation.TrackerScreen
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun featureModule() = module {
    singleOf(::TrackerModel) bind TrackerScreen::class
    singleOf(::SettingsModel) bind SettingsScreen::class
}