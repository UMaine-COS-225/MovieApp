package com.movieapp.movie;

import org.bson.Document;


public class MovieReview {
    private String review, sentiment;

    public MovieReview(String review, String sentiment) {
        this.review = review;
        this.sentiment = sentiment;
    }

    public MovieReview(Document document) {
        this.review = document.getString("review");
        this.sentiment = document.getString("sentiment");
    }

    public String getReview() {
        return review;
    }

    public String getSentiment() {
        return sentiment;
    }

    public Document getDocument() {
        Document document = new Document();
        document.append("review", review);
        document.append("sentiment", sentiment);
        return document;
    }

}