package com.gmail.nowak.wjw.popularmovies.data.model.view_data.detail;

public class ReviewViewData {
    private String content;
    private String author;

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public ReviewViewData(String content, String author) {
        this.content = content;
        this.author = author;
    }
}
