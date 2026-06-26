package com.Ressourcerie.ressourcerie;

import java.util.ArrayList;

import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;

public class SaveData{
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
    public boolean azertyMode;
}