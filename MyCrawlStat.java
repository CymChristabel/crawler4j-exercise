package csci572hw2;

import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

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
	public int fetchAttemps;
	public int fetchSuccessd;
	public int fetchFailedOrAborted;
	public int totalURLsExtracted;
	public Map<String, Integer> statusCodes;
	public Map<String, Boolean> urlEncountered;
	public Map<String, Integer> contentTypeEncountered;
	
	public MyCrawlStat() {
		fetchNewsSite = new ArrayList<>();
		visitNewsSite = new ArrayList<>();
		fetchAttemps = 0;
		fetchSuccessd = 0;
		fetchFailedOrAborted = 0;
		totalURLsExtracted = 0;
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
	
	
	public void addStatusCode(String key) {
		if(statusCodes.get(key) == null) {
			statusCodes.put(key, 1);
		}
		else
		{
			statusCodes.put(key, statusCodes.get(key) + 1);
		}
	}
	
	public void addUniqueUrl(String url){
		urlEncountered.put(url, true);
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
