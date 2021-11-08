package edu.bits.wilp.ir_assignment.search;

import edu.bits.wilp.ir_assignment.index.Document;
import edu.bits.wilp.ir_assignment.index.Documents;
import edu.bits.wilp.ir_assignment.index.TfIdItem;
import edu.bits.wilp.ir_assignment.index.TfIdf;
import edu.bits.wilp.ir_assignment.math.SparseVector;
import edu.bits.wilp.ir_assignment.math.SparseMatrix;
import edu.bits.wilp.ir_assignment.tokenize.Counter;
import edu.bits.wilp.ir_assignment.tokenize.Tokenizer;
import edu.bits.wilp.ir_assignment.utils.JsonSerDe;
import edu.bits.wilp.ir_assignment.utils.KryoSerDe;
import edu.bits.wilp.ir_assignment.utils.NumberUtil;
import edu.bits.wilp.ir_assignment.utils.SystemUtil;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.linear.SparseFieldVector;
import org.apache.commons.math3.util.Decimal64;
import org.apache.commons.math3.util.Decimal64Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Uses the {@link TfIdf} and {@link Document} models to search a given query and return the response.
 */
public class Searcher {
    private static final Logger LOG = LoggerFactory.getLogger(Searcher.class);
    private TfIdf tfIdf;
    private SparseMatrix D;
    private List<Document> documents;

    public Searcher(TfIdf tfIdf, List<Document> documents) {
        this.tfIdf = tfIdf;
        this.documents = documents;
        // document vectors (sparse matrix)
        D = new SparseMatrix(tfIdf.N(), tfIdf.size());

        LOG.info("Loading D SparseMatrix");
        for (TfIdItem tfIdItem : tfIdf) {
            int index = tfIdf.indexOf(tfIdItem.getToken());
            D.setEntry(tfIdItem.getDocId(), index, new Decimal64(tfIdItem.getTfId()));
        }
        LOG.info("D SparseMatrix Loaded");
    }

    public SearchResult search(int K, String query) {
        LOG.info("Searching for query: " + query);
        List<String> tokens = Tokenizer.tokens(query);
        SparseVector queryVectors = generateVectors(tokens);
        LOG.info("Computed Query Vectors");

        LOG.info("Searching across documents: " + D.getRowDimension());
        List<OutputRecord> ranks = new ArrayList<>();
        for (int docId = 0; docId < D.getRowDimension(); docId++) {
            SparseVector documentVector = D.getRow(docId);
            // do the cosine-sim calculation only if we have any overlap
            double sim = NumberUtil.cosineSim(documentVector, queryVectors);
            // avoid adding unrelated documents to the output
            if (!Double.isNaN(sim) && sim > 0.0) {
                ranks.add(new OutputRecord(docId, sim, documents.get(docId)));
            }
        }
        LOG.info("Computed cosine-sim across documents");

        ranks.sort((o1, o2) -> Double.compare(o2.getCosineSim(), o1.getCosineSim()));
        List<OutputRecord> outputRecords = ranks.stream().limit(K).collect(Collectors.toList());
        double precision = NumberUtil.roundDouble((double) ranks.size() / documents.size(), 4);
        double recall = NumberUtil.roundDouble(
                ranks.size() > 10 ? 10.0 / ranks.size() :
                        ranks.size() == 0 ? 0 : 1,
                4);
        return new SearchResult(outputRecords, precision, recall);
    }

    private SparseVector generateVectors(List<String> tokens) {
        SparseVector Q = new SparseVector(tfIdf.size());

        Map<String, Double> counter = Counter.of(tokens);
        int wordsCount = counter.size();
        for (String token : tokens) {
            int index = tfIdf.indexOf(token);
            if (index > -1) {
                double tf = counter.get(token) / wordsCount;
                double df = tfIdf.DF(token);
                double idf = Math.log((tfIdf.size() + 1) / (df + 1));

                Q.setEntry(index, new Decimal64(tf * idf));
            }
        }

        return Q;
    }


    public static void main(String[] args) throws FileNotFoundException {
        SystemUtil.disableWarning();

        String modelFile = "output.bin";
        String documentFile = "documents.bin";
        TfIdf tfIdf = KryoSerDe.readFromFile(modelFile, TfIdf.class);
        Documents documents = KryoSerDe.readFromFile(documentFile, Documents.class);
        Searcher searcher = new Searcher(tfIdf, documents.getDocuments());
        String query = "india";
        if (args.length > 0) {
            query = args[0];
        }
        SearchResult result = searcher.search(10, query);
        List<OutputRecord> records = result.getRecords();
        System.out.println("Precision: " + result.getPrecision() + ", Recall: " + result.getRecall());
        for (OutputRecord record : records) {
            System.out.println(JsonSerDe.toJson(record));
        }
    }
}
