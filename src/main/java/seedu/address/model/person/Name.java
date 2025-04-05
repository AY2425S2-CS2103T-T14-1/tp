package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final int MAX_LENGTH = 150;
    public static final String MESSAGE_CONSTRAINTS_LENGTH = "Names can be at most " + MAX_LENGTH
            + " characters long";
    public static final String MESSAGE_CONSTRAINTS_CHARACTERS =
            "Names should not have consecutive special characters, end with a special character "
                    + "and must start with a letter";
    public static final String MESSAGE_CONSTRAINTS_START_END = "Names cannot start or end with a special character.";
    public static final String MESSAGE_CONSTRAINTS_LETTER_START = "Names must start with a letter.";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX =
            "^[\\p{L}][\\p{L}0-9 ]*(?:[@.,!'/\\\\-][\\p{L}0-9 ]+)*$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        isValidName(name);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        if (test.isBlank()) {
            throw new IllegalArgumentException("Names cannot be empty, or only contain spaces or tabs.");
        }

        if (test.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_LENGTH);
        }

        if (startsOrEndsWithSpecialCharacter(test)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_START_END);
        }
        if (!Character.isLetter(test.charAt(0))) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_LETTER_START);
        }
        if (!test.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS_CHARACTERS);
        }
        return true;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equalsIgnoreCase(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    private static boolean startsOrEndsWithSpecialCharacter(String test) {
        // Check if the string is empty or null
        if (test == null || test.isEmpty()) {
            return false; // A blank string doesn't start or end with a "special character".
        }

        // Check the first and last characters
        char firstChar = test.charAt(0);
        char lastChar = test.charAt(test.length() - 1);

        // If the first or last characters are NOT alphanumeric, then it's a special character
        return !isAlphanumeric(firstChar) || !isAlphanumeric(lastChar);
    }

    /**
     * Returns true if the character is alphanumeric.
     *
     * @param ch The character to check.
     * @return true if the character is a letter or digit.
     */
    private static boolean isAlphanumeric(char ch) {
        return Character.isLetterOrDigit(ch); // Uses Java's built-in method for checking letters and digits
    }


}
