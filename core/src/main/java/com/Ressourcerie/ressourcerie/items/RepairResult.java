package com.Ressourcerie.ressourcerie.items;

public class RepairResult {

    public boolean success;
    public int energyCost;
    public String message;

    public RepairResult(boolean success, int energyCost, String message){
        this.success = success;
        this.energyCost = energyCost;
        this.message = message;
    }

}
