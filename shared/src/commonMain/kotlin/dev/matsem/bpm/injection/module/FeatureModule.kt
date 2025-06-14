package dev.matsem.bpm.injection.module

import dev.matsem.bpm.feature.app.presentation.AppWindow
import dev.matsem.bpm.feature.app.presentation.AppWindowModel
import dev.matsem.bpm.feature.commit.presentation.CommitModel
import dev.matsem.bpm.feature.commit.presentation.CommitScreen
import dev.matsem.bpm.feature.logbook.presentation.LogbookModel
import dev.matsem.bpm.feature.logbook.presentation.LogbookScreen
import dev.matsem.bpm.feature.search.presentation.SearchModel
import dev.matsem.bpm.feature.search.presentation.SearchScreen
import dev.matsem.bpm.feature.settings.presentation.SettingsModel
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.stats.presentation.StatsModel
import dev.matsem.bpm.feature.stats.presentation.StatsWidget
import dev.matsem.bpm.feature.tracker.presentation.TrackerModel
import dev.matsem.bpm.feature.tracker.presentation.TrackerScreen
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun featureModule() = module {
    singleOf(::AppWindowModel) bind AppWindow::class
    singleOf(::TrackerModel) bind TrackerScreen::class
    singleOf(::LogbookModel) bind LogbookScreen::class
    singleOf(::SettingsModel) bind SettingsScreen::class
    singleOf(::SearchModel) bind SearchScreen::class
    singleOf(::StatsModel) bind StatsWidget::class
    factoryOf(::CommitModel) bind CommitScreen::class
}
