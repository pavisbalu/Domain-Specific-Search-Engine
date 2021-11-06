package edu.bits.wilp.ir_assignment.indexing;

import edu.bits.wilp.ir_assignment.tokenizing.DocumentTokenizer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Indexer {
    public static void main(String[] args) throws IOException {
        new Indexer().doWork();
    }

    public void doWork() throws IOException {
        String filename = "datasets/sample.csv";
        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = csvFormat().parse(in);
        int docId = 0;
        List<DocumentMeta> documents = new ArrayList<>();
        for (CSVRecord record : records) {
            String reviewsText = record.get("reviews.text");
            DocumentTokenizer document = new DocumentTokenizer(List.of(reviewsText));
            document.process();
            Map<String, Double> docTf = document.tf();
            documents.add(new DocumentMeta(docTf, docId));

            // update the docId, so we can consider each review as a new document
            docId++;
        }

        final int N = documents.size();
        Map<String, Double> DF = computeDF(documents);
        Set<String> VOCAB = DF.keySet();

        for (DocumentMeta document : documents) {
            Set<String> tokens = document.tokens();
            for (String token : tokens) {
                double tf = document.tf(token);
                double df = DF.get(token);
                double idf = Math.log(N + 1 / df + 1);
                double tfIdf = tf * idf;
                // TODO: Store these TF-IDF values separately and persist them in a file for later consumption
                System.out.println(token + "@" + document.docId + " - tf-idf:" + tfIdf);
            }
        }
    }

    private Map<String, Double> computeDF(List<DocumentMeta> documents) {
        Map<String, Double> DF = new HashMap<>();
        Map<String, List<Integer>> termToDocuments = new HashMap<>();
        // df(t) = occurrence of t in N documents
        // to normalize the value, we also divide the occurrence by total number of documents
        for (DocumentMeta doc : documents) {
            Set<String> tokens = doc.tokens();
            for (String token : tokens) {
                List<Integer> existingDocuments = termToDocuments.getOrDefault(token, new ArrayList<>());
                existingDocuments.add(doc.docId);
                termToDocuments.put(token, existingDocuments);
            }
        }

        final int N = documents.size();
        for (String term : termToDocuments.keySet()) {
            DF.put(term, (double) termToDocuments.get(term).size() / N);
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
