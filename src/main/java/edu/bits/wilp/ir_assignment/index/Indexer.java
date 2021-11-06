package edu.bits.wilp.ir_assignment.index;

import edu.bits.wilp.ir_assignment.tokenize.DocumentTokenizer;
import edu.bits.wilp.ir_assignment.utils.KryoSerDe;
import edu.bits.wilp.ir_assignment.utils.NumberUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Indexer {
    public static void main(String[] args) throws IOException {
        new Indexer().index("datasets/sample.csv", "reviews.text", "output.bin");
    }

    public void index(String filename, String inputFile, String outputFile) throws IOException {
        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = csvFormat().parse(in);

        List<DocumentMeta> documents = processDocuments(inputFile, records);

        final int N = documents.size();
        Map<String, Double> DF = computeDF(documents);
        TfIdf tfIdfTable = new TfIdf(N, DF);

        // compute tf-idf
        // tf-idf(t, d) = tf(t, d) * log(N/(df + 1))
        for (DocumentMeta document : documents) {
            Set<String> tokens = document.tokens();
            for (String token : tokens) {
                double tf = document.tf(token);
                double df = DF.get(token);
                double idf = Math.log(N + 1 / df + 1);
                double tfIdf = NumberUtil.roundDouble(tf * idf, 4);
                tfIdfTable.add(document.docId, token, tfIdf);
            }
        }

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        KryoSerDe.writeToFile(tfIdfTable, outputStream);
        outputStream.close();
    }

    private List<DocumentMeta> processDocuments(String fieldName, Iterable<CSVRecord> records) {
        List<DocumentMeta> documents = new ArrayList<>();
        int docId = 0;
        for (CSVRecord record : records) {
            String reviewsText = record.get(fieldName);
            DocumentTokenizer document = new DocumentTokenizer(List.of(reviewsText));
            document.process();
            Map<String, Double> docTf = document.tf();
            documents.add(new DocumentMeta(docTf, docId));

            // update the docId, so we can consider each review as a new document
            docId++;
        }
        return documents;
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
