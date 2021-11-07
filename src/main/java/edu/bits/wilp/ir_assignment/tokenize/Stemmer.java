package edu.bits.wilp.ir_assignment.tokenize;

import ca.rmen.porterstemmer.PorterStemmer;

/**
 * Wraps the {@link PorterStemmer}
 * from <a href="https://github.com/caarmen/porter-stemmer">https://github.com/caarmen/porter-stemmer</a>
 */
public class Stemmer {
    private static final PorterStemmer stemmer = new PorterStemmer();

    public static String stem(String input) {
        return stemmer.stemWord(input);
    }
}
