package com.movieapp.movie;

import org.bson.Document;

public class Movie {

    private String name;
    private String overview;
    private Float rating;

    public Movie(String name, String overview, Float rating) {
        this.name = name;
        this.overview = overview;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public Float getRating() {
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