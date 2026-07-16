package com.Ressourcerie.ressourcerie.managers;

import java.util.ArrayList;
import java.util.Random;

import com.Ressourcerie.ressourcerie.items.Item;
import com.Ressourcerie.ressourcerie.config.GameBalance;
import com.Ressourcerie.ressourcerie.MainScreen;


public class ItemManager {

    private final Random random = new Random();
    private int reputation;

    public Item createRandomItem() {
        String[] names = { "Vieille radio", "Lampe cassée", "Chaise en bois", "Vieux vélo", "Table abîmée",
                "Ordinateur obsolète", "Télévision ancienne", "Machine à écrire", "Guitare désaccordée", "Canapé usé",
                "Vase ébréché", "Montre cassée", "Appareil photo vintage", "Jouet en bois", "Livre ancien" };
        String name = names[random.nextInt(names.length)];
        String type = getTypeFromName(name);
        int conditions;
        if (reputation < GameBalance.BAD_REPUTATION_THRESHOLD){
            conditions = random.nextInt(41) + 10;
        }
        else if (reputation < GameBalance.GOOD_REPUTATION_THRESHOLD){
            conditions = random.nextInt(61) + 20;
        }
        else {
            conditions = random.nextInt(41) + 60;
        }
        int values = random.nextInt(41) + 10;
        String rarity;
        int salePrice = values;
        int rarityRoll = random.nextInt(100);
        if (reputation > 80){
            rarityRoll += 10;
        }
        if (reputation > GameBalance.EXCELLENT_REPUTATION_THRESHOLD){
            rarityRoll += 10;
        }
        if (rarityRoll < 50) {
            rarity = "Commun";
        } else if (rarityRoll < 80) {
            rarity = "Rare";
        } else if (rarityRoll < 95) {
            rarity = "Épique";
        } else {
            rarity = "Légendaire";
        }

        int energyCost = random.nextInt(21) + 5; //
        return new Item(name, conditions, values, salePrice, rarity, energyCost, type);
    }

       public String getTypeFromName(String name) {
        if (name.equals("Vieille radio")
                || name.equals("Lampe cassée")
                || name.equals("Ordinateur obsolète")
                || name.equals("Télévision ancienne")
                || name.equals("Appareil photo vintage")) {
            return "Electronique";
        }
        if (name.equals("Chaise en bois")
                || name.equals("Table abîmée")) {
            return "Mobilier";
        }
        if (name.equals("Vieux vélo")
                || name.equals("Machine à écrire")
                || name.equals("Montre cassée")) {
            return "Mécanique";
        }
        if (name.equals("Canapé usé")) {
            return "Textile";
        }
        if (name.equals("Guitare désaccordée")
                || name.equals("Jouet en bois")
                || name.equals("Livre ancien")
                || name.equals("Vase ébréché")) {
            return "Décoration";
        }
        return "Divers";
    }

    public int getFinalRepairCost(Item item, int repairBonus) {
        int workshopLevel = MainScreen.getWorkshopLevelForItem(item);
        int workshopBonus = workshopLevel - 1;
        int finalCost = item.energyCost - repairBonus - workshopBonus;
        if (finalCost < 1) {
            finalCost = 1;
        }
        return finalCost;
    }

}
