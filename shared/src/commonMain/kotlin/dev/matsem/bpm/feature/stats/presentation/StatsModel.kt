package dev.matsem.bpm.feature.stats.presentation

import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.WorklogRepo
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

internal class StatsModel(
    private val worklogRepo: WorklogRepo,
) : BaseModel<StatsState, Nothing>(DefaultState), StatsWidget {

    override val actions: StatsActions = object : StatsActions {}

    companion object {
        private val DefaultState: StatsState = StatsState()
    }

    override suspend fun onStart() {
        coroutineScope.launch {
            runCatching {
                worklogRepo.getAllStats().collect { stats ->
                    updateState { it.copy(allStats = stats.toImmutableList()) }
                }
            }
        }
    }
}
