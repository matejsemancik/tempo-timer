package dev.matsem.bpm.data.operation

/**
 * Represents an undoable operation with a value of type T.
 *
 * @param T The non-null type of the value associated with this undoable operation.
 */
interface Undoable<T : Any> {
    /**
     * The value associated with this undoable operation.
     */
    val value: T

    /**
     * Performs the undo operation.
     */
    suspend fun undo()
}

/**
 * Creates an instance of an undoable operation.
 *
 * @param T The non-null type of the value.
 * @param value The value associated with the undoable operation.
 * @param undo The function to execute when undoing the operation.
 * @return An implementation of [Undoable] that will call the provided undo function.
 */
fun <T : Any> undoable(value: T, undo: suspend (T) -> Unit): Undoable<T> = object : Undoable<T> {
    override val value: T = value
    override suspend fun undo() = undo(value)

    suspend fun undo(value: T) = undo(value)
}

/**
 * Executes an operation and returns an undoable instance representing that operation.
 *
 * @param T The non-null type of the result.
 * @param invoke The function to execute as the operation.
 * @param undo The function to execute when undoing the operation.
 * @return An [Undoable] instance that contains the operation result and can undo it.
 */
suspend fun <T : Any> undoableOperation(
    invoke: suspend () -> T,
    undo: suspend (T) -> Unit,
): Undoable<T> = undoable(invoke(), undo)
