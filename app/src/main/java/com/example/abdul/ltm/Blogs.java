package com.example.abdul.ltm;

public class Blogs {

    private String country;
    private String desc;
    private String image;
    private String title;
    private String userId;
    private long date;

    public Blogs() {
    }

    public Blogs(String country, String desc, String image, String title, String userId, long date) {
        this.country = country;
        this.desc = desc;
        this.image = image;
        this.title = title;
        this.userId = userId;
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
