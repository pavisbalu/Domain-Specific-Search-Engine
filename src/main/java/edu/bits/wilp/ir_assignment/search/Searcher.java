package edu.bits.wilp.ir_assignment.search;

import edu.bits.wilp.ir_assignment.index.Document;
import edu.bits.wilp.ir_assignment.index.Documents;
import edu.bits.wilp.ir_assignment.index.TfIdItem;
import edu.bits.wilp.ir_assignment.index.TfIdf;
import edu.bits.wilp.ir_assignment.tokenize.Counter;
import edu.bits.wilp.ir_assignment.tokenize.Tokenizer;
import edu.bits.wilp.ir_assignment.utils.KryoSerDe;
import edu.bits.wilp.ir_assignment.utils.NumberUtil;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.linear.SparseFieldMatrix;
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
    private SparseFieldMatrix<Decimal64> D;
    private List<Document> documents;

    public Searcher(TfIdf tfIdf, List<Document> documents) {
        this.tfIdf = tfIdf;
        this.documents = documents;
        // document vectors (sparse matrix)
        D = new SparseFieldMatrix<>(Decimal64Field.getInstance(), tfIdf.N(), tfIdf.size());

        LOG.info("Loading D SparseMatrix");
        for (TfIdItem tfIdItem : tfIdf) {
            int index = tfIdf.indexOf(tfIdItem.getToken());
            D.setEntry(tfIdItem.getDocId(), index, new Decimal64(tfIdItem.getTfId()));
        }
        LOG.info("D SparseMatrix Loaded");
    }

    public List<OutputRank> search(int K, String query) {
        LOG.info("Searching for query: " + query);
        List<String> tokens = Tokenizer.tokens(query);
        FieldVector<Decimal64> queryVectors = generateVectors(tokens);
        LOG.info("Computed Query Vectors");

        LOG.info("Searching across documents");
        // TODO: This takes about 1.5 minutes on 10K documents, we can parallelize here
        List<OutputRank> ranks = new ArrayList<>();
        for (int docId = 0; docId < D.getRowDimension(); docId++) {
            FieldVector<Decimal64> documentVector = D.getRowVector(docId);

            double sim = NumberUtil.cosineSim(queryVectors, documentVector);
            // avoid adding unrelated documents to the output
            if (sim > 0.0) {
                ranks.add(new OutputRank(docId, sim, documents.get(docId).getText()));
            }
        }
        LOG.info("Computed cosine-sim across documents");

        ranks.sort((o1, o2) -> Double.compare(o2.getCosineSim(), o1.getCosineSim()));
        return ranks.stream().limit(K).collect(Collectors.toList());
    }

    private FieldVector<Decimal64> generateVectors(List<String> tokens) {
        SparseFieldVector<Decimal64> Q = new SparseFieldVector<>(Decimal64Field.getInstance(), tfIdf.size());

        Map<String, Double> counter = Counter.of(tokens);
        int wordsCount = counter.size();
        for (String token : tokens) {
            double tf = counter.get(token) / wordsCount;
            double df = tfIdf.DF(token);
            double idf = Math.log((tfIdf.size() + 1) / (df + 1));

            int index = tfIdf.indexOf(token);
            Q.setEntry(index, new Decimal64(tf * idf));
        }

        return Q;
    }


    public static void main(String[] args) throws FileNotFoundException {
        String modelFile = "output.bin";
        String documentFile = "documents.bin";
        TfIdf tfIdf = KryoSerDe.readFromFile(modelFile, TfIdf.class);
        Documents documents = KryoSerDe.readFromFile(documentFile, Documents.class);
        Searcher searcher = new Searcher(tfIdf, documents.getDocuments());
        String query = "table";
        if (args.length > 0) {
            query = args[0];
        }
        List<OutputRank> results = searcher.search(10, query);
        for (OutputRank result : results) {
            System.out.println(result);
        }
    }
}
