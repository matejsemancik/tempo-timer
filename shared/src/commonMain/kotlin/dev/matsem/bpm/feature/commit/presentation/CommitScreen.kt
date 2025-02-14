package dev.matsem.bpm.feature.commit.presentation

import kotlinx.coroutines.flow.StateFlow

interface CommitScreen {

    val state: StateFlow<CommitState>
    val actions: CommitActions
}

