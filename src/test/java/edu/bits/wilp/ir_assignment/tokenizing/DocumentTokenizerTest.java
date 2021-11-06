package edu.bits.wilp.ir_assignment.tokenizing;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class DocumentTokenizerTest {

    @Test
    public void testTokenizer() {
        DocumentTokenizer documentTokenizer = new DocumentTokenizer(List.of("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows."));
        documentTokenizer.process();
        Set<String> tokens = documentTokenizer.tokens();

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItems("tabl", "tv", "music", "read", "4", "thi", "watch", "kind", "movi",
                "book", "best", "listen"));

        Map<String, Double> termFrequency = documentTokenizer.tf();
        assertThat(termFrequency.get("tabl"), is(0.0834)); // 1 / 12
        assertThat(termFrequency.get("book"), is(0.1667)); // 2 / 12
    }

    // Step 1 - Lower case all tokens
    @Test
    public void testTokenizerLowerCasesTokens() {
        DocumentTokenizer documentTokenizer = new DocumentTokenizer(List.of("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows."));
        documentTokenizer.process();
        Set<String> tokens = documentTokenizer.tokens();

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItem("tv"));
        assertThat(tokens, hasItem("movi"));
    }

    // Step 2 - Strip any non-alpha numeric characters from the input
    @Test
    public void testTokenizerStripsAnyNonAlphaNumericChars() {
        DocumentTokenizer documentTokenizer = new DocumentTokenizer(List.of("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows."));
        documentTokenizer.process();
        Set<String> tokens = documentTokenizer.tokens();

        assertThat(tokens, hasSize(12));
        assertThat(tokens, not(hasItem("movies/tv")));
        assertThat(tokens, hasItem("4"));
        assertThat(tokens, hasItem("music"));
    }

    // Step 3 - Use Porter Stemmer to stem each token
    @Test
    public void testTokenizerStemsTokens() {
        DocumentTokenizer documentTokenizer = new DocumentTokenizer(List.of("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows."));
        documentTokenizer.process();
        Set<String> tokens = documentTokenizer.tokens();

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItem("tabl"));
    }

    // Step 4 - Remove Stopwords
    @Test
    public void testTokenizerStripsStopWords() {
        DocumentTokenizer documentTokenizer = new DocumentTokenizer(List.of("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows."));
        documentTokenizer.process();
        Set<String> tokens = documentTokenizer.tokens();

        assertThat(tokens, hasSize(12));
        assertThat(tokens, not(hasItem("have")));
    }

}