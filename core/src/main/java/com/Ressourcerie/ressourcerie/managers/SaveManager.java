package com.Ressourcerie.ressourcerie.managers;

import java.util.ArrayList;
import com.Ressourcerie.ressourcerie.SaveData;
import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SaveManager {

    public int money;
    public int day;
    public int energy;
    public int maxEnergy;
    public int reputation;

    public int selectedIndex;

    public int maxInventorySize;
    public int maxSellingStockSize;

    public int electronicWorkshopLevel;
    public int mechanicalWorkshopLevel;
    public int woodWorkshopLevel;
    public int decorationWorkshopLevel;
    public int textileWorkshopLevel;

    public int storageLevel;
    public int storageUpgradeCost;

    public ArrayList<Item> Inventory;
    public ArrayList<Item> sellingStock;

    public ArrayList<Employee> employees;
    public int selectedEmployeeIndex;
    public String message;

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

        data.Inventory = Inventory;
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

        Inventory = data.Inventory;
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
