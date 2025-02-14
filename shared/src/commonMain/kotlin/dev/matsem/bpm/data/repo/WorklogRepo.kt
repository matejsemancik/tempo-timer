package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.database.dao.UserDao
import dev.matsem.bpm.data.service.tempo.TempoApiManager
import kotlinx.datetime.LocalDate

interface WorklogRepo {

    suspend fun printWorklogs(
        from: LocalDate,
        to: LocalDate
    )
}

internal class WorklogRepoImpl(
    private val tempoApiManager: TempoApiManager,
    private val userDao: UserDao
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
}