package dev.matsem.bpm.feature.commit.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.Timer
import dev.matsem.bpm.feature.tracker.formatting.DurationFormatter.formatForTextInput

data class CommitState(
    private val timer: Timer,
    val descriptionInput: TextFieldValue = TextFieldValue(),
    val durationInput: TextFieldValue = run {
        val text = timer.state.duration.formatForTextInput()
        TextFieldValue(text, TextRange(0, text.length))
    },
    val isDurationInputError: Boolean = false,
) {
    val issue: Issue
        get() = timer.issue
    val descriptionPlaceholder: String
        get() = "Working on issue ${issue.key}"
}