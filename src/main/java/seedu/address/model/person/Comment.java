package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's comment in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidComment(String)}
 */
public class Comment {

    public final String value; // Has to be public to be accessed when checking
    public static final String VALIDATION_REGEX = "\\S.*";
    public static final String MESSAGE_CONSTRAINTS = "Remarks can take any values, BUT it should not be blank";

    /**
     * Constructs an {@code Comment}.
     *
     * @param note A valid comment for a person profile to be added
     */
    public Comment(String note) {
        requireNonNull(note);
        checkArgument(isValidComment(note), MESSAGE_CONSTRAINTS);
        this.value = note;
    }

    /**
     * Returns if a given string is a valid comment.
     */
    public static boolean isValidComment(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Comment)) {
            return false;
        }

        Comment otherComment = (Comment) other; // Type cast
        return this.value.equals(otherComment.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
