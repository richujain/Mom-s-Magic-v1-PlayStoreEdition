package com.pulimoottil.richu.momsmagic_indianfoodrecipe.fragment;

public class Search_Items {
    private String title, imageurl, youtubeurl,videoby;
    public Search_Items(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public String getYoutubeUrl() {
        return youtubeurl;
    }
    public String getVideoBy() {
        return videoby;
    }

    public Search_Items(String title, String imageurl, String youtubeurl,String videoby) {
        this.title = title;
        this.imageurl = imageurl;
        this.youtubeurl = youtubeurl;
        this.videoby = videoby;
    }
}
