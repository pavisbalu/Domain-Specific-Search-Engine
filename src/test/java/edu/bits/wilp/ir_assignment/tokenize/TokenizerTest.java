package edu.bits.wilp.ir_assignment.tokenize;

import org.apache.commons.lang3.StringUtils;
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

    @Test
    public void testTokenizerLowerCasesTokens() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItem("tv"));
        assertThat(tokens, hasItem("movi"));
    }

    @Test
    public void testTokenizerConvertAccentedCharsToAscii() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tàbles. This the bést I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItem("tabl"));
        assertThat(tokens, hasItem("best"));
    }

    @Test
    public void testTokenizerStripsAnyNonAlphaNumericChars() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, not(hasItem("movies/tv")));
        assertThat(tokens, hasItem("4"));
        assertThat(tokens, hasItem("music"));
    }

    @Test
    public void testTokenizerStemsTokens() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, hasItem("tabl"));
    }

    @Test
    public void testTokenizerStripsStopWords() {
        List<String> tokens = Tokenizer.tokens("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows.");

        assertThat(tokens, hasSize(12));
        assertThat(tokens, not(hasItem("have")));
    }
}
