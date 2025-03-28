package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Computes a refined similarity index between two strings.
     * Combines multiple factors for more accuracy.
     *
     * @param a First string
     * @param b Second string
     * @return Similarity index between 0.0 (completely dissimilar) and 1.0 (identical)
     */
    public static double calculateSimilarity(String a, String b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) {
            return 0.0;
        }

        // 1. Levenshtein Distance Contribution
        int distance = levenshteinDistance(a, b);
        double maxLength = Math.max(a.length(), b.length());
        double editDistanceScore = 1 - (double) distance / maxLength; // Normalized to [0, 1]

        // 2. Overlap Score (based on common substrings)
        double overlapScore = computeOverlapScore(a, b);

        // 3. Length Ratio Score (penalizes large length differences)
        double lengthRatioScore = computeLengthRatioScore(a, b);

        // Combine the scores
        double similarityIndex = (0.6 * editDistanceScore) + (0.3 * overlapScore) + (0.1 * lengthRatioScore);

        return similarityIndex;
    }

    /**
     * Computes the Levenshtein distance (edit distance) between two strings.
     */
    private static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    /**
     * Compute substring overlap score, measuring the proportion of characters in common.
     */
    private static double computeOverlapScore(String a, String b) {
        Set<Character> setA = new HashSet<>();
        for (char c : a.toCharArray()) {
            setA.add(c);
        }

        Set<Character> setB = new HashSet<>();
        for (char c : b.toCharArray()) {
            setB.add(c);
        }

        // Calculate intersection and union of character sets
        Set<Character> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<Character> union = new HashSet<>(setA);
        union.addAll(setB);

        // Return the overlap score as intersection size divided by union size
        return (double) intersection.size() / union.size();
    }

    /**
     * Compute the length-based similarity score, penalizing large differences in length.
     */
    private static double computeLengthRatioScore(String a, String b) {
        double lengthA = a.length();
        double lengthB = b.length();
        return Math.min(lengthA, lengthB) / Math.max(lengthA, lengthB);
    }



}
