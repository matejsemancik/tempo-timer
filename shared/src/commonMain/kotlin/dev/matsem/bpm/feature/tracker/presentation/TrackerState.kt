package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.data.model.domain.Issue
import dev.matsem.bpm.data.model.domain.Timer
import dev.matsem.bpm.data.model.domain.TimerMocks
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class TrackerState(
    val favouriteIssues: PersistentList<Issue> = TimerMocks.mapNotNull { it.issue }.toPersistentList(),
    val timers: PersistentList<Timer> = persistentListOf()
)