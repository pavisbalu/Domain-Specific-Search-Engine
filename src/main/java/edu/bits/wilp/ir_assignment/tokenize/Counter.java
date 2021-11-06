package edu.bits.wilp.ir_assignment.tokenize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class Counter {
    public static Map<String, Double> of(List<String> tokens) {
        return tokens.stream().reduce(new HashMap<>(), termCount(), mergeCounters());
    }

    private static BiFunction<Map<String, Double>, String, Map<String, Double>> termCount() {
        return (counter, word) -> {
            Double count = counter.getOrDefault(word, 0.0);
            counter.put(word, count + 1.0);
            return counter;
        };
    }

    private static BinaryOperator<Map<String, Double>> mergeCounters() {
        return (left, right) -> {
            Map<String, Double> mergedCounter = new HashMap<>();
            left.keySet().forEach(word -> {
                Double counterSoFar = mergedCounter.getOrDefault(word, 0.0);
                mergedCounter.put(word, counterSoFar + left.get(word));
            });

            right.keySet().forEach(word -> {
                Double counterSoFar = mergedCounter.getOrDefault(word, 0.0);
                mergedCounter.put(word, counterSoFar + right.get(word));
            });

            return mergedCounter;
        };
    }

}
