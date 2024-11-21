package com.movieapp.nlp;

import java.util.HashMap;
import java.util.HashSet;
import org.bson.BsonValue;

import com.movieapp.movie.Movie;

public class TFIDF {

    private HashSet<String> vocabulary = new HashSet<>();
    private HashMap<String, Float> idf = new HashMap<>();
    private HashMap<BsonValue, HashMap<String, Integer>> tf = new HashMap<>();

    Processor processor = new Processor("src/main/resources/stop_words.txt");

    public void addSample(BsonValue id, Movie movie) {
        String[] words = processor.processText(movie.getOverview());
        HashMap<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            vocabulary.add(word);
        }
        tf.put(id, wordCount);
    }

    public void calculateIDF() {
        for (String word : vocabulary) {
            int count = 0;
            for (HashMap<String, Integer> wordCount : tf.values()) {
                if (wordCount.containsKey(word)) {
                    count++;
                }
            }
            idf.put(word, (float) Math.log((float) tf.size() / count));
        }
    }

    public float calculateTFIDF(BsonValue id, String word) {
        if (!tf.containsKey(id)) {
            return 0;
        }
        HashMap<String, Integer> wordCount = tf.get(id);
        if (!wordCount.containsKey(word)) {
            return 0;
        }
        return wordCount.get(word) * idf.get(word);
    }

    
}
