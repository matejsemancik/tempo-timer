package dev.matsem.bpm.feature.search.presentation

import dev.matsem.bpm.data.repo.model.Issue

sealed interface SearchEvent {

    data class IssueSelectedEvent(val issue: Issue) : SearchEvent
}