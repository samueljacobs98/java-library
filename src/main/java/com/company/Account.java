package com.company;

import java.util.UUID;

public abstract class Account {
    private String name;
    private final String iD;
    private Accounts accountType;

    public Account(String name, Accounts accountType) {
        this.name = name;
        this.iD = UUID.randomUUID().toString();
        this.accountType = accountType;
    }

    public String getiD() {
        return iD;
    }
}
