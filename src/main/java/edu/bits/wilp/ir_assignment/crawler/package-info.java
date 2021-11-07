/**
 * <h2>Location Dataset Collection</h2>
 *
 * <p>
 * This package implements a custom crawler for
 * <a href="https://www.latlong.net/countries.html">https://www.latlong.net/countries.html</a> page. The intent is to
 * navigate the website from the mentioned link to gather all the places along with their latitude and longitude
 * information.
 * </p>
 *
 * <h2>Crawler &amp; Architecture</h2>
 *
 * <p>
 * {@link edu.bits.wilp.ir_assignment.crawler.Crawler} is the entry point of the program which uses multi-threading
 * to crawl the website and collect the data. The entire crawler is modelled as a producer-producer-consumer problem.
 * </p>
 *
 * <p>
 * {@link edu.bits.wilp.ir_assignment.crawler.FetchWorker} is the Fetcher worker thread that helps in crawling and
 * adding the discovered urls back to the queue for crawling.
 * </p>
 *
 * <p>
 * {@link edu.bits.wilp.ir_assignment.crawler.ResultWriterWorker} is the Writer worker thread that persist the final
 * information to disk. Our file format follows JSONL format, in which each line is a valid JSON record. Instead of
 * storing the results in an entire array, we write each result in a separate line so it is easily splittable and
 * processable in parallel.
 * </p>
 */
package edu.bits.wilp.ir_assignment.crawler;