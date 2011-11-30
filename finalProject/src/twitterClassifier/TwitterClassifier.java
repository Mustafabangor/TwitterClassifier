package twitterClassifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public abstract class TwitterClassifier {
	
    protected RAMDirectory index = new RAMDirectory();
    
	public TwitterClassifier(){
		
	}
	
	public abstract void classify(String query);
	protected abstract void trainClassifier(String trainingFile) throws Exception;
	
	protected void buildIndex(String trainingFile){
		String currentLine; // current line in the file
		String curSentiment = "positive";  // current sentiment for the doc
		StringBuffer buf = new StringBuffer();  // concat all lines in this doc
		try {
			// open the training file and start reading lines
			BufferedReader reader = new BufferedReader(new FileReader(trainingFile));
			
			// create a new index
			index = new RAMDirectory();		// Create a new index in RAM
			IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_34));
			IndexWriter writer = new IndexWriter(index, indexConfig);
			
			// loop through all lines
			while((currentLine = reader.readLine()) != null){
				if(currentLine.startsWith("^^END^^")){	// found the end of the document
					Document doc = new Document(); // build a new document
					
					// add fields to the document
					doc.add(new Field("text", buf.toString(), Store.YES, Index.ANALYZED));
					doc.add(new Field("sentiment", curSentiment, Store.YES, Index.ANALYZED));
					writer.addDocument(doc);
					buf = new StringBuffer();  // empty the buffer
				}
				else if(currentLine.startsWith("^^NEGATIVE^^")){
					curSentiment = "negative"; // we've switched from positive docs to negative docs.
				}
				else if(currentLine.startsWith("^^^POSITIVE^^^")){
					continue;
				}
				else{
					// found a regular line. Add it to the buffer and continue
					buf.append(currentLine);
				}			
			}
			// commit these changes to the index.
			writer.commit();
			writer.optimize();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("ERROR: File " + trainingFile + " Not Found!");
		} catch (IOException e) {
			System.out.println("ERROR: IO Exception caught in buildClassifier");
			e.printStackTrace();
		}
		// return the newly created training index
	}
	
}
