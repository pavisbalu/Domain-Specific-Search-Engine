package edu.bits.wilp.ir_assignment.tokenize;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StemmerTest {
    @Test
    public void testStemWord() {
        // testing nouns -- should remain unchanged
        assertThat(Stemmer.stem("dog"), is("dog"));
        assertThat(Stemmer.stem("queen"), is("queen"));
        assertThat(Stemmer.stem("king"), is("king"));
        // testing variations of adverb, adjectives, etc. which should get stemmed
        assertThat(Stemmer.stem("likely"), is("like"));
        assertThat(Stemmer.stem("liked"), is("like"));
        assertThat(Stemmer.stem("liking"), is("like"));
        assertThat(Stemmer.stem("likes"), is("like"));
    }

}