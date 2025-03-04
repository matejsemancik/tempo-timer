package dev.matsem.bpm.data.operation

import java.util.EmptyStackException
import java.util.Stack

/**
 * Interface for a stack of undoable operations.
 * Implements the command pattern to allow for undoing operations.
 */
interface UndoStack {
    /**
     * Pushes an undoable operation onto the stack.
     *
     * @param undoable The undoable operation to add to the stack.
     */
    fun push(undoable: Undoable<*>)

    /**
     * Pops the most recent undoable operation from the stack and performs the undo operation.
     */
    suspend fun popUndo()
}

/**
 * Implementation of [UndoStack] using a standard Java [Stack].
 *
 * @property stack The underlying stack storing the undoable operations.
 */
internal class UndoStackImpl() : UndoStack {

    private val stack: Stack<Undoable<*>> = Stack()

    /**
     * Pushes an undoable operation onto the stack.
     *
     * @param undoable The undoable operation to add to the stack.
     */
    override fun push(undoable: Undoable<*>) {
        stack.push(undoable)
    }

    /**
     * Pops the most recent undoable operation from the stack and performs the undo operation.
     * Silently returns if the stack is empty.
     *
     * @throws Throwable Any exception thrown during undo operation except [EmptyStackException]
     */
    override suspend fun popUndo() {
        try {
            stack.pop().undo()
        } catch (_: EmptyStackException) {
            return
        } catch (other: Throwable) {
            throw other
        }
    }
}
