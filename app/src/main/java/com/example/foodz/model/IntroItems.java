package com.example.foodz.model;

public class IntroItems {
    String Title, Description;
    int screenImg;
    int logo;

    public IntroItems(String title, String description, int screenImg, int logo) {
        Title = title;
        Description = description;
        this.screenImg = screenImg;
        this.logo = logo;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setScreenImg(int screenImg) {
        this.screenImg = screenImg;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getScreenImg() {
        return screenImg;
    }

    public int getLogo() {
        return logo;
    }
}
