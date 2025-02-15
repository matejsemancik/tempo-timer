package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.service.tempo.TempoApiManager
import dev.matsem.bpm.data.service.tempo.model.CreateWorklogBody
import dev.matsem.bpm.tooling.dropNanos
import kotlinx.datetime.*
import kotlin.time.Duration

interface WorklogRepo {

    suspend fun printWorklogs(
        from: LocalDate,
        to: LocalDate
    )

    suspend fun createWorklog(
        jiraIssue: Issue,
        createdAt: Instant,
        timeSpent: Duration,
        description: String?,
    )
}

internal class WorklogRepoImpl(
    private val tempoApiManager: TempoApiManager,
    private val userDao: UserDao,
) : WorklogRepo {
    override suspend fun printWorklogs(
        from: LocalDate,
        to: LocalDate
    ) {
        val user = userDao.get() ?: error("No User")
        val worklogs = tempoApiManager.getAllWorklogs(
            jiraAccountId = user.accountId,
            from = from,
            to = to
        )

        println(worklogs.joinToString(separator = "\n") { it.toString() })
    }

    override suspend fun createWorklog(jiraIssue: Issue, createdAt: Instant, timeSpent: Duration, description: String?) {
        val user = userDao.get() ?: error("No User")
        val requestBody = CreateWorklogBody(
            jiraAccountId = user.accountId,
            jiraIssueId = jiraIssue.id,
            // Current system default time zone is our best bet rn.
            startedAtDate = createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date,
            // Disabled until I find out how to properly work with time zones (no clear guidance on Tempo side)
            // startedAtTime = createdAt.toLocalDateTime(TimeZone.UTC).time.dropNanos(),
            timeSpentSeconds = timeSpent.inWholeSeconds,
            description = description
        )
        val worklog = tempoApiManager.createWorklog(requestBody)
        println("Worklog created: $worklog")
    }
}