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
    private final static String TARGET_WEBSITE = "wsj.com";
    private final static String HTTP_PREFIX = "http://";
    private final static String HTTPS_PREFIX = "https://";
    
    private static boolean isResides(String URL) {
   	 return URL.startsWith(HTTP_PREFIX + TARGET_WEBSITE) || URL.startsWith(HTTPS_PREFIX + TARGET_WEBSITE)
			|| URL.startsWith(HTTP_PREFIX + "www." + TARGET_WEBSITE) || URL.startsWith(HTTPS_PREFIX + "www." + TARGET_WEBSITE);
   }
    
    private static void writeCrawlReportNewsSite(String normalizedPath, MyCrawlStat myCrawlStat) {
    	try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(normalizedPath))){
    		 // write general info
			 bWriter.write("News site crawled: " + HTTPS_PREFIX + "www." + TARGET_WEBSITE);
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
			 
			 // get unique URLs
			 int uniqueURLsExtracted = 0;
			 int uniqueURLsWithin = 0;
			 int uniqueURLsOutside = 0;
			 for(Map.Entry<String, Boolean> entry : myCrawlStat.urlEncountered.entrySet()) {
				 uniqueURLsExtracted = uniqueURLsExtracted + 1;
				 if(isResides(entry.getKey())) {
					 uniqueURLsWithin = uniqueURLsWithin + 1;
				 }
				 else {
					 uniqueURLsOutside = uniqueURLsOutside + 1;
				 }
			 }
			 
			 bWriter.write("# unique URLs extracted: " + Integer.toString(uniqueURLsExtracted));
			 bWriter.newLine();
			 bWriter.write("# unique URLs within News Site: " + Integer.toString(uniqueURLsWithin));
			 bWriter.newLine();
			 bWriter.write("# unique URLs outside News Site: " + Integer.toString(uniqueURLsOutside));
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
			 // count different sizes
			 int[] file_sizes = { 0, 0, 0, 0, 0 };
			 for(MyVisitNewsSite visitNewsSite : myCrawlStat.visitNewsSite) {
				 if(visitNewsSite.file_size < 1024) {
					 file_sizes[0] = file_sizes[0] + 1;
				 }
				 else if(visitNewsSite.file_size < 1024 * 10 && visitNewsSite.file_size >= 1024) {
					 file_sizes[1] = file_sizes[1] + 1;
				 }
				 else if(visitNewsSite.file_size < 1024 * 100 && visitNewsSite.file_size >= 1024 * 10) {
					 file_sizes[2] = file_sizes[2] + 1;
				 }
				 else if(visitNewsSite.file_size < 1024 * 1000 && visitNewsSite.file_size >= 1024 * 100) {
					 file_sizes[3] = file_sizes[3] + 1;
				 }
				 else {
					 file_sizes[4] = file_sizes[4] + 1;
				}
			 }
			 bWriter.write("File Sizes:");
			 bWriter.newLine();
			 bWriter.write("===========");
			 bWriter.newLine();
			 bWriter.write("< 1KB: " + Integer.toString(file_sizes[0]));
			 bWriter.newLine();
			 bWriter.write("1KB ~ <10KB: " + Integer.toString(file_sizes[1]));
			 bWriter.newLine();
			 bWriter.write("10KB ~ <100KB: " + Integer.toString(file_sizes[2]));
			 bWriter.newLine();
			 bWriter.write("100KB ~ <1MB: " + Integer.toString(file_sizes[3]));
			 bWriter.newLine();
			 bWriter.write(">= 1MB: " + Integer.toString(file_sizes[4]));
			 bWriter.newLine();
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
		int maxPagesToFetch	= 20000;
		int politenessDelay = 200;
		
		CrawlConfig config  = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		config.setMaxPagesToFetch(maxPagesToFetch);
		config.setIncludeBinaryContentInCrawling(true);
		config.setPolitenessDelay(politenessDelay);
		config.setUserAgentString("Ataraxia");
		
		/* Instantiate the controller for crawl */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		
		
		controller.addSeed(HTTPS_PREFIX + "www." + TARGET_WEBSITE);
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
			finalCrawlstat.fetchNewsSite.addAll(crawlStat.fetchNewsSite);
			finalCrawlstat.visitNewsSite.addAll(crawlStat.visitNewsSite);
			
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
			
			// merge encountered URLs
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
		System.out.println("fetch_NewsSite.csv created, total row is " + Integer.toString(finalCrawlstat.fetchNewsSite.size()));
		
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
		System.out.println("visit_NewsSite.csv created, total row is " + Integer.toString(finalCrawlstat.visitNewsSite.size()));
		
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
		for(Map.Entry<String, Boolean> entry : finalCrawlstat.urlEncountered.entrySet()) {
			String[] temp = { entry.getKey(), isResides(entry.getKey()) ? "OK" : "N_OK" };
			writer.writeNext(temp);
		}
		writer.close();
		System.out.println("url_NewsSites.csv created, total row is " + Integer.toString(finalCrawlstat.urlEncountered.size()));
		
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
