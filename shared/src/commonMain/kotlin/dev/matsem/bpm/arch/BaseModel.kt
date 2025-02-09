package dev.matsem.bpm.arch

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * A base presentation component for managing and encapsulating state of application components with reactive state updates.
 *
 * @param S The type of the state managed by this model.
 * @param defaultState The initial state to be held by this model.
 *
 * The `BaseModel` provides a structure for managing state in a reactive programming model. It leverages Kotlin's
 * `StateFlow` to expose the current state and ensure it can be observed reactively.
 *
 * @property coroutineScope The `CoroutineScope` used for managing coroutines associated with this model. It uses the
 * global `MainScope` as the scope for coroutine operations.
 *
 * @property state A `StateFlow` representing the current state of the model. Observers can subscribe to this flow to
 * receive updates whenever the state changes. It initializes with the `defaultState` and starts observing the flow
 * when the model is instantiated.
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
 */
abstract class BaseModel<S : Any>(defaultState: S) {

    val coroutineScope = MainScope()
    private val _state = MutableStateFlow(defaultState)

    val state: StateFlow<S> = _state
        .onStart { onStart() }
        .stateIn(coroutineScope, SharingStarted.Lazily, defaultState)

    protected open suspend fun onStart() = Unit

    protected fun updateState(mapper: (S) -> S) = _state.update(mapper)
}