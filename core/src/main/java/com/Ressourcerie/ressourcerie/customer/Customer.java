package com.Ressourcerie.ressourcerie.customer;

public class Customer {

    public String name;
    public int budget;
    public String wantedItems;
    public String customerType;

    public Customer(String name, int budget, String wantedItems, String customerType) {
        this.name = name;
        this.budget = budget;
        this.wantedItems = wantedItems;
        this.customerType = customerType;
    }

}
