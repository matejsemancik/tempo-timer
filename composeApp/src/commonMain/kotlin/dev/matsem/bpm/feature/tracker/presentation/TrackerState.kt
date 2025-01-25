package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.feature.tracker.model.Issue
import dev.matsem.bpm.feature.tracker.model.Timer
import dev.matsem.bpm.feature.tracker.model.TimerMocks
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class TrackerState(
    val favouriteIssues: PersistentList<Issue> = TimerMocks.mapNotNull { it.issue }.toPersistentList(),
    val timers: PersistentList<Timer> = persistentListOf()
)