package dev.matsem.bpm.feature.commit.presentation

import dev.matsem.bpm.arch.BaseModel

internal class CommitModel : BaseModel<CommitState, CommitEvent>(DefaultState), CommitScreen {

    companion object {
        val DefaultState = CommitState()
    }

    override val actions: CommitActions = object : CommitActions {}
}