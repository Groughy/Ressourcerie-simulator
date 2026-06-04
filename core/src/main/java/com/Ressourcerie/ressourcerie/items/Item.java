package com.Ressourcerie.ressourcerie.items;

public class Item {

    public String name;
    public int condition;
    public int value;
    public int salePrice;
    public String rarety;
    public int energyCost;
    public String type;

    public Item(String name, int condition, int value, int salePrice, String rarety, int energyCost, String type) {
        this.name = name;
        this.condition = condition;
        this.value = value;
        this.salePrice = salePrice;
        this.rarety = rarety;
        this.energyCost = energyCost;
        this.type = type;
    }
    public void repair(int amount) {
        condition += amount;

        if(condition > 100) {
            condition = 100;
        }
    }
    
    public Item(){
    }
}
