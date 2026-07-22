package com.Ressourcerie.ressourcerie.results;

public class RepairResult extends ActionResult{

    public int energyCost;

    public RepairResult(boolean success, int energyCost, String message){
        super(success, message);
        this.energyCost = energyCost;
    }

}
