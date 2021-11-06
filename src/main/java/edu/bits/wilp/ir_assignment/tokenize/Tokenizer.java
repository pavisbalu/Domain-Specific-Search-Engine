package edu.bits.wilp.ir_assignment.tokenize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tokenizer {
    public static List<String> tokens(String input) {
        return new ArrayList<>(
                Stream.of(input)
                        .map(String::toLowerCase)
                        .map(s -> s.replaceAll("[^a-z0-9 ]", " "))
                        .flatMap(s -> Stream.of(s.split("\\s+")))
                        .map(Stemmer::stemWord)
                        .filter(StopWords::isNotStopWord)
                        .collect(Collectors.toSet())
        );
    }
}
