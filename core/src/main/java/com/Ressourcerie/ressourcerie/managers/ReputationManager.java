package com.Ressourcerie.ressourcerie.managers;

public class ReputationManager {

    public static String getReputationRank(int reputation){
        if (reputation < 30) {
            return "Mauvaise";
        } else if (reputation < 70) {
            return "Correcte";
        } else if (reputation < 90) {
            return "Excellente";
        } else {
            return "Legendaire";
        }
    }

}
