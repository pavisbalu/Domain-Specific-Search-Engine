package edu.bits.wilp.ir_assignment.tokenize;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StemmerTest {
    @Test
    public void testStemWord() {
        // testing nouns -- should remain unchanged
        assertThat(Stemmer.stemWord("dog"), is("dog"));
        assertThat(Stemmer.stemWord("queen"), is("queen"));
        assertThat(Stemmer.stemWord("king"), is("king"));
        // testing variations of adverb, adjectives, etc. which should get stemmed
        assertThat(Stemmer.stemWord("likely"), is("like"));
        assertThat(Stemmer.stemWord("liked"), is("like"));
        assertThat(Stemmer.stemWord("liking"), is("like"));
        assertThat(Stemmer.stemWord("likes"), is("like"));
    }

}