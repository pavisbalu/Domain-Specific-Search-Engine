package edu.bits.wilp.ir_assignment.search;

import edu.bits.wilp.ir_assignment.index.TfIdItem;
import edu.bits.wilp.ir_assignment.index.TfIdf;
import edu.bits.wilp.ir_assignment.tokenize.Counter;
import edu.bits.wilp.ir_assignment.tokenize.Tokenizer;
import edu.bits.wilp.ir_assignment.utils.KryoSerDe;
import edu.bits.wilp.ir_assignment.utils.NumberUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Searcher {
    private TfIdf tfIdf;
    private double[][] D;

    public Searcher(TfIdf tfIdf) {
        this.tfIdf = tfIdf;
        // document vectors
        D = new double[tfIdf.N()][tfIdf.size()];
        for (int i = 0; i < tfIdf.N(); i++) {
            double[] elements = new double[tfIdf.size()];
            Arrays.fill(elements, 0.0);
            D[i] = elements;
        }

        for (TfIdItem tfIdItem : tfIdf) {
            int index = tfIdf.indexOf(tfIdItem.getToken());
            D[tfIdItem.getDocId()][index] = tfIdItem.getTfId();
        }
    }

    public double[] generateVectors(List<String> tokens) {
        double[] Q = new double[tfIdf.size()];
        Arrays.fill(Q, 0.0);

        Map<String, Double> counter = Counter.of(tokens);
        int wordsCount = counter.size();
        for (String token : tokens) {
            double tf = counter.get(token) / wordsCount;
            double df = tfIdf.DF(token);
            double idf = Math.log((tfIdf.size() + 1) / (df + 1));

            int index = tfIdf.indexOf(token);
            Q[index] = tf * idf;
        }

        return Q;
    }

    public List<OutputRank> search(int K, String query) {
        List<String> tokens = Tokenizer.tokens(query);
        double[] queryVectors = generateVectors(tokens);

        List<OutputRank> ranks = new ArrayList<>();
        for (int docId = 0; docId < D.length; docId++) {
            double[] documentVector = D[docId];
            double sim = NumberUtil.cosineSim(queryVectors, documentVector);
            // avoid adding unrelated documents to the output
            if (sim > 0.0) {
                ranks.add(new OutputRank(docId, sim));
            }
        }

        Collections.sort(ranks, (o1, o2) -> Double.compare(o2.getCosineSim(), o1.getCosineSim()));
        return ranks.stream().limit(K).collect(Collectors.toList());
    }

    public static void main(String[] args) throws FileNotFoundException {
        String modelFile = "output.bin";
        TfIdf tfIdf = KryoSerDe.readFromFile(new FileInputStream(modelFile), TfIdf.class);
        Searcher searcher = new Searcher(tfIdf);
        List<OutputRank> results = searcher.search(10, "table");
        for (OutputRank result : results) {
            System.out.println(result);
        }
    }
}
