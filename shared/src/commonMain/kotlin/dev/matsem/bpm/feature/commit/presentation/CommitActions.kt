package dev.matsem.bpm.feature.commit.presentation

interface CommitActions {

    companion object {
        fun noOp(): CommitActions = object : CommitActions {}
    }
}