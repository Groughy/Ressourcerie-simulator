package com.Ressourcerie.ressourcerie.managers;
package com.Ressourcerie.ressourcerie;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SavaManager {

    public void saveGame(){
        SaveData data = new SaveData();

        data.money = money;
        data.day = day;
        data.energy = energy;
        data.maxEnergy = maxEnergy;
        data.reputation = reputation;

        data.selectedIndex = selectedIndex;

        data.maxInventorySize = maxInventorySize;
        data.maxSellingStockSize = maxSellingStockSize;

        data.electronicWorkshopLevel = electronicWorkshopLevel;
        data.mechanicalWorkshopLevel = mechanicalWorkshopLevel;
        data.woodWorkshopLevel = woodWorkshopLevel;
        data.decorationWorkshopLevel = decorationWorkshopLevel;
        data.textileWorkshopLevel = textileWorkshopLevel;

        data.inventory = Inventory;
        data.sellingStock = sellingStock;
        data.employees = employees;
        data.selectedEmployeeIndex = selectedEmployeeIndex;
        data.storageLevel = storageLevel;
        data.storageUpgradeCost = storageUpgradeCost;

        Json json = new Json();
        FileHandle file = Gdx.files.local("save.json");

        file.writeString(json.prettyPrint(data), false);

        message = "Partie sauvegardée.";
    }

    public void loadGame(){
        FileHandle file = Gdx.files.local("save.json");

        if(!file.exists()){
            message = "Aucune sauvegarde trouvée.";
            return;
        }

        Json json = new Json();
        SaveData data = json.fromJson(SaveData.class, file.readString());

        money = data.money;
        day = data.day;
        energy = data.energy;
        maxEnergy = data.maxEnergy;
        reputation = data.reputation;

        selectedIndex = data.selectedIndex;

        maxInventorySize = data.maxInventorySize;
        maxSellingStockSize = data.maxSellingStockSize;

        electronicWorkshopLevel = data.electronicWorkshopLevel;
        mechanicalWorkshopLevel = data.mechanicalWorkshopLevel;
        woodWorkshopLevel = data.woodWorkshopLevel;
        decorationWorkshopLevel = data.decorationWorkshopLevel;
        textileWorkshopLevel = data.textileWorkshopLevel;

        Inventory = data.inventory;
        sellingStock = data.sellingStock;
        employees = data.employees;
        selectedEmployeeIndex = data.selectedEmployeeIndex;

        if (employees == null){
            employees = new ArrayList<>();
        }

        if (selectedEmployeeIndex >= employees.size()){
            selectedEmployeeIndex = 0;
        }

        storageLevel = data.storageLevel;
        storageUpgradeCost = data.storageUpgradeCost;

        message = "partie chargée.";
    }


}
