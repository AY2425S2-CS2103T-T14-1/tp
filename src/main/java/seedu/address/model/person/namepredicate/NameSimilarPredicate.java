package seedu.address.model.person.namepredicate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * A predicate that evaluates whether a given person's name is similar to any of the specified keywords.
 * Similarity is determined based on a similarity threshold using the Levenshtein distance algorithm.
 *
 * The predicate converts both the person's name and the keywords to lowercase, splits the name into words,
 * and checks each keyword against the name's words. If any keyword matches a name word with a similarity
 * score above the threshold (default set to 0.6), the predicate evaluates to true.
 */
public class NameSimilarPredicate implements Predicate<Person> {
    private static double threshold = 0.6;

    private final List<String> keywords;
    public NameSimilarPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        assert person != null;
        String fullName = person.getName().fullName.toLowerCase();
        List<String> nameWords = Arrays.asList(fullName.split("\\s+"));

        for (String keyword : keywords) {
            String keywordLowerCase = keyword.toLowerCase();
            for (String word : nameWords) {
                if (StringUtil.calculateSimilarity(word, keywordLowerCase) >= threshold) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameSimilarPredicate)) {
            return false;
        }

        NameSimilarPredicate otherNameSimilarPrediate = (NameSimilarPredicate) other;
        return keywords.equals(otherNameSimilarPrediate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}

