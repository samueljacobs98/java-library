package com.company;

public abstract class ABook {
    protected String title;
    protected String author;
    protected String genre;
    protected String subGenre;
    protected String publisher;
    protected String bookID;

    public ABook(String title, String author, String genre, String subGenre, String publisher, String bookID) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.subGenre = subGenre;
        this.publisher = publisher;
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getSubGenre() {
        return subGenre;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getBookID() {
        return bookID;
    }
}
