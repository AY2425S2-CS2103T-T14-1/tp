package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.ImagePath;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nickname;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;


/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String INPUT_VALIDATION_REGEX =
            "^[\\p{L}][\\p{L}0-9 ]*(?:[@.,'\\-][\\p{L}0-9 ]+|\\\\/[\\p{L}0-9 ]*)*$";
    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String input = name.trim();
        String formattedName = formatName(input);
        formattedName = escapeRemover(formattedName);
        try {
            Name.isValidName(formattedName);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return new Name(formattedName);
    }

    /**
     * Formats a name to have the first letter of each word in uppercase and the rest in lowercase.
     * Removes any extra spaces between words.
     *
     * @param name The name to be formatted.
     * @return The formatted name.
     */
    public static String formatName(String name) {
        name = name.trim().replaceAll("\\s+", " ");
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty() && Character.isLetter(word.charAt(0)) && !word.contains("/")) {
                formattedName.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    formattedName.append(word.substring(1));
                }
                formattedName.append(" ");
            } else {
                formattedName.append(word).append(" ");
            }
        }
        return formattedName.toString().trim();
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws ParseException {
        if (phone.isEmpty()) {
            return Optional.empty();
        }
        String trimmedPhone = phone.get().trim();
        try {
            trimmedPhone = slashEscapeRemover(trimmedPhone);
            Phone.isValidPhone(trimmedPhone);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return Optional.of(new Phone(trimmedPhone));
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Optional<Address> parseAddress(Optional<String> address) throws ParseException {
        if (address.isEmpty()) {
            return Optional.empty();
        }
        String trimmedAddress = address.get().trim();
        try {
            Address.isValidAddress(trimmedAddress);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return Optional.of(new Address(trimmedAddress));
    }

    /**
     * Parses a {@code String birthday} into a {@code Birthday}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param birthday The input string representing the birthday.
     *                 It should be in the format DD/MM/YYYY and must be a valid date that is not in the future.
     * @return A {@code Birthday} object if the input string is valid.
     * @throws ParseException If the given {@code birthday} is invalid.
     */
    public static Optional<Birthday> parseBirthday(Optional<String> birthday) throws ParseException {
        if (birthday.isEmpty()) {
            return Optional.empty();
        }
        String trimmedBirthday = birthday.get().trim();
        try {
            Birthday.isValidBirthday(trimmedBirthday);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return Optional.of(new Birthday(trimmedBirthday));
    }

    /**
     * Parses a {@code String nickname} into a {@code Nickname}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param nickname The input string representing the nickname.
     * @return A {@code Nickname} object if the input string is valid.
     * @throws ParseException If the given {@code nickname} is invalid.
     */
    public static Optional<Nickname> parseNickname(Optional<String> nickname) throws ParseException {
        if (nickname.isEmpty()) {
            return Optional.empty();
        }
        String trimmedNickname = nickname.get().trim();
        try {
            trimmedNickname = slashEscapeRemover(trimmedNickname);
            Nickname.isValidNickname(trimmedNickname);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return Optional.of(new Nickname(trimmedNickname));
    }

    /**
     * Parses a {@code String notes} into a {@code Notes}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param notes The input string representing the notes.
     * @return A {@code Notes} object if the input string is valid.
     * @throws ParseException If the given {@code notes} is invalid.
     */
    public static Optional<Notes> parseNotes(Optional<String> notes) throws ParseException {
        if (notes.isEmpty()) {
            return Optional.empty();
        }
        String trimmedNotes = notes.get().trim();
        try {
            trimmedNotes = slashEscapeRemover(trimmedNotes);
            Notes.isValidNotes(trimmedNotes);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return Optional.of(new Notes(trimmedNotes));
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Optional<Email> parseEmail(Optional<String> email) throws ParseException {
        if (email.isEmpty()) {
            return Optional.empty();
        }
        String trimmedEmail = email.get().trim();
        try {
            Email.isValidEmail(trimmedEmail);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return Optional.of(new Email(trimmedEmail));
    }

    /**
     * Parses a {@code String relationship} into a {@code Relationship}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code relationship} is invalid.
     */
    public static Optional<Relationship> parseRelationship(Optional<String> relationship) throws ParseException {
        if (relationship.isEmpty()) {
            return Optional.empty();
        }
        String trimmedRelationship = relationship.get().trim();
        try {
            Relationship.isValidRelationship(trimmedRelationship);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return Optional.of(new Relationship(trimmedRelationship));
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        String trimmedTag = tag.trim();
        try {
            Tag.isValidTagName(trimmedTag);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     * Limits the number of tags to MAX_TAGS_PER_PERSON.
     *
     * @throws ParseException if there are more than MAX_TAGS_PER_PERSON tags or if any tag is invalid.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);

        if (tags.size() > Tag.MAX_NUM) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS_NUM);
        }

        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String imagePath} into an {@code ImagePath}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param imagePath
     * @return An {@code ImagePath} object if the input string is valid.
     * @throws ParseException
     */
    public static Optional<ImagePath> parseImagePath(Optional<String> imagePath) throws ParseException {
        if (imagePath.isEmpty()) {
            return Optional.empty();
        }

        String trimmed = imagePath.get().trim();
        if (trimmed.isEmpty()) {
            throw new ParseException("Image path cannot be empty if provided.");
        }

        if (!trimmed.toLowerCase().endsWith(".png")) {
            throw new ParseException("Invalid image path: must be a readable .png file "
                    + "(Only PNG images are supported for optimal performance) — '" + trimmed + "'");
        }

        // Handle file:/... resource URLs (e.g. from getDefault())
        if (trimmed.startsWith("file:")) {
            try {
                File file = new File(new URI(trimmed));
                if (!(file.exists() && file.isFile() && file.canRead())) {
                    throw new ParseException("Invalid image path: must be a readable .png file "
                            + "(Only PNG images are supported for optimal performance) — '" + trimmed + "'");
                }
            } catch (Exception e) {
                throw new ParseException("Invalid image path: malformed file URL — '" + trimmed + "'");
            }
        } else {
            // Normal absolute/relative file path
            File file = new File(trimmed);
            if (!(file.exists() && file.isFile() && file.canRead())) {
                throw new ParseException("Invalid image path: must be a readable .png file "
                        + "(Only PNG images are supported for optimal performance) — '" + trimmed + "'");
            }
        }

        return Optional.of(new ImagePath(trimmed));
    }


    /**
     * Removes escape characters from the input string.
     *
     * @param input The input string.
     * @return The input string with escape characters removed.
     */
    public static String escapeRemover(String input) {
        return input.replace("\\", "");
    }

    /**
     * Removes escape characters only when they precede a forward slash.
     *
     * @param input The input string.
     * @return The input string with escape characters removed before forward slashes.
     */
    public static String slashEscapeRemover(String input) {
        return input.replace("\\/", "/");
    }
}
