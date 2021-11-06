package edu.bits.wilp.ir_assignment.tokenize;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class TokenizerTest {
    @Test
    public void testTokenizeInput() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");
        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItems("tabl", "tv", "music", "read", "4", "thi", "watch", "kind", "movi",
                "book", "best", "listen"));
    }

    // Step 1 - Lower case all tokens
    @Test
    public void testTokenizerLowerCasesTokens() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItem("tv"));
        assertThat(tokens, hasItem("movi"));
    }

    // Step 2 - Strip any non-alpha numeric characters from the input
    @Test
    public void testTokenizerStripsAnyNonAlphaNumericChars() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, not(hasItem("movies/tv")));
        assertThat(tokens, hasItem("4"));
        assertThat(tokens, hasItem("music"));
    }

    // Step 3 - Use Porter Stemmer to stem each token
    @Test
    public void testTokenizerStemsTokens() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItem("tabl"));
    }

    // Step 4 - Remove Stopwords
    @Test
    public void testTokenizerStripsStopWords() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, not(hasItem("have")));
    }
}
