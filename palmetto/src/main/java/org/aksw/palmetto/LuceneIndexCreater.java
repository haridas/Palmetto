package org.aksw.palmetto;

import org.aksw.palmetto.*;
import org.aksw.palmetto.corpus.lucene.creation.*;
import org.aksw.palmetto.corpus.lucene.creation.IndexableDocument;
import org.aksw.palmetto.corpus.lucene.creation.PositionStoringLuceneIndexCreator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
public class LuceneIndexCreater{

    public static void main(String args[]){
        if(args.length < 2){
		System.out.println("Too few Arguments");
		System.out.println("Usage: java -cp target/Palmetto-jar-with-dependencies.jar org.aksw.palmetto.LuceneIndexCreater indexDir SingleTextFile");
		return;
	}
        String indexDir = args[0];
        String filepath = args[1];

       // Iterator<IndexableDocument> docIterator = readDataLineByLine(filepath);
        PositionStoringLuceneIndexCreator creator = new PositionStoringLuceneIndexCreator("text", "length");
       createIndexSplitwise(filepath, new File(indexDir), creator);
    }

	public static void createIndexSplitwise(String file, File indexDir, PositionStoringLuceneIndexCreator creator){
		Path pathToFile = Paths.get(file);
		List<IndexableDocument> dir = new ArrayList<IndexableDocument>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"))){
			String line = br.readLine();
			while(line != null){
				if(dir.size() == 100000){
					System.out.println("CreatingIndex");
					creator.createIndex(indexDir, dir.iterator());
					dir = new ArrayList<IndexableDocument>();
				}
				if(line.equals("")){
					line = br.readLine();
					continue;
				}
				String arr[] = line.split(" ");
				int length = arr.length;
				IndexableDocument id = new IndexableDocument(line,length);
				dir.add(id);
				line = br.readLine();
			}
			creator.createIndex(indexDir, dir.iterator());
			LuceneIndexHistogramCreator histogramCreator = new LuceneIndexHistogramCreator("length");		
			histogramCreator.createLuceneIndexHistogram(indexDir.getPath());
		}catch(Exception e){
			System.out.println(e);
		}
	}

    public static Iterator<IndexableDocument> readDataLineByLine(String file) { 
  
        Path pathToFile = Paths.get(file);
        List<IndexableDocument>  dir= new ArrayList<IndexableDocument>();
        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {
            String line = br.readLine();            
            int counter = 0;
	      while(line != null){
            if(line.equals("")){
                line = br.readLine();
                continue;
            }
            counter = counter + 1;
                    String arr[] = line.split(" ");
                    int length = arr.length;           
                    IndexableDocument id = new IndexableDocument(line,length);
                    dir.add(id);
                    line = br.readLine();
        }
            System.out.println(counter);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return dir.iterator();
	}
}
