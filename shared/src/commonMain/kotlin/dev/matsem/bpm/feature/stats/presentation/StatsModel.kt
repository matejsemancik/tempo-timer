package dev.matsem.bpm.feature.stats.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.WorklogRepo
import dev.matsem.bpm.data.repo.model.WorkStats
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

internal class StatsModel(
    private val worklogRepo: WorklogRepo,
) : BaseModel<StatsState, Nothing>(DefaultState), StatsWidget {

    override val actions: StatsActions = object : StatsActions {
        override fun onViewResumed() {
            syncWorkStats()
        }
    }

    companion object {
        private val DefaultState: StatsState = StatsState()
    }

    override suspend fun onStart() {
        syncWorkStats()
        coroutineScope.launch {
            runCatching {
                worklogRepo.getWorkStats().collect { workStats ->
                    updateState { it.copy(allWorkStats = workStats.toImmutableList()) }
                }
            }
        }
    }

    private fun syncWorkStats() = coroutineScope.launch { runCatching { worklogRepo.syncWorkStats() } }
}
