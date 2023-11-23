package com.example.reciclaapp;

// VerNoticiasItem.java
public class VerNoticiasItem {
    private String title;
    private String content;
    private String author;
    private int imageResource; // You can use int for drawable resource ID or String for image URL

    public VerNoticiasItem(String title, String content, String author, int imageResource) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public int getImageResource() {
        return imageResource;
    }
}

