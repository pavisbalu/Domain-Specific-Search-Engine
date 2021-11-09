# Group A1 - IR Assignment

Live Demo: https://pavisbalu.github.io/maps-search/

API is hosted on Heroku and App is hosted on Github Pages.

<hr />

<!-- vscode-markdown-toc -->
* 1. [Usage](#Usage)
* 2. [Dataset Collection](#DatasetCollection)
	* 2.1. [Crawler Implementation Notes](#CrawlerImplementationNotes)
* 3. [Indexing](#Indexing)
* 4. [Searching](#Searching)
	* 4.1. [Custom SparseVector Implementation](#CustomSparseVectorImplementation)
* 5. [Citations / References](#CitationsReferences)
* 6. [Problem Statement](#ProblemStatement)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='Usage'></a>Usage

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
17:34:20.251 [main] INFO  e.b.w.ir_assignment.search.Searcher - Loading D SparseMatrix
17:34:49.619 [main] INFO  e.b.w.ir_assignment.search.Searcher - D SparseMatrix Loaded
17:34:49.682 [main] INFO  e.b.w.ir_assignment.search.Searcher - Searching for query: india
17:34:49.949 [main] INFO  e.b.w.ir_assignment.search.Searcher - Computed Query Vectors
17:34:50.017 [main] INFO  e.b.w.ir_assignment.search.Searcher - Searching across documents: 20795
17:34:50.969 [main] INFO  e.b.w.ir_assignment.search.Searcher - Computed cosine-sim across documents
Precision: 0.3041, Recall: 0.0016
{"docId":6789,"cosineSim":0.7064057272235196,"document":{"indexedFields":{"title":"Haryana, India","description":"Haryana is one of the states of India and located in the north-central part of the country. It is bordered by Punjab, Chandigarh, Himachal Pradesh, Uttarakhand, Uttar Pradesh, Delhi, and Rajasthan. The state has divided into 22 districts and has 6 administrative divisions, 72 sub-divisions. Chandigarh is the capital city of the two neighboring states of Punjab and Haryana. The state is one of the wealthiest ones in the country, with quite high per capita incomes and a good number of large businesses. Its economy is based on agriculture, car manufacturing industry, real estate, transport, financial and professional services. Haryana is home to the headquarters of DLF Limited which is the largest real estate company in India. The state has also various Medical Colleges such as ESIC Medical College, Pandit Bhagwat Dayal Sharma Post Graduate Institute of Medical Sciences Rohtak, as well as many universities and colleges including Chaudhary Charan Singh Haryana Agricultural University, Guru Jambheshwar University of Science and Technology, and Maharaja Agrasen Medical College, Agroha. There are plenty of national parks, wildlife sanctuaries, and protected areas in Haryana, with very diverse and unique fauna and flora. Attractions and points of interest in Haryana include Zakir Rose Garden (ਜ਼ਾਕਿਰ ਰੋਜ਼ ਗਾਰਡਨ (ਚੰਡੀਗੜ੍ਹ), Chandigarh Botanical Garden (ਚੰਡੀਗੜ੍ਹ ਬੋਟੈਨੀਕਲ ਬਾਗ), Garden of Fragrance (ਗਾਰਡਨ ਦਾ ਫਰੇਗਰੈਂਸ), Garden of Silence (ਗਾਰਡਨ ਦਾ ਸਾਈਲੈਂਸ), Government Museum and Art Gallery (ਸਰਕਾਰੀ ਮਿਊਜ਼ੀਅਮ ਅਤੇ ਆਰਟ ਗੈਲਰੀ), Tau Devi Lal Town Park (ताउ देवी लाल टाउन पार्क), Tau Devi Lal Sports Complex (ताउ देवी लाल स्पोर्ट्स कॉम्पलेक्स), Sheetla Mata Mandir (शीतला माता मंदिर), Kingdom of Dreams (किंगडम ऑफ़ ड्रीम्स), SkyJumper Trampoline Park Gurgaon (स्काई जम्पर ट्रैम्पोलिन पार्क गुरुग्राम), Subhash Chandra Bose Park (सुभाष चंद्र बोस पार्क), Leisure Valley Park (लैज़र वैली पार्क), Sai Ka Angan Temple (साईं का आंगन), DLF Cyber Hub (डीएलएफ साइबरहूब), Heritage Transport Museum (हेरिटेज ट्रांसपोर्ट म्यूज़ियम), Gurudwara Nada Sahib (गुरुद्वारा नाढा साहिब), Shri Maharani Vaishno Devi Mandir (श्री महाराणी वैष्णो देवी मंदिर. तिकोना पार्क), and many more."},"nonIndexedFields":{"country":"India","zoom level":"7","latitude":"29.065773","country code":"IN","utm northing":"3,215,719.55","category":"States","dms lat":"29° 3' 56.7828'' N","dms long":"76° 2' 25.7892'' E","url":"https://www.latlong.net/place/haryana-india-15611.html","longitude":"76.040497","utm easting":"601,283.08"}}}
{"docId":2109,"cosineSim":0.7062861671009687,"document":{"indexedFields":{"title":"Karnataka, India","description":"Karnataka is one of the states in India located in the western part of the country, on the shores of the Arabian Sea. It is bordered by Telangana to the east, the Arabian Sea to the west, Goa and Maharashtra to the north, Kerala to the south, and Tamil Nadu to the southeast. The state has 3 geographical regions: the coastal area, the highlands, and the plains of the Deccan plateau. The capital of the state is Bangalore city that is located close to the southeastern border. The state has a large number of educational institutions such as Bangalore University, the Indian Institute of Science, and the University of Agricultural Sciences. The official language of the state is Kannada. The state has a long history and plenty of interesting tourism spots including wonderful world-famous resorts and historic monuments. The Gommateshwara statue located on Vindyagiri at Shravanbelagola is known as the tallest monolithic statue in the world. Other attractions and points of interest in Karnataka are Mysore Palace, Pattadakal, Gommateshwara Statue, Shivanasamudra Falls, Gol Gumbaz, Virupaksha Temple in Hampi, the Kollur Mookambika Temple, the Marikamba Temple, Udupi Sri Krishna Temple, M. Chinnaswamy Stadium in Bangalore, Kudremukh National Park, Bandipura National Park, Bannerghatta National Park, Chennakesava Temple, Kukke Subramanya Temple, Gol Gumbaz at Bijapur, Jog Falls, Unchalli Falls, Abbey Falls, Gokak Falls, The Shettihalli Rosary Church, and many more."},"nonIndexedFields":{"country":"India","zoom level":"7","latitude":"15.317277","country code":"IN","utm northing":"1,693,544.85","category":"States","dms lat":"15° 19' 2.1972'' N","dms long":"75° 42' 50.0040'' E","url":"https://www.latlong.net/place/karnataka-india-5053.html","longitude":"75.713890","utm easting":"576,635.89"}}}
{"docId":2615,"cosineSim":0.7062556326737265,"document":{"indexedFields":{"title":"Maharashtra, India","description":"Maharashtra is a large state which can be found in the west central India, in the north western part of the Indian Subcontinent. It has the area of about 120 thousand square miles and is the state where the large cities of Mumbai, Nashik and Pune are located. The shores of the Indian Ocean form the western borders of the state. To the south, Maharashtra shares borders with the state of Goa and Telangana, to the east it has borders with the state of Chhattisgarh, and to the north Maharashtra has borders with the states of Gujarat and Madhya Pradesh. Mumbai (former Bombay) is the largest and the capital city of the state, as well as a large port and one of the largest metropolitan areas not just in India but on the whole continent. The state of Maharashtra has one of the longest coastlines estimated to be about 450 miles long. The main part of the state occupies the Deccan plateau, with plenty of forests and excellent fertile lands. A mountain range known and the Ghats is located in the southern and the eastern parts of the state. There are a few rives crossing the state, including the rivers of Godavari, Bhima, Krishna, Tapi-Purna, and others. The largest natural parks of the state include Raigad Fort Natural Reserve, Koyna Wildlife Sanctuary, Shri Bhimashankar Jyotirlinga Wildlife Reserve, Tungareshwar National Park, Kalsubai Harichandragad Wildlife Sanctury, and others. Maharashtra is a key economic contributor and industrial region of the country, and this makes the state one of the wealthiest and the most developed among the other states."},"nonIndexedFields":{"country":"India","zoom level":"5","latitude":"19.663280","country code":"IN","utm northing":"2,174,248.33","category":"States","dms lat":"19° 39' 47.8080'' N","dms long":"75° 18' 1.0548'' E","url":"https://www.latlong.net/place/maharashtra-india-6206.html","longitude":"75.300293","utm easting":"531,478.58"}}}
{"docId":311,"cosineSim":0.706248873116888,"document":{"indexedFields":{"title":"Assam, India","description":"Assam is a northeastern state of India, bordering with Bhutan and other northern states of the country known as Seven Sisters State. Assam is located near the Himalayas and has absolutely amazing natural views, with plenty of valleys, rivers, creeks, waterfalls, and so on. It is bordered by the states of Arunachal Pradesh, Nagaland, Manipur, Mizoram, Tripura, Meghalaya, as well as Bangladesh, Bhutan, and West Bengal. The official language of the state is Assamese that is the eastern Indo-Aryan language. Assam is home to Jorhat Engineering College that was founded in 1960. The state is also home to Assam University, Cotton University, Assam Agricultural University, Assam Don Bosco University, National Institute of Technology, and Indian Institute of Technology Guwahati. The state's economy is mainly based on wildlife tourism, oil, and agriculture. Kaziranga National Park and Manas National Park are World Heritage Sites. The capital city of Assam is Dispur since 1973. It is known for The Guwahati Tea Auction Centre (GTAC) which is one of the busiest tea trading facilities in the world. Assam is known for its wildlife sanctuaries, ancient temples, large tea plantations, a special local brand of silk, and amazing rare fauna, including some rare species of elephants living in their natural habitat. There are many attractions in the state such as Kaziranga National Park, Kakochang Waterfalls, the Manas Wildlife Sanctuary, Shaiva temple in Sibsagar, Ahom palace at Gargaon, Dibru-Saikhowa National Park, Madan Kamdev ruins, Kamakhya Temple, Basistha Temple, Majuli Island, Hoollongapar Gibbon Wildlife Sanctuary, Tocklai Tea Research Centre, Orang National Park, Guwahati Planetarium, Nameri National Park, Panimoor Falls, and many more."},"nonIndexedFields":{"country":"India","zoom level":"6","latitude":"26.244156","country code":"IN","utm northing":"2,902,805.35","category":"States","dms lat":"26° 14' 38.9616'' N","dms long":"92° 32' 16.2312'' E","url":"https://www.latlong.net/place/assam-india-6684.html","longitude":"92.537842","utm easting":"453,844.03"}}}
{"docId":3617,"cosineSim":0.7062389526076865,"document":{"indexedFields":{"title":"Rajasthan, India","description":"Rajasthan, the largest state of India, is located in the northwestern region of the country, right at the border with Pakistan. It covers 10 per cent of the country's territory and has the total area of about 132 thousand square miles. The city of Jaipur, located in the northeastern part of the state, is the capital of Rajasthan, and other large cities of the state include Bikaner, Ajmer, Kota, Udaipur, Jodhpur, and some others. The capital city of the state is situated quite close to the capital city of the country, New Delhi, and the distance between the two cities is close to just 160 miles. The city of Agra, the home to the famous historic monument of India Taj Mahal, can be found about 180 miles east of the city of Jaipur. The state of Rajasthan has borders with 5 states of the country, Punjab, Haryana, Uttar Pradesh, Madhya Pradesh, and Gujarat. Known as the Land of Kings, the state is the place where the largest desert of the country, the Great Indian Desert, or the Thar Desert, is situated. Some part of the state is occupied by the Aravalli Mountain Range, with the top of Mount Abu being the highest point of the state. There are also some green areas covered by fertile soils and natural preserves, mainly the parts of the Northwestern thorn scrub forests which are famous in India. A few rivers like the Luni River or the Ghaggar River are crossing the state from its eastern parts (with the higher elevation) to the southern or western parts of Rajasthan. There are a few natural parks in the state like Sariska Tiger Reserve or the Ranthambore National Park famous for their large varieties of wild animals living there in their natural living environment."},"nonIndexedFields":{"country":"India","zoom level":"5","latitude":"27.391277","country code":"IN","utm northing":"3,030,749.37","category":"States","dms lat":"27° 23' 28.5972'' N","dms long":"73° 25' 57.4212'' E","url":"https://www.latlong.net/place/rajasthan-india-1370.html","longitude":"73.432617","utm easting":"345,022.42"}}}
{"docId":6788,"cosineSim":0.7062167662194739,"document":{"indexedFields":{"title":"Chhattisgarh, India","description":"Chhattisgarh is a state located in east-central India, bordered by Odisha, Telangana, Maharashtra, Madhya Pradesh, and states of Uttar Pradesh and Jharkhand. It is one of the largest and the fastest-developing states in the country, it appeared on the map of India only in the year 2000. There are 5 divisions with 28 districts in the state, and the economy of Chhattisgarh bases mainly on agriculture, mining industry, tea production, and information technology (IT). Raipur is the capital and the largest city of the state, as well as one of the important industrial centers of India. Swami Vivekananda Airport is the major airport serving the state. It is situated at Mana between Raipur and Naya Raipur. Its main attractions include mostly natural beauties like Chitrakote Falls, Charre Marre Waterfalls, Ghatarani Waterfalls, Tirathgarh Falls, Kotumsar Cave, Jatmai Mandir at Gariaband, Abujmarh, Achanakmar Wildlife Sanctuary, Amarkantak Hill Range, Guru Ghasidas (Sanjay) National Park, Kanger Ghati National Park, Barnawapara Wildlife Sanctuary, Indravati National Park, along with temples and Buddhist sites. Other landmarks and points of interest in the state are Lakshmana Temple, Bhoramdeo Temple, Swami Vivekanand Sarovar, Madku Dweep, Balbir Singh Juneja Indoor Stadium, Sardar Vallabh Bhai Patel International Hockey Stadium, etc."},"nonIndexedFields":{"country":"India","zoom level":"6","latitude":"21.295132","country code":"IN","utm northing":"2,355,036.79","category":"States","dms lat":"21° 17' 42.4752'' N","dms long":"81° 49' 41.6352'' E","url":"https://www.latlong.net/place/chhattisgarh-india-16504.html","longitude":"81.828232","utm easting":"585,909.04"}}}
{"docId":4429,"cosineSim":0.7062125124877221,"document":{"indexedFields":{"title":"Telangana, India","description":"Telangana is one of the largest states of India, located in the central part of the Indian subcontinent. Telangana is bordered by the state of Maharashtra to the north, by the state of Chhattisgarh to the east, by the state of Andhra Pradesh to the south, and by the state of Karnataka to the west. Telangana has the area of about 43 thousand square miles which makes the state among the country's top 12 largest states. It is a center of Telugu culture, which started developing very rapidly after the country's gaining independence. The capital city of Telangana is Heydarabad. The state is an important agricultural center, with plenty of tobacco and vegetable producing companies established there. The city has a very rich culture and plenty of interesting old traditions. Most of the area of the state lay on the Deccan Plateau which is located in the central part of the peninsula. Telangana is known for its excellent arable lands which receive plenty of water from the two principal rivers of the region, the river of Krishna and the river of Godavari. Other smaller rivers of the state include the ones of Manjira, Bhima, Maner, Musi, and others. Despite having quite enough of hydration, the state has not forests and large natural preserves. The largest cities of Telangana are the cities of Nizamabad, Nirmal, Armoor, Nalgonda, Khammam, Suryapet, Warangal, Mancherial, Siddipet, and many others. The state, its history and landmarks like Chowmahalla Palace, Paigah Tombs, Warangal Fort, Thousand Pillar Temple, Qutb Shahi Tombs, and others are of an extreme importance for the country's culture and heritage."},"nonIndexedFields":{"country":"India","zoom level":"5","latitude":"17.123184","country code":"IN","utm northing":"1,894,059.54","category":"States","dms lat":"17° 7' 23.4624'' N","dms long":"79° 12' 31.7664'' E","url":"https://www.latlong.net/place/telangana-india-21875.html","longitude":"79.208824","utm easting":"309,440.59"}}}
{"docId":1622,"cosineSim":0.7061621510597391,"document":{"indexedFields":{"title":"Gujarat, India","description":"Gujarat is one of the Indian states located in the northwestern part of the country, near the border between India and Pakistan. This is the 7th largest and the westernmost state of India. Gujarat has the area close to 75.5 thousand square miles and it is also the state with the longest coastline which is about 980 miles in length. The southern and southwestern parts of Gujarat are overlooking the waters of the Arabian Sea, the northern parts of the state have borders with Pakistan and the state of Rajasthan, at the same time to the east Gujarat shares borders with the states of Madhya Pradesh and Maharashtra. The city of Gandhinagar located in the northeastern part of the state is the capital of the state, and the city of Ahmedabad, which can be found just 20 miles south east of the capital city, is the largest settlement of Gujarat. The state of Gujarat is the location of the country's 41 ports, with more than a half of them being really large and important ones for the whole region. There are also a few rivers flowing through the state, including the River Sabarmati that passes through the capital city and Ahmedabad. The cities of Vadodara, Surat, Rajkot, Jamnagar, Junagadh, Amreli, and others are the largest settlements of Gujarat. The area of the state is mainly covered by lowlands and there are a good number of small and medium lakes can be found in the north and northeastern parts of the state. The highland areas of the state include the hills of the Western Ghats, the Aravalli Range, the Vindhya Range, and a few more. Gir Forest National Park located in the northern part of the state is one of the largest natural parks in India where rare species of Asiatic lions live in their natural habitat."},"nonIndexedFields":{"country":"India","zoom level":"5","latitude":"22.309425","country code":"IN","utm northing":"2,469,877.24","category":"States","dms lat":"22° 18' 33.9300'' N","dms long":"72° 8' 10.4280'' E","url":"https://www.latlong.net/place/gujarat-india-2235.html","longitude":"72.136230","utm easting":"204,956.76"}}}
{"docId":2142,"cosineSim":0.7061198645547964,"document":{"indexedFields":{"title":"Kerala, India","description":"Kerala is one of the southern states of India, located on the western edge of the Indian subcontinent. Kerala is bordered by the state of Tamil Nadu to the east, the state of Kerala to the north, and the waters of the Arabian Sea to the west and to the north. The city of Thiruvananthapuram is the capital and the largest city of the state, which can be found in the southern edge of the state and the peninsula. The total area of Kerala is over 15 thousand square miles and administratively the state is divided into 14 divisions, which are further sub-divided into smaller districts. The largest cities of the state of Kerala are the ones of Thiruvalla, Changanassery, Alappuzha, Kozhikode, Kannur, Kasaragod, Payyanur, Vatakara, Palakkad, and others. Due to its location close to the sea, Kerala is a local resort and the state famous for its beaches, hotels and various seaside vacation facilities. Just like the most popular Indian resort area, the state of Goa, which can be found in the north of Karnataka, the southern part of the country has plenty of nice beaches and modern facilities for water-sports. As a very large and famous resort area, Kerala is a destination for local tourists which is famous for a great deal of wonderful hotels, amusement parks, lagoons, harbors, and so on. The coastal cities of the state attract millions of tourists from around the region. In addition to that, Kerala is a center of Malayalam culture, traditions, customs, arts and dance, cinema and cuisine, lifestyle and social attitude, etc."},"nonIndexedFields":{"country":"India","zoom level":"6","latitude":"10.850516","country code":"IN","utm northing":"1,199,741.15","category":"States","dms lat":"10° 51' 1.8576'' N","dms long":"76° 16' 15.8880'' E","url":"https://www.latlong.net/place/kerala-india-7331.html","longitude":"76.271080","utm easting":"638,937.82"}}}
{"docId":3078,"cosineSim":0.7060182777803725,"document":{"indexedFields":{"title":"New Delhi, Delhi, India","description":"One of the most crowded cities of the world, New Delhi, or simply Delhi, is the capital of India and is the largest city of the country as well. It is located close to the geographical center of the country and is a key cultural and tourist center of India, with a huge number of temples, palaces, modern shopping malls and office centers, stadiums, parks and numerous recreational facilities that can be found there. Just like most of the large cities of the country, New Delhi has a very long history and, as the capital city, it played a key role in all the latest developments and historical events related to independence gaining and modern progress of the city. In contrast to a common idea, New Delhi is not a home to the Taj Mahal, but there is a large amount of amazing landmarks like the amazing Lotus Temple, the Akshardham Temple, the Rashtrapati Bhavan, the Secretariat Building of the Country, the National Museum, the Rajpath, Gurudwara Bangla Sahib, Laxminarayan Temple, the Sacred Heart Cathedral, and so on. The city is the key center of transportation, entertainment, sports, and cultural life of the country. It is home to the people of different kinds of religions and origins, with most of them living happily side by side."},"nonIndexedFields":{"country":"India","zoom level":"10","latitude":"28.644800","country code":"IN","utm northing":"3,170,643.46","category":"Cities","dms lat":"28° 38' 41.2800'' N","dms long":"77° 13' 0.1956'' E","url":"https://www.latlong.net/place/new-delhi-delhi-india-2441.html","longitude":"77.216721","utm easting":"716,671.89"}}}
```

# Implementation Notes

##  2. <a name='DatasetCollection'></a>Dataset Collection

In order to work with latitude and longitude data, we have implemented a custom crawler which crawls
[LatLong.net](https://www.latlong.net/countries.html) to capture all the places across all the countries along with some
more additional information. You can find the actual schema of the data at `datasets/location-info.jsonl`. The custom
crawler implementation is part of `edu.bits.wilp.ir_assignment.crawler` package.

###  2.1. <a name='CrawlerImplementationNotes'></a>Crawler Implementation Notes

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

##  3. <a name='Indexing'></a>Indexing

Indexing is the process of converting the raw documents to TF-IDF Vectorised model so we can search across these
documents faster. Our indexing is available in `edu.bits.wilp.ir_assignment.index` package. Most of our TF, DF, IDF and
TF-IDF implementation was inspired
from [this blog post](https://towardsdatascience.com/tf-idf-for-document-ranking-from-scratch-in-python-on-real-world-dataset-796d339a4089)
. We have implemented indexing on different fields of the same document using custom weights. For example, from our
location dataset that we captured above, we have highest weight of 75% to title while the description tf-idf scores
consitutue only 25% of the total score. In other words, tokens from title would have higher ranks compared to tokens
from description.

##  4. <a name='Searching'></a>Searching

Searching is the process of using the model generated from [Indexing](#3-indexing) step to find relevant documents. We
use [Cosine Similarity](https://en.wikipedia.org/wiki/Cosine_similarity) to find relevancy between the given query and a
document and return the top K results. Currently in our implementation K is set to _10_.

###  4.1. <a name='CustomSparseVectorImplementation'></a>Custom SparseVector Implementation

While we were working with a very small dataset (100 documents) using 2D arrays worked well. When we started working
with the entire dataset of 20K documents and ~750K terms the program started crashing with Heap Size issue because of
the very sparse nature of the document term relationship. In other words the description and title contained almost
unique terms which was very specific to the document and don't repeat that often across documents. That is the very
nature of the dataset we're working with.

In order to handle this scenario and exploit the sparse relationship nature between terms and documents, we had to
implement a custom SparseMatrix and SparseVector implementations for our cosine-sim calculations. We started of using
the [SparseFieldMatrix](https://commons.apache.org/proper/commons-math/javadocs/api-3.6.1/org/apache/commons/math3/linear/SparseFieldMatrix.html)
from [Apache commons-math](https://commons.apache.org/proper/commons-math/). While the in-memory representation was
great getting individual row vector (for a document) was so slow and taking very long time. We took inspiration from the
existing implementation and modified a few things that we wanted to make things super fast. Also methods like `norm()`
or `squared()` was not supported in their Vector implementations which we did. This significantly reduced our search
time from 10+ minutes for a single query to < 300ms which was a huge motivation for us to build the API and the App
because we could search the dataset in realistic amount of time.

##  5. <a name='CitationsReferences'></a>Citations / References

- Stopwords used are from https://www.ranks.nl/stopwords. We use the very large stop word list.
- [Reference implementation in Python for inspiration](https://towardsdatascience.com/tf-idf-for-document-ranking-from-scratch-in-python-on-real-world-dataset-796d339a4089)

<hr />

##  6. <a name='ProblemStatement'></a>Problem Statement

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
