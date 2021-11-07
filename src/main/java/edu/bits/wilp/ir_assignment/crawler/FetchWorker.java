package edu.bits.wilp.ir_assignment.crawler;

import com.google.common.base.Joiner;
import com.google.common.hash.BloomFilter;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FetchWorker implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FetchWorker.class);
    private static final int WAIT_TIME_IN_MS = 3 * 1000; // seconds
    public static final int POLITENESS_DELAY_IN_MS = 2 * 1000; // seconds

    private final String crawledDocsOutputDir;
    private final LinkedBlockingQueue<String> fetchQueue;
    private final LinkedBlockingQueue<Data> outQueue;
    private final BloomFilter<String> crawledUrls;

    public FetchWorker(LinkedBlockingQueue<String> fetchQueue, LinkedBlockingQueue<Data> outQueue, BloomFilter<String> crawledUrls, String crawledDocsOutputDir) {
        this.fetchQueue = fetchQueue;
        this.outQueue = outQueue;
        this.crawledUrls = crawledUrls;
        this.crawledDocsOutputDir = crawledDocsOutputDir;
    }

    @Override
    public void run() {
        while (true) {
            LOG.trace("Polling for work.");
            String url = fetchQueue.poll();
            if (url == null || crawledUrls.mightContain(url)) {
                try {
                    LOG.warn("No work! Trying again in " + toSeconds(WAIT_TIME_IN_MS) + " seconds");
                    if (StringUtils.isNotEmpty(url) && crawledUrls.mightContain(url)) {
                        LOG.info("Skipping page: " + url);
                    } else {
                        Thread.sleep(WAIT_TIME_IN_MS);
                    }
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            LOG.info("Work found for: " + url);
            try {
                File fetchedDocument = cacheFile(url);
                boolean cachedCopy = fetchedDocument.exists();
                // pattern 1 -- list of links, usually countries, cities, universities, etc.
                Document document = fetch(url, fetchedDocument);
                Elements linksInList = document.select("main>ul>li>a");
                addToFetchQueue(linksInList);
                // patten 2 -- actual places in a table with lat, long information
                Elements linksOnTable = document.select("main>table>tbody>tr>td>a");
                addToFetchQueue(linksOnTable);
                Elements nextLinks = document.select("a:contains(next)");
                addToFetchQueue(nextLinks);
                // pattern 3 -- final info page
                extractInfoPage(url, document);

                crawledUrls.put(url);

                LOG.info("Work complete for: " + url);
                if (!cachedCopy) {
                    // Politeness Delay for the site
                    Thread.sleep(POLITENESS_DELAY_IN_MS);
                }
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
            if (!urlToFetch.endsWith("#") && !crawledUrls.mightContain(urlToFetch)) {
                fetchQueue.offer(urlToFetch, 60, TimeUnit.MINUTES);
            }
        }
    }

    private Document fetch(String url, File fetchedDocument) throws IOException {
        // if url exist locally use that instead of fetching them again
        if (fetchedDocument.exists()) {
            LOG.info("Cached copy found, using that for: " + url);
            List<String> lines = IOUtils.readLines(new FileInputStream(fetchedDocument), "UTF-8");
            String body = Joiner.on('\n').join(lines);
            return Jsoup.parse(body, url);
        } else {
            HttpResponse<String> response = Unirest.get(url).asString();
            if (!response.isSuccess()) {
                throw new RuntimeException(String.format("Invalid page. Got %s (%d) for: %s", response.getStatusText(), response.getStatus(), url));
            }
            String body = response.getBody();
            writeDocumentToDisk(body, fetchedDocument);
            return Jsoup.parse(body, url);
        }
    }

    private File cacheFile(String url) {
        String filename = DigestUtils.md5Hex(url);
        File fetchedDocument = new File(crawledDocsOutputDir, filename);
        return fetchedDocument;
    }

    private void writeDocumentToDisk(String body, File filename) throws IOException {
        try (FileOutputStream output = new FileOutputStream(filename)) {
            IOUtils.write(body, output, "UTF-8");
        }
    }

    private long toSeconds(int timeInMs) {
        return TimeUnit.MILLISECONDS.toSeconds(timeInMs);
    }

}
