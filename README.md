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

The `sample.csv` that's part of the source code contains the top 1000 records from the [Amazon Product Review Dataset](https://www.kaggle.com/datafiniti/consumer-reviews-of-amazon-products).

## Citations / References
- Stopwords used are from https://www.ranks.nl/stopwords. We use the very large stop word list.

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
