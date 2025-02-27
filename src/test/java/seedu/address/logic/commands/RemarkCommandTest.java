package seedu.address.logic.commands;

import static seedu.address.logic.Messages.MESSAGE_REMARK_NOT_IMPLEMENTED;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute() {
        assertCommandFailure(new RemarkCommand(), model, MESSAGE_REMARK_NOT_IMPLEMENTED);
    }


}
