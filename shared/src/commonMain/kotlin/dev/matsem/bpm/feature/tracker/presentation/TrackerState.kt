package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.Timer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class TrackerState(
    val favouriteIssues: PersistentList<Issue> = persistentListOf(),
    val timers: PersistentList<Timer> = persistentListOf()
)