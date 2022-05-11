package com.company;

import java.util.ArrayList;
import java.util.List;

public class Member extends Account{
    private final List<Loan> currentLoans = new ArrayList<>();
    private final List<Loan> previousLoans = new ArrayList<>();

    public Member(String name) {
        super(name, Accounts.MEMBER);
    }

    // Getters
    public List<Loan> getCurrentLoans() {
        return currentLoans;
    }

    // Methods
    public void addToCurrentLoans(Loan loan) {
        currentLoans.add(loan);
    }

    public void addToPreviousLoans(Loan loan) {
        currentLoans.remove(loan);
        previousLoans.add(loan);
    }
}
