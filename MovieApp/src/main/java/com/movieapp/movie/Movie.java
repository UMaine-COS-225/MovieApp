package com.movieapp.movie;

import org.bson.Document;

public class Movie {

    private String name;
    private String overview;
    private Double rating;

    public Movie(String name, String overview, Double rating) {
        this.name = name;
        this.overview = overview;
        this.rating = rating;
    }

    public Movie(Document document) {
        this.name = document.getString("name");
        this.overview = document.getString("overview");
        this.rating = document.getDouble("rating");
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return rating;
    }

    public Document getDocument() {
        Document document = new Document();
        document.append("name", name);
        document.append("overview", overview);
        document.append("rating", rating);
        return document;
    }

}