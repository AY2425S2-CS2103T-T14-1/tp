package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class UndoCommandParserTest {
    private final UndoCommandParser parser = new UndoCommandParser();

    @Test
    public void parse_validInput_returnsUndoCommand() throws Exception {
        assertEquals(new UndoCommand(), parser.parse(""));
    }

    @Test
    public void parse_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("random text"));
        assertThrows(ParseException.class, () -> parser.parse("123"));
    }
}
