package dev.matsem.bpm.data.repo.model

import kotlin.random.Random

data class SearchResult(
    val issue: Issue,
    val isFavourite: Boolean,
)

val MockSearchResults = MockIssues.map { SearchResult(it, isFavourite = Random.nextBoolean()) }