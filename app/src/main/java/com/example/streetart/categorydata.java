package com.example.streetart;

public class categorydata {


    String categoryName;
    int  numberOfArt;

    public categorydata(String categoryName, int numberOfArt) {
        this.categoryName = categoryName;
        this.numberOfArt = numberOfArt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getNumberOfArt() {
        return numberOfArt;
    }
}
