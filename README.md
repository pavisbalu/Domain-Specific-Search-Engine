# Group A1 - IR Assignment

## Usage

Assignment was built using JDK 11 and we use Maven as our build tool of choice.

To build, run the tests and package into the final JAR please run the following command.

```
$ mvn clean package
```

Once the JAR is built, you can run the project as follows:

```
## Running the Indexer to create the TF-IDF model
$ java -cp target/ir_assignment-1.0-SNAPSHOT.jar edu.bits.wilp.ir_assignment.index.Indexer
09:30:45.230 [main] INFO  e.b.wilp.ir_assignment.index.Indexer - Opening datasets/sample.csv for reading
09:30:45.250 [main] INFO  e.b.wilp.ir_assignment.index.Indexer - Files read, starting to compute DF
09:30:46.022 [main] INFO  e.b.wilp.ir_assignment.index.Indexer - DF Complete
09:30:46.022 [main] INFO  e.b.wilp.ir_assignment.index.Indexer - Starting Tf-Idf calculations
09:30:46.308 [main] INFO  e.b.wilp.ir_assignment.index.Indexer - Tf-Idf calculations, done. Writing the model file: output.bin
09:30:46.494 [main] INFO  e.b.wilp.ir_assignment.index.Indexer - Persisting the documents: documents.bin
09:30:46.503 [main] INFO  e.b.wilp.ir_assignment.index.Indexer - Indexing Complete

## Searching against this model
$ java -cp target/ir_assignment-1.0-SNAPSHOT.jar edu.bits.wilp.ir_assignment.search.Searcher "love tablet"
10:54:32.419 [main] INFO  e.b.w.ir_assignment.search.Searcher - Loading D SparseMatrix
10:54:32.512 [main] INFO  e.b.w.ir_assignment.search.Searcher - D SparseMatrix Loaded
10:54:32.538 [main] INFO  e.b.w.ir_assignment.search.Searcher - Searching for query: love table
10:54:32.605 [main] INFO  e.b.w.ir_assignment.search.Searcher - Computed Query Vectors
10:54:32.606 [main] INFO  e.b.w.ir_assignment.search.Searcher - Searching across documents
10:54:33.910 [main] INFO  e.b.w.ir_assignment.search.Searcher - Computed cosine-sim across documents
Precision: 0.254, Recall: 0.0394
{"docId":37,"cosineSim":0.47244385850822546,"document":"My children love this table great quality of pictures, excellent camera"}
{"docId":463,"cosineSim":0.40358427359665505,"document":"Love the tablet! Easy to use and easy to take with"}
{"docId":14,"cosineSim":0.403362560227578,"document":"Got it as a present and love the size of the screen"}
{"docId":570,"cosineSim":0.3494586192465874,"document":"I love the amazon tablet very much and love to use"}
{"docId":621,"cosineSim":0.34905953708265014,"document":"Love my kindle it does everything I wanted I to do."}
{"docId":981,"cosineSim":0.34898384370701396,"document":"My wife loved her gift. I made the right purchase."}
{"docId":666,"cosineSim":0.3485084614225623,"document":"bought it as a gift for my mother and she loves it"}
{"docId":625,"cosineSim":0.3482165593865239,"document":"I got this for my husband birthday and he loves it"}
{"docId":876,"cosineSim":0.3469500809960075,"document":"She loves it . it's the best thing we could of ever brought her"}
{"docId":417,"cosineSim":0.34582351290496915,"document":"Got this for my grandmother and she absolutely loves it."}
```

## Dataset Collection

In order to work with latitude and longitude data, we have implemented a custom crawler which crawls
[LatLong.net](https://www.latlong.net/countries.html) to capture all the places across all the countries along with some
more additional information. You can find the actual schema of the data at `datasets/location-info.jsonl`. The custom
crawler implementation is part of `edu.bits.wilp.ir_assignment.crawler` package.

### Crawler Implementation Notes

We've implemented a multi-threaded crawler which is entirely modelled as a producer-producer-consumer pattern. The
crawler contains two primary threads: `Fetcher` and `Writer`. The `Fetcher` is responsible for doing the actual fetches
while the `Wrtier` is responsible for persisting the final output to disk. `Fetcher` thread is backed by two queues
which provide the source of urls for `Fetcher` itself which is called `inQueue` and the other is the source of `Writer`
thread which is called `outQueue`. Since we have to navigate quite a few pages before we reach the final place urls
across different countries and we're interested only in the information from the final page and not any intermediate
pages. So `Fetcher` thread would extract all the urls and add it to `inQueue` and only the final data is written
to `outQueue` for the `Writer` thread to be persisted. Since `Fetcher` feeds itself using the `inQueue` like a recursive
function, the name `producer-producer-consumer`.

Our source of data also displays a few commonly visited places right at the top level which made our crawler go in
circles or in a recursive loop. In order to avoid falling into these type of `Spider Traps`, we use a `BloomFilter` to
keep track of all the URLs we've visited to avoid circular fetches. We decided to use BloomFilter instead of a normal
Set because of the following reasons:

- Space Efficiency - BloomFilter are highly memory efficient for storing large number of items in a smaller footprint
  where the use-cases are primarily set membership.
- Threadsafe - We use BloomFilter implementation from Guava which is thread-safe in our multi-threaded environment.
  Again using a HashSet or ConcurrentList would mean we'll have to sacrifice the performance for safety.

As part of fine-tuning the performance of the crawler, we realised the right number of threads for the source website is

Anything more brings down the website as a whole. Since we want to ignore non-200 status fetches, we decide to fail the
crawler and crash, than continue fetching invalid pages, following the let it crash principle. This meant each time we
restart the crawler we had to start over from scratch which meant the crawling was slow. In order to have resume crawl
functionality we do the following two things:

- On start-up load all the URLs from the output file into the bloom filter so we don't refetch the same pages multiple
  times
- Persist all the HTML pages we fetch into a directory and use the cached copy of the file during the next time we
  crawl. This drastically speed things up for us in order to quickly reach the spot where we left off and start crawling
  again. This also helped us iterate on our Parser safely by running through the crawled pages multiple times locally
  than re-fetching them again over the internet.

## Citations / References

- Stopwords used are from https://www.ranks.nl/stopwords. We use the very large stop word list.

<hr />

## Problem Statement

The task is to build a search engine which will cater to the needs of a particular domain. You have to feed your IR
model with documents containing information about the chosen domain. It will then process the data and build indexes.
Once this is done, the user will give a query as an input. You are supposed to return top 10 relevant documents as the
output. Your results should be explainable. The design document should clearly explain the working of the model along
with detailed explanation of any formulas that you might have used. . For Eg:

- You can build a search engine for searching song lyrics.
  Dataset: https://labrosa.ee.columbia.edu/millionsong/musixmatch
- Other domains on which you can find datasets easily are:
    - Product review - https://www.kaggle.com/datafiniti/consumer-reviews-of-amazon-products
    - Healthcare - https://www.kaggle.com/xhlulu/medal-emnlp
    - Finance
    - Automobiles

You may also choose your own domain and [crawl data from the web](https://www.wikiwand.com/en/Web_scraping) using the
crawler described below in the additional resources. You are free to use your
own [web crawler](https://www.analyticsvidhya.com/blog/2015/10/beginner-guide-web-scraping-beautiful-soup-python/).
