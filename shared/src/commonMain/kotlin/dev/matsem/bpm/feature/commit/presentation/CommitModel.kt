package dev.matsem.bpm.feature.commit.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.TimerRepo
import dev.matsem.bpm.data.repo.WorklogRepo
import dev.matsem.bpm.feature.tracker.formatting.DurationFormatter.formatForTextInput
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal class CommitModel(
    private val args: CommitArgs,
    private val timerRepo: TimerRepo,
    private val worklogRepo: WorklogRepo,
) : BaseModel<CommitState, CommitEvent>(CommitState(args.timer)), CommitScreen {

    companion object {
        /**
         * A regular expression for parsing duration strings.
         *
         * This regex matches duration strings that may include up to three time units:
         * hours, minutes, and seconds. Each unit is represented by one or more digits
         * immediately followed by a specific unit character:
         *
         * - `h` for hours
         * - `m` for minutes
         * - `s` for seconds
         *
         * The time units must appear in the following order: **hours → minutes → seconds**.
         * Each time unit is optional, but if present, it must be in the correct order and
         * appear only once.
         *
         * The regex also ensures that the input string contains at least one digit, thus
         * rejecting empty strings or strings that consist solely of whitespace.
         *
         *  @see Regex
         */
        val DurationInputRegex = "^(?=.*\\d)(?:(\\d+)h\\s*)?(?:(\\d+)m\\s*)?(?:(\\d+)s\\s*)?$".toRegex()
    }

    override suspend fun onStart() {
        state
            .map { it.durationInput.text }
            .distinctUntilChanged()
            .onEach { durationInput ->
                updateState { state ->
                    val isInputValid = parsePositiveDuration(durationInput) != null
                    state.copy(
                        isDurationInputError = !isInputValid,
                        error = if (isInputValid) null else state.error
                    )
                }
            }
            .launchIn(coroutineScope)

        updateState { state ->
            state.copy(
                durationSuggestions = calculateSuggestionsForDuration(args.timer.state.duration)
                    .map { it.formatForTextInput() }
                    .toImmutableList()
            )
        }
    }

    override val actions: CommitActions = object : CommitActions {
        override fun onDescriptionInput(input: TextFieldValue) = updateState {
            it.copy(descriptionInput = input)
        }

        override fun onDurationInput(input: TextFieldValue) = updateState {
            it.copy(durationInput = input)
        }

        override fun onSuggestionClick(suggestion: String) = updateState {
            it.copy(durationInput = TextFieldValue(suggestion, TextRange(suggestion.length)))
        }

        override fun onDeleteClick() {
            coroutineScope.launch {
                timerRepo.deleteTimer(args.timer)
                sendEvent(CommitEvent.Dismiss)
            }
        }

        override fun onCommitClick() {
            val duration = parsePositiveDuration(state.value.durationInput.text)
            if (duration == null) {
                updateState { state ->
                    state.copy(error = "Invalid duration input")
                }
                return
            }

            coroutineScope.launch {
                updateState { state ->
                    state.copy(isButtonLoading = true)
                }

                runCatching {
                    worklogRepo.createWorklog(
                        jiraIssue = args.timer.issue,
                        createdAt = args.timer.createdAt,
                        timeSpent = duration,
                        description = state.value.descriptionInput.text.takeIf { it.isNotBlank() }
                    )
                    timerRepo.deleteTimer(args.timer)
                }.fold(
                    onSuccess = {
                        sendEvent(CommitEvent.Dismiss)
                    },
                    onFailure = { error ->
                        println(error)
                        updateState { state ->
                            state.copy(isButtonLoading = false, error = error.message)
                        }
                    }
                )
            }
        }
    }

    private fun parsePositiveDuration(input: String): Duration? {
        val matchResult = DurationInputRegex.find(input) ?: return null
        val hours = matchResult.groupValues[1].toIntOrNull() ?: 0
        val minutes = matchResult.groupValues[2].toIntOrNull() ?: 0
        val seconds = matchResult.groupValues[3].toIntOrNull() ?: 0

        val duration = hours.hours + minutes.minutes + seconds.seconds
        println("${matchResult.groupValues}: $duration")
        if (duration.isPositive().not()) {
            return null
        }
        return duration
    }

    private fun calculateSuggestionsForDuration(duration: Duration): List<Duration> {
        return listOf(
            duration.roundToNearest(5.minutes),
            duration.roundToNearest(10.minutes),
            duration.roundToNearest(15.minutes),
        ).filter { it.isPositive() }.distinct()
    }

    private fun Duration.roundToNearest(interval: Duration): Duration {
        val intervalNanos = interval.inWholeSeconds
        val factor = round(this.inWholeSeconds.toDouble() / intervalNanos)
        return (factor * intervalNanos).seconds
    }
}
