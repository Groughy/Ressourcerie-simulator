package com.Ressourcerie.ressourcerie.items;

public class Item {

    public String name;
    public int condition;
    public int value;
    public String rarety;
    public int energyCost;

    public Item(String name, int condition, int value, String rarety, int energyCost) {
        this.name = name;
        this.condition = condition;
        this.value = value;
        this.rarety = rarety;
        this.energyCost = energyCost; 
    }
      public void repair() {
        condition += 10;

        if(condition > 100) {
            condition = 100;
        }
    }
}
