package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandTracker;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains unit tests for UndoCommand.
 */
public class UndoCommandTest {
    private Model model;
    private CommandTracker commandTracker;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        commandTracker = CommandTracker.getInstance();
    }

    @Test
    public void execute_validUndo_success() {
        Command mockCommand = new CommandStub();
        commandTracker.push(mockCommand);

        UndoCommand undoCommand = new UndoCommand();
        String expectedMessage = "Undo successful!";

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandSuccess(undoCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        UndoCommand undoCommand1 = new UndoCommand();
        UndoCommand undoCommand2 = new UndoCommand();

        // Same object -> returns true
        assertTrue(undoCommand1.equals(undoCommand1));

        // Different objects, but same type -> returns true
        assertTrue(undoCommand1.equals(undoCommand2));

        // Null -> returns false
        assertFalse(undoCommand1.equals(null));

        // Different type -> returns false
        assertFalse(undoCommand1.equals(new RedoCommand()));
    }

    @Test
    public void toStringMethod() {
        UndoCommand undoCommand = new UndoCommand();
        String expected = UndoCommand.class.getCanonicalName();
        assertEquals(expected, undoCommand.toString());
    }

    /**
     * A stub class for a mock command that supports undo.
     */
    private static class CommandStub extends UndoableCommand {
        @Override
        public CommandResult execute(Model model) {
            return new CommandResult("Mock command executed!");
        }

        @Override
        public void undo(Model model) { }

        @Override
        public void redo(Model model) { }
    }
}
