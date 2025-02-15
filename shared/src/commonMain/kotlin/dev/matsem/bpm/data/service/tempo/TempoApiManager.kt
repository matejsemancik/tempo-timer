package dev.matsem.bpm.data.service.tempo

import dev.matsem.bpm.data.service.tempo.model.CreateWorklogBody
import dev.matsem.bpm.data.service.tempo.model.Worklog
import dev.matsem.bpm.injection.scope.SessionScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.LocalDate

interface TempoApiManager {

    suspend fun getAllWorklogs(
        jiraAccountId: String,
        from: LocalDate,
        to: LocalDate,
    ): List<Worklog>

    suspend fun createWorklog(body: CreateWorklogBody): Worklog
}

internal class TempoApiManagerImpl(
    private val sessionScope: SessionScope,
) : TempoApiManager {

    companion object {
        private const val DefaultLimit = 50
    }

    private suspend fun <T : Any> sessionScoped(block: suspend (api: TempoApi) -> T): T {
        return block(sessionScope.getTempoApi())
    }

    override suspend fun getAllWorklogs(jiraAccountId: String, from: LocalDate, to: LocalDate): List<Worklog> =
        sessionScoped { api ->
            flow {
                var nextUrl: String? = null
                do {
                    val response = when {
                        nextUrl == null -> api.getWorklogs(
                            jiraAccountId = jiraAccountId,
                            from = from,
                            to = to,
                            offset = 0,
                            limit = DefaultLimit
                        )

                        else -> api.getWorklogs(nextUrl)
                    }

                    nextUrl = response.metadata.next
                    emit(response)
                } while (nextUrl != null)
            }.map { it.results }.toList().flatten()
        }

    override suspend fun createWorklog(body: CreateWorklogBody) = sessionScoped { api ->
        api.createWorklog(body)
    }
}