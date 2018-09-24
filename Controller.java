package csci572hw2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private final static String CRAWLREPORT_NEWSSITE_PATH = "/eclipse-workspace/csci572hw2/src/csci572hw2/CrawlReport_NewsSite.txt";
    
    // set web URL to crawl
    private final static String TARGET_WEBSITE = "https://www.chron.com";
    
    private static void writeCrawlReportNewsSite(String normalizedPath, MyCrawlStat myCrawlStat) {
    	try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(normalizedPath))){
    		 // write general info
			 bWriter.write("Name: Tommy Trojan");
			 bWriter.newLine();
			 bWriter.write("USC ID: ");
			 bWriter.newLine();
			 bWriter.write("News site crawled: " + TARGET_WEBSITE);
			 bWriter.newLine();
			 bWriter.newLine();
    		 
			 // write fetch statistics
			 bWriter.write("Fetch Statistics:");
			 bWriter.newLine();
			 bWriter.write("=================");
			 bWriter.newLine();
			 bWriter.write("# fetches attempted: " + Integer.toString(myCrawlStat.fetchAttemps));
			 bWriter.newLine();
			 bWriter.write("# fetches succeeded: " + Integer.toString(myCrawlStat.fetchSuccessd));
			 bWriter.newLine();
			 bWriter.write("# fetches failed or aborted: " + Integer.toString(myCrawlStat.fetchFailedOrAborted));
			 bWriter.newLine();
			 bWriter.newLine();
			 
			 // write outgoing URLs
			 bWriter.write("Outgoing URLs:");
			 bWriter.newLine();
			 bWriter.write("==============");
			 bWriter.newLine();
			 bWriter.write("Total URLs extracted: " + Integer.toString(myCrawlStat.totalURLsExtracted));
			 bWriter.newLine();
			 bWriter.write("# unique URLs extracted: " + Integer.toString(myCrawlStat.uniqueURLsExtracted));
			 bWriter.newLine();
			 bWriter.write("# unique URLs within News Site: " + Integer.toString(myCrawlStat.uniqueURlsWithin));
			 bWriter.newLine();
			 bWriter.newLine();
			 
			 // write status codes
			 bWriter.write("Status Codes:");
			 bWriter.newLine();
			 bWriter.write("=============");
			 bWriter.newLine();
			 for(Map.Entry<String, Integer> entry : myCrawlStat.statusCodes.entrySet()) {
				 bWriter.write(entry.getKey() + ": " + entry.getValue());
				 bWriter.newLine();
			 }
			 bWriter.newLine();
			 
			 // write file sizes
			 bWriter.write("File Sizes:");
			 bWriter.newLine();
			 bWriter.write("===========");
			 bWriter.newLine();
			 // TODO write file size
			 
			 bWriter.newLine();
			 
			 // Write content types
			 bWriter.write("Content Types:");
			 bWriter.newLine();
			 bWriter.write("==============");
			 bWriter.newLine();
			 for(Map.Entry<String, Integer> entry : myCrawlStat.contentTypeEncountered.entrySet()) {
				 bWriter.write(entry.getKey() + ": " + entry.getValue());
				 bWriter.newLine();
			 }
			 
			 System.out.println("CrawlReport.txt completed");
			 
    	}catch (IOException e) {
			System.out.println(e.getMessage());
		};
    }

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
		
		
		controller.addSeed(TARGET_WEBSITE);
		controller.start(MyCrawler.class, numberOfCrawlers);
		
		// work with statistic status
		MyCrawlStat finalCrawlstat = new MyCrawlStat();
		List<Object> myCrawlStat = controller.getCrawlersLocalData();
		// merge all statistic from each crawlers
		for(Object localData : myCrawlStat) {
			// merge number and list
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
			
			// merge statusCodes
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
			// merge encountered urls
			for(Map.Entry<String, Boolean> entry : crawlStat.urlEncountered.entrySet()) {
				String key = entry.getKey();
				Boolean value = entry.getValue();
				finalCrawlstat.urlEncountered.put(key, value);
			}
			// merge encountered content types
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
		System.out.println("Start creating csv files");
		String normalizedPath = FilenameUtils.normalize(System.getProperty("user.home") + FETCH_NEWSSITE_PATH);
		File newFile = new File(normalizedPath);
		if(newFile.exists()){
			newFile.delete();
		}
		newFile.createNewFile();
		
		CSVWriter writer = new CSVWriter(new FileWriter(normalizedPath));
		String[] fetchNewsSiteHeader = { "URl", "Http Status" };
		writer.writeNext(fetchNewsSiteHeader);
		for(MyFetchNewsSite fetchNewsSite : finalCrawlstat.fetchNewsSite) {
			String[] temp = {fetchNewsSite.url, Integer.toString(fetchNewsSite.statusCode)};
			writer.writeNext(temp);
		}
		writer.close();
		System.out.println("fetch_NewsSite.csv created, total row is " + Integer.toString(finalCrawlstat.fetchNewsSite.size() + 1));
		
		// write visit_NewsSite.csv
		normalizedPath = FilenameUtils.normalize(System.getProperty("user.home") + VISIT_NEWSSITE_PATH);
		newFile = new File(normalizedPath);
		if(newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		
		writer = new CSVWriter(new FileWriter(normalizedPath));
		String[] visitNewsSiteHeader = { "URL", "File Size", "Outlinks", "Content Tyoe" };
		writer.writeNext(visitNewsSiteHeader);
		for(MyVisitNewsSite visitNewsSite : finalCrawlstat.visitNewsSite) {
			String[] temp = {visitNewsSite.url, Integer.toString(visitNewsSite.file_size), Integer.toString(visitNewsSite.outlinks), visitNewsSite.content_type};
			writer.writeNext(temp);
		}
		writer.close();
		System.out.println("visit_NewsSite.csv created, total row is " + Integer.toString(finalCrawlstat.visitNewsSite.size() + 1));
		
		//write urls_NewsSite.csv
		normalizedPath = FilenameUtils.normalize(System.getProperty("user.home") + URLS_NEWSSITE_PATH);
		newFile = new File(normalizedPath);
		if(newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		
		writer = new CSVWriter(new FileWriter(normalizedPath));
		String[] urlNewsSiteHeader = { "URL", "Resides" };
		writer.writeNext(urlNewsSiteHeader);
		for(MyUrlsNewsSite myUrlsNewsSite : finalCrawlstat.urlsNewsSite) {
			String[] temp = { myUrlsNewsSite.url, myUrlsNewsSite.isResides ? "OK" : "N_OK" };
			writer.writeNext(temp);
		}
		writer.close();
		System.out.println("url_NewsSites.csv created, total row is " + Integer.toString(finalCrawlstat.urlsNewsSite.size() + 1));
		
		System.out.println("All csv files created");
		
		// write CrawlReport_NewsSite.txt
		System.out.println("Start creating CrawlReport_NewsSite.txt");
		normalizedPath = FilenameUtils.normalize(System.getProperty("user.home") + CRAWLREPORT_NEWSSITE_PATH);
		newFile = new File(normalizedPath);
		if(newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();
		
		writeCrawlReportNewsSite(normalizedPath, finalCrawlstat);
	}
}
