package dev.matsem.bpm.injection.module

import dev.matsem.bpm.feature.commit.presentation.CommitModel
import dev.matsem.bpm.feature.commit.presentation.CommitScreen
import dev.matsem.bpm.feature.search.presentation.SearchModel
import dev.matsem.bpm.feature.search.presentation.SearchScreen
import dev.matsem.bpm.feature.settings.presentation.SettingsModel
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.tracker.presentation.TrackerModel
import dev.matsem.bpm.feature.tracker.presentation.TrackerScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun featureModule() = module {
    singleOf(::TrackerModel) bind TrackerScreen::class
    singleOf(::SettingsModel) bind SettingsScreen::class
    singleOf(::SearchModel) bind SearchScreen::class
    factoryOf(::CommitModel) bind CommitScreen::class
}