package edu.bits.wilp.ir_assignment.indexing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Indexer {
    public static void main(String[] args) throws IOException {
        new Indexer().doWork();
    }

    public void doWork() throws IOException {
        String filename = "datasets/sample.csv";
        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = csvFormat().parse(in);
        for (CSVRecord record : records) {
            String name = record.get("name");
            String reviewsText = record.get("reviews.text");
            System.out.println(name);
            System.out.println(reviewsText);
        }
    }

    private CSVFormat csvFormat() {
        return CSVFormat.EXCEL.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
    }
}
