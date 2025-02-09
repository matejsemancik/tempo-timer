package dev.matsem.bpm.arch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

/**
 * A composable function that observes a stream of events from a [Flow] and triggers an action
 * for each emitted event. This is useful for reacting to one-time events such as navigation
 * commands or showing messages.
 *
 * @param E The type of the events emitted by the [Flow].
 * @param eventsFlow The [Flow] emitting events to be observed.
 * @param observer A suspend lambda function that handles each event received from the [eventsFlow].
 */
@Composable
inline fun <reified E : Any> EventEffect(
    eventsFlow: Flow<E>,
    crossinline observer: suspend (E) -> Unit,
) {
    LaunchedEffect(eventsFlow) {
        eventsFlow.collect {
            observer(it)
        }
    }
}