package com.movieapp.nlp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import org.bson.BsonValue;

import com.movieapp.movie.Movie;
import com.movieapp.movie.MovieReview;

public class TFIDF {

    private HashSet<String> vocabulary = new HashSet<>();
    private HashMap<String, Float> idf = new HashMap<>();
    private HashMap<BsonValue, HashMap<String, Integer>> tf = new HashMap<>();
    private Processor processor;

    public TFIDF(Processor processor) {
        this.processor = processor;
    }

    public void addSample(BsonValue id, String text) {
        String[] words = processor.processText(text);
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
            idf.put(word, (float) Math.log((float) tf.size() + 1 / count + 1));
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

    public float calculateTFIDF(BsonValue id, String[] words) {
        float score = 0;
        for (String word : words) {
            score += calculateTFIDF(id, word);
        }
        return score;
    }

    public ArrayList<BsonValue> getIds() {
        return new ArrayList<>(tf.keySet());
    }

}
