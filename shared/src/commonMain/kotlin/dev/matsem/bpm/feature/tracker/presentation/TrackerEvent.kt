package dev.matsem.bpm.feature.tracker.presentation

import dev.matsem.bpm.data.repo.model.Timer

sealed interface TrackerEvent {
    data class OpenCommitDialog(val timer: Timer) : TrackerEvent
}