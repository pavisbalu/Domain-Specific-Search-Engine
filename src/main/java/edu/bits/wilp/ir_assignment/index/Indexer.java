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

        Map<String, Double> alphaMapping = new HashMap<>();
        alphaMapping.put("title", 0.75);
        alphaMapping.put("description", 0.25);
        new Indexer().index(documents, alphaMapping, "output.bin", "documents.bin");
    }

    /**
     * Start the indexing process.
     *
     * @throws IOException IOException
     */
    public void index(List<Document> documents, Map<String, Double> alphaMapping, String modelFile, String documentsFile) throws IOException {
        LOG.info("Alpha Mapping: " + JsonSerDe.toJson(alphaMapping));
        LOG.info("Output Model File: " + modelFile);
        LOG.info("Documents File: " + documentsFile);
        LOG.info("Starting to tokenize documents");
        processDocuments(documents);
        LOG.info("Tokenize documents complete");

        final int N = documents.size();
        Map<String, Double> DF = computeDF(documents);
        LOG.info("DF Complete");
        LOG.info("Starting Tf-Idf calculations");
        TfIdf tfIdfTable = new TfIdf(N, DF);

        Double sumOfAlphaOverrides = alphaMapping.values().stream().reduce(0.0, Double::sum);

        // compute tf-idf
        // the alpha is the weight assigned to tokens from a given field to boost relevancy scores
        // tf-idf(t, d) = alpha * tf(t, d) * log(N/(df + 1))
        for (Document document : documents) {
            // we use the alpha from alpha mapping for the fields that are present
            // for the rest of the fields, we'll split equally between the available value
            double defaultAlpha = (1.0 - sumOfAlphaOverrides) / document.indexedFields().size();   // alpha is weight assigned to each indexed field.
            for (String indexedField : document.indexedFields()) {
                Set<String> tokens = document.tokens(indexedField);
                for (String token : tokens) {
                    double tf = document.tf(indexedField, token);
                    double df = DF.get(token);
                    double idf = Math.log(N + 1 / df + 1);
                    Double alpha = alphaMapping.getOrDefault(indexedField, defaultAlpha);
                    if (alpha == 0.0) {
                        throw new RuntimeException("We've got a zero alpha for the field: " + indexedField);
                    }
                    double tfIdf = alpha * NumberUtil.roundDouble(tf * idf, DECIMAL_PRECISION);
                    double existingTfIdf = tfIdfTable.getOrElse(document.getDocId(), token, 0.0);
                    double newTfIdf = tfIdf + existingTfIdf;
                    tfIdfTable.add(document.getDocId(), token, newTfIdf);
                }
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
            document.setDocId(docId);
            for (String indexedField : document.indexedFields()) {
                DocumentTokenizer docTokenizer = new DocumentTokenizer(List.of(document.get(indexedField)));
                docTokenizer.process();
                Map<String, Double> fieldTf = docTokenizer.tf();
                document.setTfPerIndexedField(indexedField, fieldTf);
            }

            docId++;
        }
    }

    private Map<String, Double> computeDF(List<Document> documents) {
        Map<String, Double> DF = new HashMap<>();
        Map<String, List<Integer>> termToDocuments = new HashMap<>();
        // df(t) = occurrence of t in N documents
        // to normalize the value, we also divide the occurrence by total number of documents
        for (Document doc : documents) {
            for (String indexedField : doc.indexedFields()) {
                Set<String> tokens = doc.tokens(indexedField);
                for (String token : tokens) {
                    List<Integer> existingDocuments = termToDocuments.getOrDefault(token, new ArrayList<>());
                    existingDocuments.add(doc.getDocId());
                    termToDocuments.put(token, existingDocuments);
                }
            }
        }

        final int N = documents.size();
        for (String term : termToDocuments.keySet()) {
            DF.put(term, (double) termToDocuments.get(term).size() / N);
        }
        return DF;
    }
}
