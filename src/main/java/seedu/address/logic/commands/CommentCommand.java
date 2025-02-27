package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Comment;

/**
 * Edits the comment of the person identified by the index number
 */
public class CommentCommand extends Command {

    public static final String MESSAGE_ADD_COMMENT_SUCCESS = "Added comment to Person: %1$s";
    public static final String MESSAGE_DELETE_COMMENT_SUCCESS = "Removed comment from Person: %1$s";

    private final Index index;
    private final Comment comment;

    public CommentCommand(Index index, Comment comment) {
        requireAllNonNull(index, comment);
        this.index = index;
        this.comment = comment;
    }

    public static final String COMMAND_WORD = "comment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the comments of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing comment will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "r/ [COMMENT]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "r/ Likes to play basketball.";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "Comment command not implemented yet";

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommentCommand)) {
            return false;
        }

        CommentCommand otherCommentCommand = (CommentCommand) other;
        return index.equals(otherCommentCommand.index)
                && comment.equals(otherCommentCommand.comment);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), comment);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    private String generateSuccessMessage(Person person) {
        String message = !comment.value.isEmpty() ? MESSAGE_ADD_COMMENT_SUCCESS : MESSAGE_DELETE_COMMENT_SUCCESS;
        return String.format(message, Messages.format(person));
    }

}
