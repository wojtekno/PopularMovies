package com.gmail.nowak.wjw.popularmovies.data.model.api;

public class ApiVideo {

    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public String getSite() {
        return site;
    }
}
