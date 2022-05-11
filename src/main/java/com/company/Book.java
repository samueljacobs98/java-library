package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Book extends ABook {
    private final List<BookItem> copies = new ArrayList<>();

    public Book(String title, String author, String genre, String subGenre, String publisher) {
        super(title, author, genre, subGenre, publisher, UUID.randomUUID().toString());
    }

    // Getters
    public List<BookItem> getCopies() {
        return copies;
    }

    // Methods
    public void addNewCopy() {
        BookItem copy = new BookItem(title, author, genre, subGenre, publisher, bookID);
        copies.add(copy);
    }

    public List<BookItem> getAvailableCopies() {
        return copies.stream().filter(BookItem::getAvailability).collect(Collectors.toList());
    }

    public List<BookItem> getUnavailableCopies() {
        return copies.stream().filter(bookItem -> !bookItem.getAvailability()).collect(Collectors.toList());

    }
}
