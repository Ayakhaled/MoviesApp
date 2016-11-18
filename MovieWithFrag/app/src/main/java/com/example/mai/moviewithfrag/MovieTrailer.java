package com.example.mai.moviewithfrag;

/**
 * Created by mai on 06/11/16.
 */

public class MovieTrailer {
    private String id;
    private String key;
    private String name;

    public MovieTrailer(String id, String key, String name){
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public String getFullUrl (){
        return "https://www.youtube.com/watch?v="+key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
