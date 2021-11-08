package edu.bits.wilp.ir_assignment.index;

import edu.bits.wilp.ir_assignment.crawler.Data;
import edu.bits.wilp.ir_assignment.tokenize.DocumentTokenizer;
import edu.bits.wilp.ir_assignment.utils.JsonSerDe;
import edu.bits.wilp.ir_assignment.utils.KryoSerDe;
import edu.bits.wilp.ir_assignment.utils.NumberUtil;
import edu.bits.wilp.ir_assignment.utils.SystemUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Indexes the documents and writes the tf-idf and document models.
 */
public class Indexer {
    private static final Logger LOG = LoggerFactory.getLogger(Indexer.class);
    public static final int DECIMAL_PRECISION = 4;

    public static void main(String[] args) throws IOException {
        SystemUtil.disableWarning();
        String inputFile = "datasets/location-info.jsonl";
        List<Document> documents = new ArrayList<>();
        if (new File(inputFile).exists()) {
            List<String> lines = IOUtils.readLines(new FileInputStream(inputFile), "UTF-8");
            for (String dataAsJson : lines) {
                Data data = JsonSerDe.fromJson(dataAsJson, Data.class);
                Document doc = new Document();
                doc.addNonIndexedField("url", data.getUrl());
                for (String key : data.getFields().keySet()) {
                    doc.addNonIndexedField(key, data.getFields().get(key));
                }
                doc.addIndexField("title", data.getTitle());
                doc.addIndexField("description", data.getDescription());

                documents.add(doc);
            }
        }

        new Indexer().index(documents, "output.bin", "documents.bin");
    }

    /**
     * Start the indexing process.
     *
     * @throws IOException IOException
     */
    public void index(List<Document> documents, String modelFile, String documentsFile) throws IOException {
        LOG.info("Starting to tokenize documents");
        processDocuments(documents);
        LOG.info("Tokenize documents complete");

        final int N = documents.size();
        Map<String, Double> DF = computeDF(documents);
        LOG.info("DF Complete");
        LOG.info("Starting Tf-Idf calculations");
        TfIdf tfIdfTable = new TfIdf(N, DF);

        // compute tf-idf
        // tf-idf(t, d) = tf(t, d) * log(N/(df + 1))
        for (Document document : documents) {
            Set<String> tokens = document.tokens();
            for (String token : tokens) {
                double tf = document.tf(token);
                double df = DF.get(token);
                double idf = Math.log(N + 1 / df + 1);
                double tfIdf = NumberUtil.roundDouble(tf * idf, DECIMAL_PRECISION);
                tfIdfTable.add(document.getDocId(), token, tfIdf);
            }
        }
        LOG.info("Tf-Idf calculations, done. Writing the model file: " + modelFile);

        KryoSerDe.writeToFile(tfIdfTable, modelFile);

        LOG.info("Persisting the documents: " + documentsFile);
        KryoSerDe.writeToFile(new Documents(documents), documentsFile);

        LOG.info("Indexing Complete");
    }

    private void processDocuments(List<Document> documents) {
        int docId = 0;
        for (Document document : documents) {
            // TODO: Need to tokenize each indexed field separately
            DocumentTokenizer docTokenizer = new DocumentTokenizer(document.indexedValues());
            docTokenizer.process();
            Map<String, Double> docTf = docTokenizer.tf();

            document.setDocId(docId).setTf(docTf);

            docId++;
        }
    }

    private Map<String, Double> computeDF(List<Document> documents) {
        Map<String, Double> DF = new HashMap<>();
        Map<String, List<Integer>> termToDocuments = new HashMap<>();
        // df(t) = occurrence of t in N documents
        // to normalize the value, we also divide the occurrence by total number of documents
        for (Document doc : documents) {
            Set<String> tokens = doc.tokens();
            for (String token : tokens) {
                List<Integer> existingDocuments = termToDocuments.getOrDefault(token, new ArrayList<>());
                existingDocuments.add(doc.getDocId());
                termToDocuments.put(token, existingDocuments);
            }
        }

        final int N = documents.size();
        for (String term : termToDocuments.keySet()) {
            DF.put(term, (double) termToDocuments.get(term).size() / N);
        }
        return DF;
    }
}
