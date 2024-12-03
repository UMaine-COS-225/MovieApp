package com.movieapp.nlp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Processor {

    private Set<String> stopWords;

    public Processor(String stopWordsFilePath) {
        stopWords = new HashSet<>();
        loadStopWords(stopWordsFilePath);
    }

    private void loadStopWords(String stopWordsFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(stopWordsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String lowerAllCases(String text) {
        return text.toLowerCase();
    }   

    public String removePunctuation(String text) {
        return text.replaceAll("[^a-zA-Z0-9]", " ");
    }

    public String[] tokenizeWords(String text) {
        return text.split("\\s+");
    }

    /**
     * Removes words that are present in listOfStopWords from the arrayOfText
     * @param arrayOfText - Array of words after they do not have any punctuation
     * @return arrayOfText - Array of words that do not contain any stop words
     */
    public String[] removeStopWords(String[] arrayOfText) {
        Set<String> words = new HashSet<>();
        for (String word : arrayOfText) {
            if (!stopWords.contains(word)) {
                words.add(word);
            }
        }
        return words.toArray(new String[0]);
    }

    public String[] processText(String text) {
        text = lowerAllCases(text);
        text = removePunctuation(text);
        String[] words = tokenizeWords(text);
        words = removeStopWords(words);
        return words;
    }

    public String[] splitTextIntoSentences(String text) {
        return text.split("\\.");
    }

}
