package com.Ressourcerie.ressourcerie.results;

public class SatisfactionResult {

    public final int reputationChange;
    public final int happyChange;
    public final int neutralChange;
    public final int unhappyChange;

    public SatisfactionResult(int reputationChange, int happyChange, int neutralChange, int unhappyChange){
        this.reputationChange = reputationChange;
        this.happyChange = happyChange;
        this.neutralChange = neutralChange;
        this.unhappyChange = unhappyChange;
    }

}
