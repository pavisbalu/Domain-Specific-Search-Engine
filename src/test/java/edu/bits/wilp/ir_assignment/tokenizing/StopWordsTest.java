package edu.bits.wilp.ir_assignment.tokenizing;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class StopWordsTest {
    @Test
    public void testIsStopWord() {
        assertTrue(StopWords.isStopWord("a"));
        assertTrue(StopWords.isStopWord("the"));
        assertFalse(StopWords.isStopWord("pavithra"));
    }

    @Test
    public void testIsNotStopWord() {
        assertFalse(StopWords.isNotStopWord("a"));
        assertFalse(StopWords.isNotStopWord("the"));
        assertTrue(StopWords.isNotStopWord("pavithra"));
    }

    @Test
    public void testFilter() {
        List<String> input = List.of("a quick brown fox jumped over a lazy dog".split(" "));
        List<String> filteredWords = StopWords.filter(input);
        // a,over -- are the stop words
        assertThat(filteredWords, hasSize(6));
    }
}