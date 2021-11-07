package edu.bits.wilp.ir_assignment.tokenize;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DocumentTokenizerTest {

    @Test
    public void testTokenizer() {
        DocumentTokenizer documentTokenizer = new DocumentTokenizer(List.of("I have 4 other kinds of tables. This the best I have had. " +
                "I can listen to music, read a book, listen to a book, or watch movies/tv shows."));
        documentTokenizer.process();

        Map<String, Double> termFrequency = documentTokenizer.tf();
        assertThat(termFrequency.get("tabl"), is(0.0834)); // 1 / 12
    }

}