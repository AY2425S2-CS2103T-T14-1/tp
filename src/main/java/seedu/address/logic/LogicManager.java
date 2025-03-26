package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyCommandHistory;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;



/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private ConfirmableCommand pendingConfirmation;
    private boolean isPendingConfirmation = false;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        if (isPendingConfirmation) {
            boolean isConfirmed = addressBookParser.parseConfirmation(commandText);
            logger.info("Confirmation Status: " + ((isConfirmed) ? "Confirmed" : "Aborted"));
            CommandResult result;
            if (isConfirmed) {
                result = pendingConfirmation.executeConfirmed(model);
                logger.info("Executed confirmed command: " + pendingConfirmation);
                if (pendingConfirmation instanceof UndoableCommand) {
                    logger.info("Save UndoableCommand: " + pendingConfirmation);
                    CommandTracker.getInstance().push((UndoableCommand) pendingConfirmation);
                }
            } else {
                result = pendingConfirmation.executeAborted();
                logger.info("Aborted command: " + pendingConfirmation);
            }
            pendingConfirmation = null;
            isPendingConfirmation = false;
            return result;
        }

        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(commandText);
        logger.info("Parsed command: " + command.getClass().getSimpleName());

        commandResult = command.execute(model);
        logger.info("Executed command: " + commandResult.getFeedbackToUser());


        if (commandResult.isToBeConfirmed()) {
            pendingConfirmation = commandResult.getToBeConfirmed();
            isPendingConfirmation = true;
            logger.info("Command requires confirmation: " + pendingConfirmation);
        }

        try {
            storage.saveAddressBook(model.getAddressBook());
            model.addToCommandHistory(commandText);
            storage.saveCommandHistory(model.getCommandHistory());
            logger.info("Data saved successfully.");
        } catch (AccessDeniedException e) {
            logger.severe("Permission error saving data: " + e.getMessage());
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            logger.severe("IO error saving data: " + ioe.getMessage());
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ReadOnlyCommandHistory getCommandHistory() {
        return model.getCommandHistory();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
