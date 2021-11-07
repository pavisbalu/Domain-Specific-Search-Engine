package edu.bits.wilp.ir_assignment.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class Crawler {
    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    // global queue to maintain the list of urls that we need to crawl. Our workers would periodically pool this queue
    // for work and fetch the results. If we've reached the final page then we'll write the results separately.
    private static final LinkedBlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
    private static final LinkedBlockingQueue<Data> resultQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
        String startUrl = "https://www.latlong.net/countries.html";
        String outputFile = "datasets/location-info.jsonl";
        int nrOfFetchWorkers = 3;
        int nrOfWriters = 1;

        LOG.info("Creating " + nrOfFetchWorkers + " fetch workers");
        for (int i = 0; i < nrOfFetchWorkers; i++) {
            Thread fetcherThread = new Thread(new FetchWorker(urlQueue, resultQueue));
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

}
