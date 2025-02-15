package dev.matsem.bpm.feature.commit.presentation

sealed interface CommitEvent {
    data object Dismiss : CommitEvent
}