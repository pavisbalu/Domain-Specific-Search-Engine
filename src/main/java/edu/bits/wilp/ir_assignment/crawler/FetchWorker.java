package edu.bits.wilp.ir_assignment.crawler;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FetchWorker implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FetchWorker.class);
    private static final int WAIT_TIME_IN_MS = 3 * 1000; // seconds
    public static final int POLITENESS_DELAY_IN_MS = 2 * 1000; // seconds

    private LinkedBlockingQueue<String> fetchQueue;
    private LinkedBlockingQueue<Data> outQueue;

    public FetchWorker(LinkedBlockingQueue<String> fetchQueue, LinkedBlockingQueue<Data> outQueue) {
        this.fetchQueue = fetchQueue;
        this.outQueue = outQueue;
    }

    @Override
    public void run() {
        while (true) {
            LOG.info("Polling for work.");
            String url = fetchQueue.poll();
            if (url == null) {
                try {
                    LOG.info("No work! Trying again in " + toSeconds(WAIT_TIME_IN_MS) + " seconds");
                    Thread.sleep(WAIT_TIME_IN_MS);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            LOG.info("Work found for: " + url);
            try {
                // pattern 1 -- list of links, usually countries, cities, universities, etc.
                Document document = fetch(url);
                Elements linksInList = document.select("main>ul>li>a");
                addToFetchQueue(linksInList);
                // patten 2 -- actual places in a table with lat, long information
                Elements linksOnTable = document.select("main>table>tbody>tr>td>a");
                addToFetchQueue(linksOnTable);
                // pattern 3 -- final info page
                extractInfoPage(url, document);

                LOG.info("Work done, taking rest before asking for work again");
                // Politeness Delay for the site
                Thread.sleep(POLITENESS_DELAY_IN_MS);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private void extractInfoPage(String url, Document document) throws InterruptedException {
        String title = document.select("h1").text();
        Elements rows = document.select("table.margina>tbody>tr");
        Map<String, String> kvPairs = new HashMap<>();
        for (Element row : rows) {
            String key = row.select("th").text().toLowerCase();
            String value = row.select("td").text();
            kvPairs.put(key, value);
        }
        String description = document.selectXpath("/html/body/main/div[2]/div[1]/p[2]").text();
        if (!rows.isEmpty()) {
            Data extractedData = new Data(url, title, kvPairs, description);
            outQueue.offer(extractedData, 60, TimeUnit.MINUTES);
        }
    }

    private void addToFetchQueue(Elements linksOnTable) throws InterruptedException {
        for (Element e : linksOnTable) {
            String urlToFetch = e.absUrl("href");
            fetchQueue.offer(urlToFetch, 60, TimeUnit.MINUTES);
        }
    }

    private Document fetch(String url) {
        HttpResponse<String> response = Unirest.get(url).asString();
        String body = response.getBody();
        return Jsoup.parse(body, url);
    }

    private long toSeconds(int timeInMs) {
        return TimeUnit.MILLISECONDS.toSeconds(timeInMs);
    }

}
