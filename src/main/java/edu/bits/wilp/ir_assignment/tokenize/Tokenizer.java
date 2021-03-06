package edu.bits.wilp.ir_assignment.tokenize;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tokenizer tokenize the input into List&lt;{@link String}&gt; of tokens.
 * The steps are as follows:
 * <ol>
 * <li>Lowercase the input</li>
 * <li>Remove anything that's not alpha numeric</li>
 * <li>Split tokens based on space</li>
 * <li>Stem using {@link Stemmer} implementation</li>
 * <li>Remove Stopwords. {@link StopWords} has details on the source</li>
 * </ol>
 */
public class Tokenizer {
    public static List<String> tokens(String input) {
        return Stream.of(input)
                .map(StringUtils::stripAccents)
                .map(String::toLowerCase)
                .map(s -> s.replaceAll("[^a-z0-9 ]", " "))
                .flatMap(s -> Stream.of(s.split("\\s+")))
                .map(Stemmer::stem)
                .filter(StopWords::isNotStopWord)
                .filter(s -> StringUtils.length(s) >= 3)
                .distinct()
                .collect(Collectors.toList());
    }
}
