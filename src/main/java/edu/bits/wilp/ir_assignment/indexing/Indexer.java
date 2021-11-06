package edu.bits.wilp.ir_assignment.indexing;

import edu.bits.wilp.ir_assignment.tokenizing.DocumentTokenizer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {
    public static void main(String[] args) throws IOException {
        new Indexer().doWork();
    }

    public void doWork() throws IOException {
        String filename = "datasets/sample.csv";
        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = csvFormat().parse(in);
        int docId = 0;
        Map<String, Double> TF = new HashMap<>();
        Map<String, List<Integer>> documentToTerms = new HashMap<>();
        for (CSVRecord record : records) {
            String reviewsText = record.get("reviews.text");
            DocumentTokenizer document = new DocumentTokenizer(List.of(reviewsText));
            document.process();
            Map<String, Double> docTf = document.tf();

            TF = DocumentTokenizer.mergeCounters().apply(TF, docTf);
            for (String token : document.tokens()) {
                List<Integer> existingDocuments = documentToTerms.getOrDefault(token, new ArrayList<>());
                existingDocuments.add(docId);
                documentToTerms.put(token, existingDocuments);
            }

            // update the docId, so we can consider each review as a new document
            docId++;
        }

        int totalDocuments = docId;
        Map<String, Double> DF = computeDF(documentToTerms, totalDocuments);
        Map<String, Double> IDF = computeIDF(totalDocuments, DF);
    }

    private Map<String, Double> computeIDF(int totalDocuments, Map<String, Double> DF) {
        Map<String, Double> IDF = new HashMap<>();
        for (String term : DF.keySet()) {
            double idf = (double) totalDocuments / DF.get(term);
            IDF.put(term, idf);
        }
        return IDF;
    }

    private Map<String, Double> computeDF(Map<String, List<Integer>> documentToTerms, int totalDocuments) {
        Map<String, Double> DF = new HashMap<>();
        // df(t) = occurrence of t in N documents
        // to normalize the value, we also divide the occurrence by total number of documents
        for (String token : documentToTerms.keySet()) {
            DF.put(token, (double) documentToTerms.get(token).size() / totalDocuments);
        }
        return DF;
    }

    private CSVFormat csvFormat() {
        return CSVFormat.EXCEL.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
    }
}
