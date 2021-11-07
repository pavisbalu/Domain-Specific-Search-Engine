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
09:30:25.380 [main] INFO  e.b.w.ir_assignment.search.Searcher - Loading D SparseMatrix
09:30:25.470 [main] INFO  e.b.w.ir_assignment.search.Searcher - D SparseMatrix Loaded
09:30:25.497 [main] INFO  e.b.w.ir_assignment.search.Searcher - Searching for query: love tablet
09:30:25.543 [main] INFO  e.b.w.ir_assignment.search.Searcher - Computed Query Vectors
09:30:25.543 [main] INFO  e.b.w.ir_assignment.search.Searcher - Searching across documents
09:30:26.453 [main] INFO  e.b.w.ir_assignment.search.Searcher - Computed cosine-sim across documents
OutputRank{docId=463, cosineSim=0.816370774647198, document='Love the tablet! Easy to use and easy to take with'}
OutputRank{docId=570, cosineSim=0.7069005655369549, document='I love the amazon tablet very much and love to use'}
OutputRank{docId=137, cosineSim=0.631736058859506, document='Good tablet. Wife loves it and would recommend....'}
OutputRank{docId=231, cosineSim=0.6308758405740864, document='I got this tablet on sale for my wife and she loves it'}
OutputRank{docId=698, cosineSim=0.618752056932315, document='We have had no issues with this tablet. Love it!TY'}
OutputRank{docId=761, cosineSim=0.5772570526908305, document='I love to read and this tablet work great for my needs.'}
OutputRank{docId=680, cosineSim=0.5771066450965584, document='This is a great tablet with great features. I love my kindle.'}
OutputRank{docId=566, cosineSim=0.5768396132505973, document='great tablet to replace kindle. Love the features.'}
OutputRank{docId=755, cosineSim=0.5764721594546459, document='No problems with tablet, kids love it and the size is great'}
OutputRank{docId=508, cosineSim=0.5761544749214773, document='My daughter love this tablet! Easy to use and carry'}
```

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
