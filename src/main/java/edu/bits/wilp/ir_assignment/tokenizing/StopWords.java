package edu.bits.wilp.ir_assignment.tokenizing;

import edu.bits.wilp.ir_assignment.utils.Preconditions;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StopWords {
    private static final Logger LOG = LoggerFactory.getLogger(StopWords.class);

    private static final Set<String> stopWords = new HashSet<>();

    static {
        try {
            InputStream input = StopWords.class.getResourceAsStream("/stopwords.txt");
            Preconditions.isNotNull(input);
            List<String> lines = IOUtils.readLines(input, Charset.defaultCharset());
            stopWords.addAll(lines);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static boolean isStopWord(String input) {
        return stopWords.contains(input);
    }

    public static List<String> filter(List<String> input) {
        return input.stream().filter((word) -> !stopWords.contains(word)).collect(Collectors.toList());
    }
}
