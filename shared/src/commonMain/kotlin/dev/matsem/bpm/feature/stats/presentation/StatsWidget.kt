package dev.matsem.bpm.feature.stats.presentation

import kotlinx.coroutines.flow.StateFlow

interface StatsWidget {
    val actions: StatsActions
    val state: StateFlow<StatsState>
}
