package com.example.laba_4.model;

public class Photo {

    private String id;
    private String pageURL;
    private String tags;
    private String largeImageURL;
    private String views;
    private String downloads;
    private String likes;
    private String comments;
    private String user;
    private String userImageURL;


    public Photo(String id, String pageURL, String tags, String largeImageURL, String views, String downloads, String likes, String comments, String user, String userImageURL) {
        this.id = id;
        this.pageURL = pageURL;
        this.tags = tags;
        this.largeImageURL = largeImageURL;
        this.views = views;
        this.downloads = downloads;
        this.likes = likes;
        this.comments = comments;
        this.user = user;
        this.userImageURL = userImageURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getId() {
        return id;
    }

    public String getPageURL() {
        return pageURL;
    }

    public String getTags() {
        return tags;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public String getViews() {
        return views;
    }

    public String getDownloads() {
        return downloads;
    }

    public String getLikes() {
        return likes;
    }

    public String getComments() {
        return comments;
    }

    public String getUser() {
        return user;
    }

    public String getUserImageURL() {
        return userImageURL;
    }
}
