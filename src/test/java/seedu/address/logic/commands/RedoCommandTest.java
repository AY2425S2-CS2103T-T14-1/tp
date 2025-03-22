package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandTracker;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains unit tests for RedoCommand.
 */
public class RedoCommandTest {
    private Model model;
    private CommandTracker commandTracker;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        commandTracker = CommandTracker.getInstance();
    }

    @Test
    public void execute_noRedoHistory_failure() {
        RedoCommand redoCommand = new RedoCommand();
        assertCommandFailure(redoCommand, model, "Nothing to redo!");
    }

    @Test
    public void execute_validRedo_success() {
        Command mockCommand = new CommandStub();
        commandTracker.push(mockCommand);
        commandTracker.popUndo();

        RedoCommand redoCommand = new RedoCommand();
        String expectedMessage = "Redo successful!";

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(redoCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        RedoCommand redoCommand1 = new RedoCommand();
        RedoCommand redoCommand2 = new RedoCommand();

        // Same object -> returns true
        assertTrue(redoCommand1.equals(redoCommand1));

        // Different objects, but same type -> returns true
        assertTrue(redoCommand1.equals(redoCommand2));

        // Null -> returns false
        assertFalse(redoCommand1.equals(null));

        // Different type -> returns false
        assertFalse(redoCommand1.equals(new UndoCommand()));
    }

    @Test
    public void toStringMethod() {
        RedoCommand redoCommand = new RedoCommand();
        String expected = RedoCommand.class.getCanonicalName();
        assertEquals(expected, redoCommand.toString());
    }

    /**
     * A stub class for a mock command that supports redo.
     */
    private static class CommandStub extends UndoableCommand {
        @Override
        public CommandResult execute(Model model) throws CommandException {
            return new CommandResult("Mock command executed!");
        }

        @Override
        public void undo(Model model) { }

        @Override
        public void redo(Model model) { }
    }
}
