package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookItem extends ABook{
    private final String itemID;
    private Boolean isAvailable;
    private final List<Loan> loanHistory = new ArrayList<>(); // Update String to Loan

    public BookItem(String title, String author, String genre, String subGenre, String publisher, String bookID) {
        super(title, author, genre, subGenre, publisher, bookID);
        this.itemID = UUID.randomUUID().toString();
        this.isAvailable = true;
    }

    // getters
    public String getItemID() {
        return itemID;
    }

    public Boolean getAvailability() {
        return isAvailable;
    }

    public List<Loan> getLoanHistory() {
        return loanHistory;
    }

    public Loan getRecentLoan() { return loanHistory.get(loanHistory.size() - 1); }

    // methods
    public void updateAvailability() {
        isAvailable = !isAvailable;
    }

    public void addNewLoan(Loan loan) {
        updateAvailability();
        loanHistory.add(loan);
    }
}
