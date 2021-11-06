package edu.bits.wilp.ir_assignment.tokenize;

import ca.rmen.porterstemmer.PorterStemmer;

public class Stemmer {
    private static final PorterStemmer stemmer = new PorterStemmer();

    public static String stemWord(String input) {
        return stemmer.stemWord(input);
    }
}
