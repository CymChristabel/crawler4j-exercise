package csci572hw2;

import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private final static Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpeg|png|tiff?))$");

    private MyCrawlStat myCrawlStat;
    
    public MyCrawler() {
    	myCrawlStat = new MyCrawlStat();
	}
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url)
    {
    	String href = url.getURL().toLowerCase();
    	if(imgPatterns.matcher(href).matches())
    	{
    		System.out.println(referringPage.getWebURL().getURL());
    		System.out.println(href);
    		System.out.println(referringPage.getContentType());
    		System.out.println(referringPage.getContentData().length);
    	}
    	return !FILTERS.matcher(href).matches();
    }
    
    @Override
    public Object getMyLocalData() {
    	return myCrawlStat;
    }
    
    @Override
    public void visit (Page page)
    {
    	String url = page.getWebURL().getURL();
    	if(imgPatterns.matcher(url).matches())
    	{
    		System.out.println(url);
    		System.out.println(page.getContentData().length);
    	}
    	if(page.getParseData() instanceof HtmlParseData)
    	{
    		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
    		String text = htmlParseData.getText();
    		String html = htmlParseData.getHtml();
    		Set<WebURL> links = htmlParseData.getOutgoingUrls();
    		links.add(page.getWebURL());
//    		System.out.println("content-type:" + page.getContentType());
//    		System.out.println("Status code:" + page.getStatusCode());
//    		System.out.println("URL:" + page.getWebURL());
//    		System.out.println("Text length: " + text.length());
//    		System.out.println("Html length: " + html.length());
//    		System.out.println("Number of outgoing links: " + links.size());
//    		System.out.println("file size:" + html.getBytes());
    	}
    }
}
