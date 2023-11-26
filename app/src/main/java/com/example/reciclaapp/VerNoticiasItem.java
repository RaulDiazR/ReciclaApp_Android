package com.example.reciclaapp;

// VerNoticiasItem.java
public class VerNoticiasItem {
    private String title;
    private String content;
    private String author;
    private String fotoUrl; // You can use int for drawable resource ID or String for image URL

    public VerNoticiasItem(String title, String content, String author, String fotoUrl) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.fotoUrl = fotoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}

