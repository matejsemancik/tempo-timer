package dev.matsem.bpm.data.service.tempo

import dev.matsem.bpm.data.service.tempo.model.CreateWorklogBody
import dev.matsem.bpm.data.service.tempo.model.DaySchedule
import dev.matsem.bpm.data.service.tempo.model.TimesheetApprovalPeriod
import dev.matsem.bpm.data.service.tempo.model.Worklog
import dev.matsem.bpm.injection.scope.SessionScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.LocalDate

interface TempoApiManager {

    suspend fun getAllWorklogs(
        jiraAccountId: String,
        dateRange: ClosedRange<LocalDate>,
    ): List<Worklog>

    suspend fun createWorklog(body: CreateWorklogBody): Worklog
    suspend fun getApprovalPeriod(at: LocalDate): TimesheetApprovalPeriod?
    suspend fun getUserSchedule(dateRange: ClosedRange<LocalDate>): List<DaySchedule>
}

internal class TempoApiManagerImpl(
    private val sessionScope: SessionScope,
) : TempoApiManager {

    companion object {
        private const val DefaultLimit = 50
    }

    private suspend fun <T : Any?> sessionScoped(block: suspend (api: TempoApi) -> T): T {
        return block(sessionScope.getTempoApi())
    }

    override suspend fun getAllWorklogs(
        jiraAccountId: String,
        dateRange: ClosedRange<LocalDate>,
    ): List<Worklog> =
        sessionScoped { api ->
            flow {
                var nextUrl: String? = null
                do {
                    val response = when {
                        nextUrl == null -> api.getWorklogs(
                            jiraAccountId = jiraAccountId,
                            from = dateRange.start,
                            to = dateRange.endInclusive,
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

    override suspend fun getApprovalPeriod(at: LocalDate): TimesheetApprovalPeriod? = sessionScoped { api ->
        api.getPeriods(from = at, to = at).periods.firstOrNull()
    }

    override suspend fun getUserSchedule(dateRange: ClosedRange<LocalDate>): List<DaySchedule> = sessionScoped { api ->
        api.getUserSchedule(from = dateRange.start, to = dateRange.endInclusive).results
    }
}
