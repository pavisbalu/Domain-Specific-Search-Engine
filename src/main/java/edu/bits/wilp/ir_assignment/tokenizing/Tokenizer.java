package edu.bits.wilp.ir_assignment.tokenizing;

import java.util.*;
import java.util.stream.Collectors;

// Refer to tests for documentation / process
public class Tokenizer {
    public static Set<String> tokenize(List<String> lines) {
        return lines.stream()
                .map(Tokenizer::processLine)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static Set<String> processLine(String line) {
        return Arrays.stream(line.toLowerCase().replaceAll("[^a-z0-9 ]", " ").split("\\s+"))
                .map(Stemmer::stemWord)
                .filter(StopWords::isNotStopWord)
                .collect(Collectors.toSet());
    }
}
