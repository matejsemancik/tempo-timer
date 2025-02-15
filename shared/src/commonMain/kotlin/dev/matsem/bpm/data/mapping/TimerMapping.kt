package dev.matsem.bpm.data.mapping

import dev.matsem.bpm.data.mapping.IssueMapping.toDomainModel
import dev.matsem.bpm.data.repo.model.TimerState
import kotlin.time.Duration.Companion.milliseconds
import dev.matsem.bpm.data.database.model.TimerComplete as Timer_Database
import dev.matsem.bpm.data.repo.model.Timer as Timer_Domain

internal object TimerMapping {

    fun Timer_Database.toDomainModel() = Timer_Domain(
        id = timer.id,
        issue = issue.toDomainModel(),
        createdAt = timer.createdAt,
        state = TimerState(
            finishedDuration = timer.accumulationMs.milliseconds,
            lastStartedAt = timer.lastStartedAt
        )
    )
}