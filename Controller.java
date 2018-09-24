package csci572hw2;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import org.apache.tika.io.FilenameUtils;

public class Controller {
	private final static String FETCH_NEWSSITE_PATH = "/eclipse-workspace/csci572hw2/src/csci572hw2/fetch_NewsSite.csv";
    private final static String VISIT_NEWSSITE_PATH = "/eclipse-workspace/csci572hw2/src/csci572hw2/visit_NewsSite.csv";
    private final static String URLS_NEWSSITE_PATH = "/eclipse-workspace/csci572hw2/src/csci572hw2/urls_NewsSite.csv";

	public static void main(String[] args) throws Exception {
		// crawler4jl parameter
		String crawlStorageFolder = "~/Projects/csci572hw2/data/crawl";
		int numberOfCrawlers = 7;
		int maxDepthOfCrawling = 16;
		int maxPagesToFetch	= 100;
		
		CrawlConfig config  = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		config.setMaxPagesToFetch(maxPagesToFetch);
		config.setIncludeBinaryContentInCrawling(true);
		/* Instantiate the controller for crawl */
		
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		
		
		controller.addSeed("https://www.chron.com");
		controller.start(MyCrawler.class, numberOfCrawlers);
		
		MyCrawlStat finalCrawlstat = new MyCrawlStat();
		List<Object> myCrawlStat = controller.getCrawlersLocalData();
		for(Object localData : myCrawlStat) {
			MyCrawlStat crawlStat = (MyCrawlStat) localData;
			finalCrawlstat.fetchAttemps = finalCrawlstat.fetchAttemps + crawlStat.fetchAttemps;
			finalCrawlstat.fetchSuccessd = finalCrawlstat.fetchSuccessd + crawlStat.fetchSuccessd;
			finalCrawlstat.fetchFailedOrAborted = finalCrawlstat.fetchFailedOrAborted + crawlStat.fetchFailedOrAborted;
			finalCrawlstat.totalURLsExtracted = finalCrawlstat.totalURLsExtracted + crawlStat.totalURLsExtracted;
			finalCrawlstat.uniqueURLsExtracted = finalCrawlstat.uniqueURLsExtracted + crawlStat.uniqueURLsExtracted;
			finalCrawlstat.uniqueUrlsOutside = finalCrawlstat.uniqueUrlsOutside + crawlStat.uniqueUrlsOutside;
			finalCrawlstat.uniqueURlsWithin = finalCrawlstat.uniqueURlsWithin + crawlStat.uniqueURlsWithin;
			finalCrawlstat.fetchNewsSite.addAll(crawlStat.fetchNewsSite);
			finalCrawlstat.visitNewsSite.addAll(crawlStat.visitNewsSite);
			finalCrawlstat.urlsNewsSite.addAll(crawlStat.urlsNewsSite);
			
			// update statusCodes
			for(Map.Entry<String, Integer> entry : crawlStat.statusCodes.entrySet()) {
				String key = entry.getKey();
				int value = entry.getValue();
				if(finalCrawlstat.statusCodes.get(key) == null) {
					finalCrawlstat.statusCodes.put(key, value);
				}
				else {
					finalCrawlstat.statusCodes.put(key, finalCrawlstat.statusCodes.get(key) + value);
				}
			}
			// update encountered urls
			for(Map.Entry<String, Boolean> entry : crawlStat.urlEncountered.entrySet()) {
				String key = entry.getKey();
				Boolean value = entry.getValue();
				finalCrawlstat.urlEncountered.put(key, value);
			}
			// update encountered content types
			for(Map.Entry<String, Integer> entry : crawlStat.contentTypeEncountered.entrySet()) {
				String key = entry.getKey();
				int value = entry.getValue();
				if(finalCrawlstat.contentTypeEncountered.get(key) == null) {
					finalCrawlstat.contentTypeEncountered.put(key, value);
				}
				else {
					finalCrawlstat.contentTypeEncountered.put(key, finalCrawlstat.contentTypeEncountered.get(key) + value);
				}
			}
		}
		
		// write fetch_NewsSite.csv
		String normalize_path = FilenameUtils.normalize(System.getProperty("user.home") + FETCH_NEWSSITE_PATH);
		File newFile = new File(normalize_path);
		if(newFile.exists()){
			newFile.delete();
		}
		newFile.createNewFile();
		
		CSVWriter writer = new CSVWriter(new FileWriter(normalize_path));
		String[] fetchNewsSiteHeader = { "URl", "Http Status" };
		writer.writeNext(fetchNewsSiteHeader);
		for(MyFetchNewsSite fetchNewsSite : finalCrawlstat.fetchNewsSite) {
			String[] temp = {fetchNewsSite.url, Integer.toString(fetchNewsSite.statusCode)};
			writer.writeNext(temp);
		}
		writer.close();
		System.out.println("fetch_NewsSite.csv created, total row is " + Integer.toString(finalCrawlstat.fetchNewsSite.size() + 1));
		
		// write visit_NewsSite.csv
		normalize_path = FilenameUtils.normalize(System.getProperty("user.home") + VISIT_NEWSSITE_PATH);
		newFile = new File(normalize_path);
		if(newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		
		writer = new CSVWriter(new FileWriter(normalize_path));
		String[] visitNewsSiteHeader = { "URL", "File Size", "Outlinks", "Content Tyoe" };
		writer.writeNext(visitNewsSiteHeader);
		for(MyVisitNewsSite visitNewsSite : finalCrawlstat.visitNewsSite) {
			String[] temp = {visitNewsSite.url, Integer.toString(visitNewsSite.file_size), Integer.toString(visitNewsSite.outlinks), visitNewsSite.content_type};
			writer.writeNext(temp);
		}
		writer.close();
		System.out.println("visit_NewsSite.csv created, total row is " + Integer.toString(finalCrawlstat.visitNewsSite.size() + 1));
		
		//write urls_NewsSite.csv
		normalize_path = FilenameUtils.normalize(System.getProperty("user.home") + URLS_NEWSSITE_PATH);
		newFile = new File(normalize_path);
		if(newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		
		writer = new CSVWriter(new FileWriter(normalize_path));
		String[] urlNewsSiteHeader = { "URL", "Resides" };
		writer.writeNext(urlNewsSiteHeader);
		for(MyUrlsNewsSite myUrlsNewsSite : finalCrawlstat.urlsNewsSite) {
			String[] temp = { myUrlsNewsSite.url, myUrlsNewsSite.isResides ? "OK" : "N_OK" };
			writer.writeNext(temp);
		}
		writer.close();
		System.out.println("url_NewsSites.csv created, total row is " + Integer.toString(finalCrawlstat.visitNewsSite.size() + 1));
	}
}
