package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final int MAX_LENGTH = 50;
    public static final String MESSAGE_CONSTRAINTS_LENGTH = "Phone numbers can be at most "
            + MAX_LENGTH + " characters long";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS =
            "Phone numbers should only contain printable ASCII characters and must not be blank";
    public static final String VALIDATION_REGEX = "(?!^\\s+$)[\\x20-\\x7E]+";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        isValidPhone(phone);
        value = phone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        if (test.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_LENGTH);
        }
        if (!test.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_CHARACTERS);
        }
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
