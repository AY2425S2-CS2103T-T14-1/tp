package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class RedoCommandParserTest {
    private final RedoCommandParser parser = new RedoCommandParser();

    @Test
    public void parse_validInput_returnsRedoCommand() throws Exception {
        assertEquals(new RedoCommand(), parser.parse(""));
    }

    @Test
    public void parse_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("random text"));
        assertThrows(ParseException.class, () -> parser.parse("123"));
    }
}
