package edu.bits.wilp.ir.assignment;

import ca.rmen.porterstemmer.PorterStemmer;

public class PorterExample {
    public static void main(String[] args) {
        PorterStemmer porterStemmer = new PorterStemmer();
        String word = porterStemmer.stemWord("incorporated");// returns "incorpor"
        System.out.println(word);
    }
}
