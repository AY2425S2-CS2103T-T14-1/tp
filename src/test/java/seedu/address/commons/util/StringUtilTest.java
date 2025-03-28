package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: empty strings
        assertFalse(StringUtil.isNonZeroUnsignedInteger("")); // Boundary value
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: not a number
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // EP: zero
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: zero as prefix
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: signed numbers
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: numbers with white space
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 ")); // Leading/trailing spaces
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0")); // Spaces in the middle

        // EP: number larger than Integer.MAX_VALUE
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // EP: valid numbers, should return true
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1")); // Boundary value
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }


    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase("typical sentence", null));
    }

    @Test
    public void containsWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // Matches a partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb")); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb")); // First word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa")); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces

        // Matches multiple words in sentence
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
            .contains("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }

    @Test
    public void calculateSimilarity_similarStrings_returnsTrue() {
        String[] x = getSimilarStringsX();
        String[] y = getSimilarStringsY();
        for (int i = 0; i < x.length; i++) {
            double similarity = StringUtil.calculateSimilarity(x[i], y[i]);
            if (similarity < 0.6) {
                assertTrue(false, "Test failed! Similarity: " + similarity);
            }
        }
        assertTrue(true);
    }

    @Test
    public void calculateSimilarity_differentStrings_returnsFalse() {
        String[] x = getDifferentStringsX();
        String[] y = getDifferentStringsY();
        for (int i = 0; i < x.length; i++) {
            double similarity = StringUtil.calculateSimilarity(x[i], y[i]);
            if (similarity >= 0.6) {
                assertTrue(false, "Test failed! Too high similarity, similarity: " + similarity);
            }
        }
        assertTrue(true);
    }

    public static String[] getSimilarStringsX() {
        return new String[]{
            "apple",
            "Mickie",
            "teh",
            "accomodate",
            "recieve",
            "wierd",
            "separate",
            "occurrence",
            "maintenance",
            "liaison",
            "embarrass",
            "conscience",
            "indispensable",
            "millenium",
            "supercede",
            "definitely",
            "argument",
            "calendar",
            "refered",
            "occurrance"
        };
    }

    public static String[] getSimilarStringsY() {
        return new String[]{
            "apply",
            "Micky",
            "the",
            "accommodate",
            "receive",
            "weird",
            "seperate",
            "occurence",
            "maintainance",
            "liason",
            "embarass",
            "concience",
            "indespensible",
            "millennium",
            "supersede",
            "definately",
            "arguement",
            "calender",
            "referred",
            "occurrence"
        };
    }

    public static String[] getDifferentStringsX() {
        return new String[]{
            "apple",
            "house",
            "computer",
            "ocean",
            "mountain",
            "elephant",
            "jazz",
            "bicycle",
            "rainbow",
            "galaxy"
        };
    }

    public static String[] getDifferentStringsY() {
        return new String[]{
            "zebra",
            "river",
            "telephone",
            "desert",
            "valley",
            "mosquito",
            "rock",
            "motorcycle",
            "thunder",
            "nebula"
        };
    }


}
