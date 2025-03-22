package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Represents a command that supports undo and redo operations.
 */
public abstract class UndoableCommand extends Command {
    /**
     * Undoes the last command executed.
     */
    public abstract void undo(Model model);

    /**
     * Redoes the last undone command.
     */
    public abstract void redo(Model model);
}
