package dev.matsem.bpm.feature.stats.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.WorklogRepo
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

internal class StatsModel(
    private val worklogRepo: WorklogRepo,
) : BaseModel<StatsState, Nothing>(DefaultState), StatsWidget {

    override val actions: StatsActions = object : StatsActions {
        override fun onViewResumed() {
            syncWorkStats()
        }

        override fun onClick() {
            updateState { state ->
                val currentIndex = state.selectedIndex ?: return@updateState state
                state.copy(selectedIndex = ((currentIndex + 1) % state.allWorkStats.count()))
            }
        }
    }

    companion object {
        private val DefaultState: StatsState = StatsState()
    }

    override suspend fun onStart() {
        syncWorkStats()
        coroutineScope.launch {
            worklogRepo.getWorkStats().collect { workStats ->
                println("stats: $workStats")
                updateState { state ->
                    state.copy(
                        allWorkStats = workStats.toImmutableList(),
                        selectedIndex = state.selectedIndex?.coerceIn(0, workStats.count() - 1) ?: 0
                    )
                }
            }
        }
    }

    private fun syncWorkStats() = coroutineScope.launch { runCatching { worklogRepo.syncWorkStats() } }
}
