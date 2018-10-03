package csci572hw2;

import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler extends WebCrawler {
	private static final Pattern filters = Pattern.compile(
		        ".*(\\.(php|css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" +
		        "|rm|smil|wmv|swf|wma|zip|rar|gz|xml|ico|svg|json))$");
    private final static Pattern FILEPATTERN = Pattern.compile(".*(\\.(html|pdf|doc|gif|png|jpeg))$");
    private final static String TARGET_WEBSITE = "wsj.com";
    private final static String HTTP_PREFIX = "http://";
    private final static String HTTPS_PREFIX = "https://";
    
    private MyCrawlStat myCrawlStat;

    public MyCrawler() {
    	myCrawlStat = new MyCrawlStat();
	}
    
    private boolean isResides(String URL) {
    	 return URL.startsWith(HTTP_PREFIX + TARGET_WEBSITE) || URL.startsWith(HTTPS_PREFIX + TARGET_WEBSITE)
			|| URL.startsWith(HTTP_PREFIX + "www." + TARGET_WEBSITE) || URL.startsWith(HTTPS_PREFIX + "www." + TARGET_WEBSITE);
    }
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url)
    {
    	String href = url.getURL().toLowerCase();
    	
    	return (FILEPATTERN.matcher(href).matches() || !filters.matcher(href).matches()) && isResides(href) && !href.contains(".xml");
    }
    
    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
    	// count the number of URLs that crawler trying to fetch
    	String url = webUrl.getURL();
    	myCrawlStat.addFetchNewsSite(url, statusCode);
    	myCrawlStat.fetchAttemps = myCrawlStat.fetchAttemps + 1;
    	if(statusCode / 100 == 2)
    	{
    		myCrawlStat.fetchSuccessd = myCrawlStat.fetchSuccessd + 1;
    	}
    	else
    	{
    		myCrawlStat.fetchFailedOrAborted = myCrawlStat.fetchFailedOrAborted + 1;
    	}
    	myCrawlStat.addStatusCode(Integer.toString(statusCode) + ' ' + statusDescription);
    }
    
    @Override
    public MyCrawlStat getMyLocalData() {
    	return myCrawlStat;
    }
    
    @Override
    public void visit (Page page)
    {
    	String url = page.getWebURL().getURL();
    	int fileSize = page.getContentData().length;
    	String contentType = page.getContentType().split("\\;", 2)[0];
    	if(contentType.equals("text/html") == false)
    	{
    		System.out.println(url);
    		System.out.println(contentType);
    	}
    	// count total URLs 
    	myCrawlStat.addUniqueUrl(url);
    	// handling HTML pages and files
    	if(page.getParseData() instanceof HtmlParseData)
    	{
    		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
    		Set<WebURL> links = htmlParseData.getOutgoingUrls();
    		for(WebURL outgoingUrl : links)
    		{
    			myCrawlStat.addUniqueUrl(outgoingUrl.getURL());
    		}
    		links.add(page.getWebURL());
    		myCrawlStat.totalURLsExtracted = myCrawlStat.totalURLsExtracted + htmlParseData.getOutgoingUrls().size();
    		myCrawlStat.addVisitNewsSite(url, fileSize, htmlParseData.getOutgoingUrls().size(), contentType);
        	myCrawlStat.addEncounteredContentType(contentType);
    	}
    	else
    	{
    		myCrawlStat.addVisitNewsSite(url, fileSize, 0, contentType);
        	myCrawlStat.addEncounteredContentType(contentType);
    	}
    }
}
