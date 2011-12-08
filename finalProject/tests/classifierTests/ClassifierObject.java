package classifierTests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitterClassifier.*;

public class ClassifierObject {
	private ArrayList<String> testDocs;
	private String[] coffeeDocs = {
			"resources/cocoa.txt",
			"resources/cocoa1.txt",
			"resources/cocoa2.txt",
			"resources/coffee.txt",
			"resources/coffee1.txt"
	};
	
	public String readFileToString(String fileName){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			StringBuffer buf = new StringBuffer();
			String curLine = "";
			while((curLine = reader.readLine()) != null){
				buf.append(curLine);
			}
			return buf.toString();
		} catch (FileNotFoundException e) {
			System.out.println("File "+ fileName + " Not found");
			return null;
		} catch (IOException e) {
			System.out.println("Caught IO Exception in readFileToString");
			return null;
		}
	}
	@Before
	public void loadTests(){
		testDocs = new ArrayList<String>();
		for(String s : coffeeDocs){
			testDocs.add(readFileToString(s));
		}
	}
	
	@After
	public void after(){
		
	}

	@Test
	public void testClassify() {
		LuceneClassifier classifier = new LuceneClassifier("resources/coffee-test-set.csv", "positive", false, false );
		try {
			assertTrue(classifier.classify(testDocs.get(0)));
			assertFalse(classifier.classify(testDocs.get(1)));
			assertFalse(classifier.classify(testDocs.get(2)));
			assertFalse(classifier.classify(testDocs.get(3)));
			assertFalse(classifier.classify(testDocs.get(4)));
			
		} catch (Exception e) {
			System.out.println("caught exception in test clasify");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOverfit(){
		LuceneClassifier classifier = new LuceneClassifier("resources/coffee-test-set.csv", "positive", true, false );
		try {
						
			assertTrue(classifier.classify(testDocs.get(0)));
			assertFalse(classifier.classify(testDocs.get(1)));
			assertFalse(classifier.classify(testDocs.get(2)));
			assertFalse(classifier.classify(testDocs.get(3)));
			assertFalse(classifier.classify(testDocs.get(4)));
			
		} catch (Exception e) {
			System.out.println("caught exception in test clasify");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOverfitFeatureSelect(){
		LuceneClassifier classifier = new LuceneClassifier("resources/coffee-test-set.csv", "positive", true, true );
		try {
				
			assertTrue(classifier.classify(testDocs.get(0)));
			assertTrue(classifier.classify(testDocs.get(1)));
			assertTrue(classifier.classify(testDocs.get(2)));
			assertTrue(classifier.classify(testDocs.get(3)));
			assertTrue(classifier.classify(testDocs.get(4)));
			
		} catch (Exception e) {
			System.out.println("caught exception in test clasify");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFeatureSelect() {
		LuceneClassifier classifier = new LuceneClassifier("resources/coffee-test-set.csv", "positive", false, true );
		try {
			
				
			assertTrue(classifier.classify(testDocs.get(0)));
			assertTrue(classifier.classify(testDocs.get(1)));
			assertTrue(classifier.classify(testDocs.get(2)));
			assertTrue(classifier.classify(testDocs.get(3)));
			assertTrue(classifier.classify(testDocs.get(4)));
			
		} catch (Exception e) {
			System.out.println("caught exception in test clasify");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSentiment(){
		LuceneClassifier classifier = new LuceneClassifier("resources/sentiment-test.csv", "positive", false, true );
		
		try {
			assertTrue(classifier.classify("great"));
			assertFalse(classifier.classify("horrible"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCorpus(){
		LuceneClassifier classifier = new LuceneClassifier("resources/full-corpus.csv", "positive", true, true );
		try {
			System.out.println(classifier.classify("great fantastic super awesome"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}
	

}
