package dev.matsem.bpm.feature.commit.presentation

import dev.matsem.bpm.data.repo.model.Issue

data class CommitState(
    val issue: Issue
)