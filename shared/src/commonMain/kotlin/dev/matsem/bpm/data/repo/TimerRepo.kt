package dev.matsem.bpm.data.repo

import androidx.room.PrimaryKey
import dev.matsem.bpm.data.database.dao.TimerDao
import dev.matsem.bpm.data.mapping.IssueMapping.toDbModel
import dev.matsem.bpm.data.mapping.TimerMapping.toDomainModel
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.Timer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import dev.matsem.bpm.data.database.model.Timer as Timer_Database

interface TimerRepo {

    fun getTimers(): Flow<List<Timer>>
    suspend fun createTimerForIssue(issue: Issue)
    suspend fun resumeTimer(id: Int)
    suspend fun pauseTimer(id: Int)
    suspend fun deleteTimer(id: Int)
}

internal class TimerRepoImpl(
    private val timerDao: TimerDao,
    private val clock: Clock
) : TimerRepo {

    override fun getTimers(): Flow<List<Timer>> =
        timerDao.getTimers().map { timerList -> timerList.map { dbTimber -> dbTimber.toDomainModel() } }

    override suspend fun createTimerForIssue(issue: Issue) {
        val newTimer = Timer_Database(jiraIssueId = issue.id, accumulationMs = 0L, lastStartedAt = clock.now(), createdAt = clock.now())
        timerDao.addOrUpdateTimer(newTimer, issue.toDbModel())
    }

    override suspend fun resumeTimer(id: Int) {
        val timer = timerDao.getTimerById(id) ?: return
        if (timer.lastStartedAt != null) {
            println("timer already started")
            return
        }

        val newTimer = timer.copy(lastStartedAt = clock.now())
        timerDao.upsertTimer(newTimer)
    }

    override suspend fun pauseTimer(id: Int) {
        val timer = timerDao.getTimerById(id) ?: return
        if (timer.lastStartedAt == null) {
            println("timer is not started")
            return
        }

        val durationToAdd = clock.now() - timer.lastStartedAt
        val newTimer = timer.copy(
            lastStartedAt = null,
            accumulationMs = timer.accumulationMs + durationToAdd.inWholeMilliseconds
        )

        timerDao.upsertTimer(newTimer)
    }

    override suspend fun deleteTimer(id: Int) = timerDao.deleteTimer(id)
}