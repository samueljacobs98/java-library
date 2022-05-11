package com.company;

import java.time.LocalDate;

public class Loan {
    private final String bookID;
    private final String itemID;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Member member;
    private Boolean returned;

    public Loan(BookItem bookItem, Member member) {
        this.bookID = bookItem.getBookID();
        this.itemID = bookItem.getItemID();
        this.title = bookItem.getTitle();
        this.startDate = LocalDate.now();
        this.endDate = startDate.plusWeeks(2);
        this.member = member;
        this.returned = false;
    }

    // getters
    public String getBookID() {
        return bookID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() { return startDate; }

    public LocalDate getEndDate() { return endDate; }

    public Member getMember() { return member; }

    public Boolean getReturned() { return returned; }

    // methods
    public void returnBook () {
        returned = true;
    }

    public void soutDetails () {
        System.out.println("\nLoan details:");
        System.out.println("bookID: " + bookID);
        System.out.println("itemID: " + itemID);
        System.out.println("book title: " + title);
        System.out.println("start date: " + startDate);
        System.out.println("end date: " + endDate);
        System.out.println("returned: " + returned + "\n");
    }
}
