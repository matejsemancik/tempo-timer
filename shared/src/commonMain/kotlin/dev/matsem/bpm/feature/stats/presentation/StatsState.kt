package dev.matsem.bpm.feature.stats.presentation

import dev.matsem.bpm.data.repo.model.WorkStats
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StatsState(
    internal val allWorkStats: ImmutableList<WorkStats> = persistentListOf(),
    internal val selectedIndex: Int? = null,
) {
    val displayedStats: WorkStats?
        get() = selectedIndex?.let { allWorkStats.getOrNull(selectedIndex) }
}
