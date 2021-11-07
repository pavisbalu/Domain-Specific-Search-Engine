package edu.bits.wilp.ir_assignment.search;

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

public class Searcher {
    private static final Logger LOG = LoggerFactory.getLogger(Searcher.class);
    private TfIdf tfIdf;
    private SparseFieldMatrix<Decimal64> D;

    public Searcher(TfIdf tfIdf) {
        this.tfIdf = tfIdf;
        // document vectors (sparse matrix)
        D = new SparseFieldMatrix<>(Decimal64Field.getInstance(), tfIdf.N(), tfIdf.size());

        LOG.info("Loading D SparseMatrix");
        for (TfIdItem tfIdItem : tfIdf) {
            int index = tfIdf.indexOf(tfIdItem.getToken());
            D.setEntry(tfIdItem.getDocId(), index, new Decimal64(tfIdItem.getTfId()));
        }
        LOG.info("D SparseMatrix Loaded");
    }

    public FieldVector<Decimal64> generateVectors(List<String> tokens) {
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

    public List<OutputRank> search(int K, String query) {
        LOG.info("Searching for query: " + query);
        List<String> tokens = Tokenizer.tokens(query);
        FieldVector<Decimal64> queryVectors = generateVectors(tokens);
        LOG.info("Computed Query Vectors");

        LOG.info("Searching across documents");
        List<OutputRank> ranks = new ArrayList<>();
        for (int docId = 0; docId < D.getRowDimension(); docId++) {
            FieldVector<Decimal64> documentVector = D.getRowVector(docId);

            double sim = NumberUtil.cosineSim(queryVectors, documentVector);
            // avoid adding unrelated documents to the output
            if (sim > 0.0) {
                ranks.add(new OutputRank(docId, sim));
            }
        }
        LOG.info("Computed cosinesim across documents");

        ranks.sort((o1, o2) -> Double.compare(o2.getCosineSim(), o1.getCosineSim()));
        return ranks.stream().limit(K).collect(Collectors.toList());
    }


    public static void main(String[] args) throws FileNotFoundException {
        String modelFile = "output.bin";
        TfIdf tfIdf = KryoSerDe.readFromFile(modelFile, TfIdf.class);
        Searcher searcher = new Searcher(tfIdf);
        List<OutputRank> results = searcher.search(10, "table");
        for (OutputRank result : results) {
            System.out.println(result);
        }
    }
}
