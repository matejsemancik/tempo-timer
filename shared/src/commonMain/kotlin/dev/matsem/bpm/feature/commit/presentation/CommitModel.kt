package dev.matsem.bpm.feature.commit.presentation

import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.arch.BaseModel

internal class CommitModel(
    private val args: CommitArgs,
) : BaseModel<CommitState, CommitEvent>(CommitState(args.timer)), CommitScreen {
    
    override val actions: CommitActions = object : CommitActions {
        override fun onDescriptionInput(input: TextFieldValue) = updateState {
            it.copy(description = input)
        }

        override fun onDurationInput(input: TextFieldValue) = updateState {
            it.copy(duration = input)
        }
    }
}