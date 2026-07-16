package com.Ressourcerie.ressourcerie.managers;

import java.util.ArrayList;
import java.util.Random;

import com.Ressourcerie.ressourcerie.items.Item;
import com.Ressourcerie.ressourcerie.config.GameBalance;


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

    public int getFinalRepairCost(Item item, int totalBonus) {

    return Math.max(GameBalance.MIN_REPAIR_COST, item.energyCost - totalBonus);

    }

    public boolean moveToSellingStock(Item item, ArrayList<Item> inventory, ArrayList<Item> sellingStock, int salePrice, int maxSellingStockSize) {
        
        if (item == null
            || inventory == null
            || sellingStock == null
            || item.salePrice < 1){
                return false;
            }

        if (!inventory.contains(item)) {
            return false;
        }
        
        if (sellingStock.size() >= maxSellingStockSize) {
            return false;
        }
        
        item.salePrice = salePrice;
        sellingStock.add(item);
        inventory.remove(item);
        return true;
    }

    public boolean isFullyRepaired(Item item) {
        return item.condition >= 100;
    }

    public boolean canBeSold(Item item, ArrayList<Item> sellingStock, int maxSellingStockSize, int salePrice){
        if (item == null) {
            return false;
        }
        if (sellingStock.size() >= maxSellingStockSize) {
            return false;
        }
        if (sellingStock.contains(item)) {
            return false;
        }
        if (salePrice <= 0) {
            return false;
        }
        return true;
    }
}
