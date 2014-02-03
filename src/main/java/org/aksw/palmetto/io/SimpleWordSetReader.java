package org.aksw.palmetto.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWordSetReader {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleWordSetReader.class);

    public String[][] readWordSets(String inputFile) {
        List<String[]> topics = new ArrayList<String[]>();
        FileReader reader = null;
        Scanner scanner = null;
        try {
            String[] wordset;
            reader = new FileReader(inputFile);
            scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                wordset = parseWordSetFromLine(scanner.nextLine());
                if ((wordset != null) && (wordset.length > 0)) {
                    topics.add(wordset);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while creating Index. Aborting.", e);
        } finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (Exception e) {
                }
            } else {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return topics.toArray(new String[topics.size()][]);
    }

    private String[] parseWordSetFromLine(String line) {
        List<String> topic = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(line);
        while ((tokenizer.hasMoreTokens())) {
            String nextToken = tokenizer.nextToken();
            topic.add(nextToken);
        }
        return topic.toArray(new String[topic.size()]);
    }
}