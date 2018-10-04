
# crawler4j-exercise
A web crawler practice for wsj.com based on Crawler4j

## Dependencies
- [Opencsv-4.2](http://opencsv.sourceforge.net/)
- [Crawler4j-4.1](https://github.com/yasserg/crawler4j)
- [slf4j-simple-1.7.9](https://mvnrepository.com/artifact/org.slf4j/slf4j-simple/1.7.9)

## Description
This java code is for crawling 200,000 pages within wsj.com. Pages that are html, png, jpeg, gif, doc and pdf, if can be accessed, 
would be crawled but not downloaded.

The number of crawlers(threads) and politnessDelay are set to 7 and 200 respectively for not hitting the server too much.

When crawling finish, these csv files would be created:

1. fetch_NewsSite.csv
   - URLs
   - Status code
2. visit_NewsSite.csv
   - URLs
   - File size
   - Outgoing links
   - Content type
3. urls_NewsSite.csv
   - URLs
   - Within target domain
   
And a txt file would be created too with some statistics

## Some Notes
I am kind of confused when I trying to use HttpURLConnection in overrided shouldVisit() function to fetch the header before visit
the actual page, I did get header of page that should (not) be visited, but the crawler4j won't go into visit() function 
even if that URL is exactly what I want to crawl. Hmm interesting...
