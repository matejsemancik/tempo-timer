package dev.matsem.bpm.data.service.tempo

import dev.matsem.bpm.data.service.tempo.model.Worklog
import dev.matsem.bpm.injection.scope.SessionScope
import kotlinx.datetime.LocalDate

interface TempoApiManager {

    suspend fun getAllWorklogs(
        jiraAccountId: String,
        from: LocalDate,
        to: LocalDate,
    ): List<Worklog>
}

internal class TempoApiManagerImpl(
    private val sessionScope: SessionScope
) : TempoApiManager {

    companion object {
        private const val DefaultLimit = 50
    }

    private suspend fun <T : Any> sessionScoped(block: suspend (api: TempoApi) -> T): T {
        return block(sessionScope.getTempoApi())
    }

    override suspend fun getAllWorklogs(jiraAccountId: String, from: LocalDate, to: LocalDate): List<Worklog> = sessionScoped { api ->
        TODO()
    }
}