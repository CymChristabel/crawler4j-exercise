package csci572hw2;

import java.lang.invoke.StringConcatFactory;
import java.util.List;
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
	public final String http_Status;
	
	public MyFetchNewsSite(String url, String http_Status) {
		this.url = url;
		this.http_Status = http_Status;
	}
}

class MyVisitNewsSite {
	public final String url;
	public final String file_size;
	public final int outlinks;
	public final String content_type;
	
	public MyVisitNewsSite(String url, String file_size, int outlinks, String content_type) {
		this.url = url;
		this.file_size = file_size;
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
	public int failedOrAborted;
	public int totalURLsExtracted;
	public int uniqueURLsExtracted;
	public int uniqueURlsWithin;
	public int uniqueUrlsOutside;
	public HashMap<String, Integer> statusCodes;
	
	public MyCrawlStat() {
		
	}
	
	public void addFetchNewsSite(String url, String http_Status) {
		fetchNewsSite.add(new MyFetchNewsSite(url, http_Status));
	}
	
	public void addVisitNewsSite(String url, String file_size, int outlinks, String content_type) {
		visitNewsSite.add(new MyVisitNewsSite(url, file_size, outlinks, content_type));
	}
	
	public void addUrlsNewsSite(String url, Boolean isResides) {
		urlsNewsSite.add(new MyUrlsNewsSite(url, isResides));
	}
}
