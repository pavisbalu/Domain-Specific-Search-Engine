package edu.bits.wilp.ir_assignment.tokenize;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.*;

public class CounterTest {
    @Test
    public void testTermCounter() {
        Map<String, Double> counter = Counter.of(List.of("a", "b"));
        assertThat(counter, hasEntry("a", 1.0));
        assertThat(counter, hasEntry("b", 1.0));
    }

}