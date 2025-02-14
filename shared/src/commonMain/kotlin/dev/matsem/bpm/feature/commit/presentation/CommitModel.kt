package dev.matsem.bpm.feature.commit.presentation

import dev.matsem.bpm.arch.BaseModel

internal class CommitModel(
    private val args: CommitArgs,
) : BaseModel<CommitState, CommitEvent>(CommitState(args.timer.issue)), CommitScreen {
    
    override val actions: CommitActions = object : CommitActions {}
}