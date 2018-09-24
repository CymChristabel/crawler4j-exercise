package csci572hw2;

import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private final static Pattern filePattern = Pattern.compile(".*(\\.(pdf|doc|bmp|gif|jpeg|png|tiff?))$");
    private final static String targetDomain = "https://www.chron.com";
    
    private MyCrawlStat myCrawlStat;

    public MyCrawler() {
    	myCrawlStat = new MyCrawlStat();
	}
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url)
    {
    	String href = url.getURL().toLowerCase();
    	if(href.startsWith(targetDomain))
    	{
    		myCrawlStat.addUrlsNewsSite(href, true);
    	}
    	else
    	{
    		myCrawlStat.addUrlsNewsSite(href, false);
    	}
    	return !FILTERS.matcher(href).matches() && href.startsWith(targetDomain);
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
    	myCrawlStat.addStatusCode(statusCode, statusDescription);
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
    	String contentType = page.getContentType();

    	// count total URLs and content types encountered
    	myCrawlStat.totalURLsExtracted = myCrawlStat.totalURLsExtracted + 1;
    	myCrawlStat.addUniqueUrl(url, targetDomain);
    	myCrawlStat.addEncounteredContentType(contentType);
    	
    	// handling HTML pages and files
    	if(page.getParseData() instanceof HtmlParseData)
    	{
    		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
    		Set<WebURL> links = htmlParseData.getOutgoingUrls();
    		for(WebURL outgoingUrl : links)
    		{
    			myCrawlStat.addUniqueUrl(outgoingUrl.getURL(), targetDomain);
    		}
    		links.add(page.getWebURL());
    		myCrawlStat.addVisitNewsSite(url, fileSize, htmlParseData.getOutgoingUrls().size(), contentType);
    	}
    	else
    	{
    		if(filePattern.matcher(url).matches())
        	{
        		myCrawlStat.addVisitNewsSite(url, fileSize, 0, contentType);
        	}
    	}
    }
}
