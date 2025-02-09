package dev.matsem.bpm.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.matsem.bpm.data.database.model.JiraIssue
import dev.matsem.bpm.data.database.model.Timer
import dev.matsem.bpm.data.database.model.TimerComplete
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {

    @Upsert
    suspend fun upsertIssue(issue: JiraIssue)

    @Upsert
    suspend fun upsertTimer(timer: Timer)

    @Transaction
    suspend fun addOrUpdateTimer(timer: Timer, jiraIssue: JiraIssue) {
        upsertIssue(jiraIssue)
        upsertTimer(timer)
    }

    @Query("DELETE FROM timer WHERE id = :id")
    suspend fun deleteTimer(id: Int)

    @Query("SELECT * FROM timer")
    fun getTimers(): Flow<List<TimerComplete>>

    @Query("SELECT * FROM timer WHERE id = :id")
    suspend fun getTimerById(id: Int): Timer?
}