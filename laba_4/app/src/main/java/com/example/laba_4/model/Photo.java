package com.example.laba_4.model;

public class Photo {

    private final String largeImageURL;
    private final String user;
    private final String userImageURL;

    public Photo(String user, String largeImageURL, String userImageURL) {
        this.user = user;
        this.largeImageURL = largeImageURL;
        this.userImageURL = userImageURL;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public String getUser() {
        return user;
    }

    public String getUserImageURL() {
        return userImageURL;
    }
}
