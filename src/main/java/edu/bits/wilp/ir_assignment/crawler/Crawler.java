package edu.bits.wilp.ir_assignment.crawler;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import edu.bits.wilp.ir_assignment.utils.JsonSerDe;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Crawler {
    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    // global queue to maintain the list of urls that we need to crawl. Our workers would periodically pool this queue
    // for work and fetch the results. If we've reached the final page then we'll write the results separately.
    private static final LinkedBlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
    private static final LinkedBlockingQueue<Data> resultQueue = new LinkedBlockingQueue<>();
    // 1M is an over estimate, it should be relatively less than that
    private static final BloomFilter<String> crawledUrls = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")), 1_000_000);

    public static void main(String[] args) throws InterruptedException, IOException {
        String startUrl = "https://www.latlong.net/countries.html";
        String outputFile = "datasets/location-info.jsonl";
        String crawledDocsOutputDir = "crawled-docs/";

        loadAlreadyCrawledUrls(outputFile);
        int nrOfFetchWorkers = 10;
        int nrOfWriters = 1;

        LOG.info("Creating " + nrOfFetchWorkers + " fetch workers");
        for (int i = 0; i < nrOfFetchWorkers; i++) {
            Thread fetcherThread = new Thread(new FetchWorker(urlQueue, resultQueue, crawledUrls, crawledDocsOutputDir));
            fetcherThread.setName("FetchWorker" + i);
            fetcherThread.start();
        }

        LOG.info("Creating " + nrOfWriters + " result writers");
        for (int i = 0; i < nrOfWriters; i++) {
            Thread writer = new Thread(new ResultWriterWorker(resultQueue, outputFile));
            writer.setName("ResultWriter" + i);
            writer.start();
        }

        LOG.info("Adding the start url to the queue");
        urlQueue.offer(startUrl);
        Thread.currentThread().join();
    }

    private static void loadAlreadyCrawledUrls(String outputFile) throws IOException {
        if (new File(outputFile).exists()) {
            List<String> lines = IOUtils.readLines(new FileInputStream(outputFile), "UTF-8");
            for (String dataAsJson : lines) {
                Data data = JsonSerDe.fromJson(dataAsJson, Data.class);
                crawledUrls.put(data.getUrl());
            }
            LOG.info("Loaded information about " + lines.size() + " urls");
        }
    }

}
