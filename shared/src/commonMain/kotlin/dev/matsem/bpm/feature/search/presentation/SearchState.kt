package dev.matsem.bpm.feature.search.presentation

import dev.matsem.bpm.data.model.domain.Issue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SearchState(
    val input: String = "",
    val results: ImmutableList<Issue> = persistentListOf()
)