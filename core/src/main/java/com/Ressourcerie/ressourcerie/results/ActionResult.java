package com.Ressourcerie.ressourcerie.results;

public class ActionResult {

    public final boolean success;
    public final String message;

    protected ActionResult(boolean success, String message){
        this.success = success;
        this.message = message;
    }

}
