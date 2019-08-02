package org.aksw.palmetto;
import org.aksw.palmetto.corpus.lucene.creation.IndexableDocument;
import org.aksw.palmetto.*;
import org.aksw.palmetto.corpus.lucene.creation.LuceneIndexHistogramCreator;
import org.aksw.palmetto.corpus.lucene.creation.SimpleLuceneIndexCreator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


public class LuceneBooleanIndexCreator {

    public static void printHelp() {
        System.out.println("java LuceneBooleanIndexCreator <index-dir> <doc-file-path>");
    }

    public static void main(String[] args) {

        if (args.length < 2 ) {

            System.out.println("Not enough arguments");
            printHelp();
            System.exit(1);
        }

        String indexPath = args[0];
        String docPath = args[1];

        SimpleLuceneIndexCreator creator = new SimpleLuceneIndexCreator("text");

        createBooleanIndex(new File(indexPath), docPath, creator);

    }

    private static void createBooleanIndex(File indexDir, String docFile, SimpleLuceneIndexCreator creator) {

        Path pathToFile = Paths.get(docFile);
        List<IndexableDocument> dir = new ArrayList<IndexableDocument>();

        int batchSize = 10000;
        int counter = 0;

        ArrayList<String> docBatch = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(docFile), StandardCharsets.UTF_8)))
        {
            String line = br.readLine();
            while (line != null) {
                if (line.length() > 0) {
                    docBatch.add(line);
                    counter++;

                    if (line.length() > 32766) {
                        System.out.println(counter);
                    }

                    if (counter == batchSize) {
                        creator.createIndex(indexDir, docBatch.iterator());
                        docBatch = new ArrayList<String>();
                        counter = 0;
                    }
                }
                line = br.readLine();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
