package com.example.mai.moviewithfrag;

/**
 * Created by aya on 18/11/16.
 */

public class MovieReview {
    private String id;
    private String author;
    private String review;

    public MovieReview(String id, String author, String review){
        this.id = id;
        this.author = author;
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}
