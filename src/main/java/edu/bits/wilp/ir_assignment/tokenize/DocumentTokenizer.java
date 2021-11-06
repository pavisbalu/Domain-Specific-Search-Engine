package edu.bits.wilp.ir_assignment.tokenize;

import edu.bits.wilp.ir_assignment.utils.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

// Refer to tests for documentation / process
public class DocumentTokenizer {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentTokenizer.class);
    private final List<String> linesInDocument;
    private final Map<String, Double> tf;

    public DocumentTokenizer(List<String> linesInDocument) {
        this.linesInDocument = linesInDocument;
        this.tf = new HashMap<>();
    }

    public void process() {
        // we don't want to tokenize the document twice and mess up the termFrequency calculations
        if (tf.size() > 0) {
            LOG.warn("Calling tokenize twice is not recommended. Please call DocumentTokenizer#tokenize only once in your code.");
            return;
        }

        List<String> tokens = linesInDocument.stream()
                .flatMap(line -> Tokenizer.tokens(line).stream())
                .collect(Collectors.toList());
        Map<String, Double> termCounts = Counter.of(tokens);

        // compute TF
        // tf(t,d) = count of t in d / number of words in d
        tf.clear(); // paranoid check to avoid tf calculations mismatch
        int N = termCounts.size();
        for (String token : termCounts.keySet()) {
            tf.put(token, NumberUtil.roundDouble(termCounts.get(token) / N, 4));
        }
    }

    public Set<String> tokens() {
        return this.tf.keySet();
    }

    public Map<String, Double> tf() {
        return this.tf;
    }


}
