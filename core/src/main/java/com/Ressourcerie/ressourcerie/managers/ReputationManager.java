package com.Ressourcerie.ressourcerie.managers;

import com.Ressourcerie.ressourcerie.config.GameBalance;

public class ReputationManager {

    public static String getReputationRank(int reputation){
        if (reputation < GameBalance.BAD_REPUTATION_THRESHOLD) {
            return "Mauvaise";
        } else if (reputation < GameBalance.GOOD_REPUTATION_THRESHOLD) {
            return "Correcte";
        } else if (reputation < GameBalance.EXCELLENT_REPUTATION_THRESHOLD) {
            return "Excellente";
        } else {
            return "Légendaire";
        }
    }

}
