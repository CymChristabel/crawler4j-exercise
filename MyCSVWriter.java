package csci572hw2;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileWriter;

import com.opencsv.CSVWriter;

public class MyCSVWriter {
	private final static String FETCH_NEWSSITE_PATH = "~/Projects/csci572hw2/fetch_NewsSite.csv";
    private final static String VISIT_NEWSSITE_PATH = "~/Projects/csci572hw2/visit_NewsSite.csv";
    private final static String URLS_NEWSSITE_PATH = "~/Projects/csci572hw2/urls_NewsSite.csv";
    
    private Writer fetch_NewsSite_writer;
    private Writer visit_NewsSite_writer;
    private Writer urls_NewsSite_writer;
    
    public MyCSVWriter() throws IOException{
    	fetch_NewsSite_writer = new FileWriter(FETCH_NEWSSITE_PATH);
    	visit_NewsSite_writer = new FileWriter(VISIT_NEWSSITE_PATH);
    	urls_NewsSite_writer = new FileWriter(URLS_NEWSSITE_PATH);
    }
    
    public void writeCSVs()
    {
    	
    }
}
