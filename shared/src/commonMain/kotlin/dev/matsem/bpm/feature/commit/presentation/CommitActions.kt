package dev.matsem.bpm.feature.commit.presentation

import androidx.compose.ui.text.input.TextFieldValue

interface CommitActions {

    fun onDescriptionInput(input: TextFieldValue)
    fun onDurationInput(input: TextFieldValue)

    companion object {
        fun noOp(): CommitActions = object : CommitActions {
            override fun onDescriptionInput(input: TextFieldValue) = Unit
            override fun onDurationInput(input: TextFieldValue) = Unit
        }
    }
}