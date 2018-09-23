package csci572hw2;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

class MyUrlsNewsSite {
	public final String url;
	public final boolean isResides;
	
	public MyUrlsNewsSite(String url, boolean isResides) {
		this.url = url;
		this.isResides = isResides;
	}
}

class MyFetchNewsSite {
	public final String url;
	public final int statusCode;
	
	public MyFetchNewsSite(String url, int statusCode) {
		this.url = url;
		this.statusCode = statusCode;
	}
}

class MyVisitNewsSite {
	public final String url;
	public final int file_size;
	public final int outlinks;
	public final String content_type;
	
	public MyVisitNewsSite(String url, int length, int outlinks, String content_type) {
		this.url = url;
		this.file_size = length;
		this.outlinks = outlinks;
		this.content_type = content_type;
	}
}

public class MyCrawlStat {
	public List<MyFetchNewsSite> fetchNewsSite;
	public List<MyVisitNewsSite> visitNewsSite;
	public List<MyUrlsNewsSite> urlsNewsSite;
	public int fetchAttemps;
	public int fetchSuccessd;
	public int fetchFailedOrAborted;
	public int totalURLsExtracted;
	public int uniqueURLsExtracted;
	public int uniqueURlsWithin;
	public int uniqueUrlsOutside;
	public HashMap<String, Integer> statusCodes;
	public HashMap<String, Boolean> urlEncountered;
	public HashMap<String, Integer> contentTypeEncountered;
	
	public MyCrawlStat() {
		fetchNewsSite = new ArrayList<>();
		visitNewsSite = new ArrayList<>();
		urlsNewsSite = new ArrayList<>();
		fetchAttemps = 0;
		fetchSuccessd = 0;
		fetchFailedOrAborted = 0;
		totalURLsExtracted = 0;
		uniqueURLsExtracted = 0;
		uniqueUrlsOutside = 0;
		uniqueURlsWithin = 0;
		statusCodes = new HashMap<String, Integer>();
		urlEncountered = new HashMap<String, Boolean>();
		contentTypeEncountered = new HashMap<String, Integer>();
	}
	
	public void addFetchNewsSite(String url, int statusCode) {
		fetchNewsSite.add(new MyFetchNewsSite(url, statusCode));
	}
	
	public void addVisitNewsSite(String url, int length, int outlinks, String content_type) {
		visitNewsSite.add(new MyVisitNewsSite(url, length, outlinks, content_type));
	}
	
	public void addUrlsNewsSite(String url, Boolean isResides) {
		urlsNewsSite.add(new MyUrlsNewsSite(url, isResides));
	}
	
	public void addStatusCode(int statusCode, String statusDescription) {
		String key = Integer.toString(statusCode) + ' ' + statusDescription;
		if(statusCodes.get(key) == null) {
			statusCodes.put(key, 1);
		}
		else
		{
			statusCodes.put(key, statusCodes.get(key) + 1);
		}
	}
	
	public void addUniqueUrl(String url, String targetDomain){
		if(urlEncountered.get(url) == null)
    	{
			uniqueURLsExtracted = uniqueURLsExtracted + 1;
    		if(url.startsWith(targetDomain))
    		{
    			uniqueURlsWithin = uniqueURlsWithin + 1;
    		}
    		else
    		{
    			uniqueUrlsOutside = uniqueUrlsOutside + 1; 
    		}
    		urlEncountered.put(url, true);
    	}
	}
	
	public void addEncounteredContentType(String contentType) {
		if(contentTypeEncountered.get(contentType) == null) {
			contentTypeEncountered.put(contentType, 1);
		}
		else {
			contentTypeEncountered.put(contentType, contentTypeEncountered.get(contentType) + 1);
		}
	}
}
