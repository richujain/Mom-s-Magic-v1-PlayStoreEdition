package com.pulimoottil.richu.momsmagic_indianfoodrecipe.fragment;

public class Item {
    private String title;
    private String image;
    private String videoby;

    public Item(String title, String image,String videoby) {
        this.title = title;
        this.image = image;
        this.videoby = videoby;
    }


    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
    public String getVideoBy() {
        return videoby;
    }
}
