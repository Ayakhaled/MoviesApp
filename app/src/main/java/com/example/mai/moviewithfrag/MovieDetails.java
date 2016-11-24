package com.example.mai.moviewithfrag;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mai on 17/10/16.
 */
public class MovieDetails implements Serializable {
    private final String imgURL = "http://image.tmdb.org/t/p/w185/";
    private String title;
    private String poster;
    private String plotSynopsis;
    private String rating;
    private String releaseDate;
    private String pos;
    private boolean favourite;
    private String movieID;
    private ArrayList<String> trailers;

    public MovieDetails() {
    }

    public MovieDetails(String title, String poster, String plotSynopsis, String rating, String releaseDate, String movieID) {
        super();
        this.title = title;
        this.poster = imgURL+poster;
        this.plotSynopsis = plotSynopsis;
        this.rating = rating;
        this.pos = poster;
        this.releaseDate = releaseDate;
        this.movieID = movieID;
        favourite = false;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean getFavourite() {
        return favourite;
    }

    public String getPos() {
        return pos;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setTrailers(ArrayList<String> trailers){
        this.trailers = trailers;
    }

    public ArrayList<String> getTrailers(){
        return trailers;
    }

    public void setMovieID (String movieID){
        this.movieID = movieID;
    }

    public String getMovieID(){
        return movieID;
    }
}

