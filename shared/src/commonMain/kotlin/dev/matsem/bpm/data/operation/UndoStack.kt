package dev.matsem.bpm.data.operation

import java.util.EmptyStackException
import java.util.Stack

/**
 * Functional interface representing an undoable operation.
 */
fun interface Undoable {
    /**
     * Performs the undo operation.
     */
    suspend fun undo()
}

/**
 * Interface representing a stack of undoable operations.
 */
interface UndoStack {

    /**
     * Executes an operation and stores its undo action on the stack.
     *
     * @param T The non-null type of the result of the operation.
     * @param operation The function to execute as the operation.
     * @param undo The function to execute to undo the operation.
     * @return The result of the operation.
     */
    suspend fun <T : Any> invoke(
        operation: suspend () -> T,
        undo: suspend () -> Unit,
    ): T

    /**
     * Pops the most recent undoable operation from the stack and performs the undo operation.
     */
    suspend fun undo()
}

/**
 * Implementation of the UndoStack interface using a [Stack].
 */
internal class UndoStackImpl() : UndoStack {

    private val stack: Stack<Undoable> = Stack()

    /**
     * Executes an operation and stores its undo action on the stack.
     *
     * @param T The non-null type of the result of the operation.
     * @param operation The function to execute as the operation.
     * @param undo The function to execute to undo the operation.
     * @return The result of the operation.
     */
    override suspend fun <T : Any> invoke(operation: suspend () -> T, undo: suspend () -> Unit): T {
        val result = operation()
        stack.push(Undoable(undo))
        return result
    }

    /**
     * Pops the most recent undoable operation from the stack and performs the undo operation.
     * Silently returns if the stack is empty.
     *
     * @throws Throwable Any exception thrown during undo operation except EmptyStackException.
     */
    override suspend fun undo() {
        try {
            stack.pop().undo()
        } catch (_: EmptyStackException) {
            return
        } catch (other: Throwable) {
            throw other
        }
    }
}
