package com.movieapp.nlp;

import java.util.ArrayList;

import com.movieapp.movie.Movie;


public class MovieSummarizer {

    private TFIDF tfidf;
    private Processor processor;

    public MovieSummarizer(Processor processor, TFIDF tfidf) {
        this.processor = processor;
        this.tfidf = tfidf;
    }


    public String summarizeMovie(Movie movie, int numSentences) {
        String[] sentences = processor.splitTextIntoSentences(movie.getOverview());
        ArrayList<String> summary = new ArrayList<>();
        for (String sentence : sentences) {
            String[] words = processor.processText(sentence);
            float score = 0;
            for (String word : words) {
                score += tfidf.calculateTFIDF(null, word);
            }
            if (score > 0) {
                summary.add(sentence);
            }
            if (summary.size() == numSentences) {
                break;
            }
        }
        return String.join(" ", summary);

    }

    public String summarizeText(String text) {
        String[] words = processor.processText(text);
        StringBuilder summary = new StringBuilder();
        for (String word : words) {
            float score = tfidf.calculateTFIDF(null, word);
            if (score > 0) {
                summary.append(word).append(" ");
            }
        }
        return summary.toString();
    }

}
