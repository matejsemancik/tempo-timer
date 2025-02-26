package dev.matsem.bpm.arch

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A base presentation component for managing and encapsulating state of application components with reactive state updates.
 *
 * @param S The type of the state managed by this model.
 * @param E The type of events emitted by this model.
 * @param defaultState The initial state to be held by this model.
 *
 * @property coroutineScope The `CoroutineScope` used for managing coroutines associated with this model. It uses the
 * global `MainScope` as the scope for coroutine operations.
 *
 * @property state A `StateFlow` representing the current state of the model. Observers can subscribe to this flow to
 * receive updates whenever the state changes. It initializes with the `defaultState` and starts observing the flow
 * when the model is instantiated.
 *
 * @property events A `Flow` instance that represents a stream of events emitted by the model. Events are sent via
 * a buffered `Channel`, allowing consumers to subscribe and handle incoming events.
 *
 * The state flow triggers the [onStart] function when it starts collecting, which can be overridden to perform
 * specific actions during initialization.
 *
 * @see MutableStateFlow
 *
 * Protected Functions:
 * - [onStart]: Invoked when the `state` flow starts collecting. Override this function to define custom operations
 *   that should happen during initialization. The default implementation is a no-op.
 *
 * - [updateState]: Updates the current state by applying the given `mapper` function. The `mapper` receives the
 *   current state and returns the new, updated state. This function ensures safe state updates by modifying a private
 *   [MutableStateFlow].
 *
 * - [sendEvent]: Emits a new event of type [E] to the events flow. This is intended for sending transient, one-shot
 *   events like navigation actions or UI feedback.
 */
abstract class BaseModel<S : Any, E : Any>(defaultState: S) {

    protected val coroutineScope = MainScope()

    private val _state = MutableStateFlow(defaultState)

    private val _events = Channel<E>(Channel.BUFFERED)

    val state: StateFlow<S> = _state
        .onStart { onStart() }
        .stateIn(coroutineScope, SharingStarted.Lazily, defaultState)

    val events: Flow<E> = _events
        .receiveAsFlow()
        .shareIn(coroutineScope, SharingStarted.Lazily)

    protected open suspend fun onStart() = Unit

    protected fun updateState(mapper: (S) -> S) = _state.update(mapper)

    protected fun sendEvent(event: E) {
        coroutineScope.launch { _events.send(event) }
    }
}