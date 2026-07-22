package com.Ressourcerie.ressourcerie;

import java.util.ArrayList;
import java.util.Random;

import com.Ressourcerie.ressourcerie.customer.Customer;
import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;

import com.Ressourcerie.ressourcerie.results.RepairResult;
import com.Ressourcerie.ressourcerie.results.SaleResult;

import com.Ressourcerie.ressourcerie.managers.SaveManager;
import com.Ressourcerie.ressourcerie.managers.CustomerManager;
import com.Ressourcerie.ressourcerie.managers.EmployeeManager;
import com.Ressourcerie.ressourcerie.managers.ItemManager;

import com.Ressourcerie.ressourcerie.config.GameBalance;

import com.Ressourcerie.ressourcerie.input.GameKeys;

import com.Ressourcerie.ressourcerie.ui.HudRenderer;
import com.Ressourcerie.ressourcerie.ui.InventoryRenderer;
import com.Ressourcerie.ressourcerie.ui.CustomerInfoRenderer;
import com.Ressourcerie.ressourcerie.ui.MessageRenderer;
import com.Ressourcerie.ressourcerie.ui.WorkshopRenderer;
import com.Ressourcerie.ressourcerie.ui.EmployeeRenderer;
import com.Ressourcerie.ressourcerie.ui.StockRenderer;
import com.Ressourcerie.ressourcerie.ui.SaleMenuRenderer;
import com.Ressourcerie.ressourcerie.ui.HelpRenderer;
import com.Ressourcerie.ressourcerie.ui.DayReportRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class MainScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgroundTexture;
    private Texture panelLeftTexture;
    private Texture panelRightTexture;

    private HudRenderer hudRenderer;
    private InventoryRenderer inventoryRenderer;
    private CustomerInfoRenderer customerInfoRenderer;
    private MessageRenderer messageRenderer;
    private WorkshopRenderer workshopRenderer;
    private EmployeeRenderer employeeRenderer;
    private StockRenderer stockRenderer;
    private SaleMenuRenderer saleMenuRenderer;
    private HelpRenderer helpRenderer;
    private DayReportRenderer dayReportRenderer;

    private ItemManager itemManager;


    private int selectedIndex = 0;
    private ArrayList<Item> Inventory;
    private int money = GameBalance.STARTING_MONEY;
    private int day = 1;
    private Random random = new Random();
    private int energy = GameBalance.MAX_ENERGY;
    private int maxEnergy = GameBalance.MAX_ENERGY;
    private String message = "";
    private ArrayList<Item> sellingStock;
    private ArrayList<Employee> employees;
    private Customer currentCustomer;
    private int reputation = GameBalance.STARTING_REPUTATION;
    private int happyCustomers = 0;
    private int unhappyCustomers = 0;
    private int neutralCustomers = 0;

    private boolean dayReport = false;
    private boolean showWorkshopMenu = false;
    private boolean showSaleMenu = false;
    private boolean showStockMenu = false;
    private boolean showHelpMenu = false;
    private boolean showEmployeeMenu = false;

    private int dailyMoneyEarned = 0;
    private int dailyItemsSold = 0;
    private int dailyReputationChange = 0;
    private int dailyHappyCustomers = 0;
    private int dailyNeutralCustomers = 0;
    private int dailyUnhappyCustomers = 0;
    private int dailyItemsReceived = 0;
    private int dailyItemsRepairedByEmployees = 0;
    private int dailyEmployeeMistakes = 0;
    private int dailySalariesPaid = 0;
    private int dailySalesRefused = 0;
    private int dailyMoneySpent = 0;
    private int dailyProfit = 0;

    private int coffeeEnergyBoost = GameBalance.COFFEE_ENERGY_BOOST;
    private int coffeeCost = GameBalance.COFFEE_COST;
    private int repairKitCost = GameBalance.REPAIR_KIT_COST;
    private int repairBonus = 0;
    private int electronicWorkshopLevel = 2;
    private int woodWorkshopLevel = 2;
    private int mechanicalWorkshopLevel = 2;
    private int decorationWorkshopLevel = 1;
    private int textileWorkshopLevel = 0;
    private int currentSalePrice = 0;
    private int maxInventorySize = GameBalance.STARTING_INVENTORY_CAPACITY;
    private int maxSellingStockSize = GameBalance.STARTING_SELLING_CAPACITY;
    private int storageLevel = 1;
    private int storageUpgradeCost = GameBalance.STORAGE_UPGRADE_COST;
    private int selectedEmployeeIndex = 0;
    private SaveManager saveManager = new SaveManager();
    private CustomerManager customerManager = new CustomerManager();
    private EmployeeManager employeeManager = new EmployeeManager();

    @Override
    public void show() {
        random = new Random();
        batch = new SpriteBatch();
        font = new BitmapFont();

        Assets.load();
        backgroundTexture = Assets.backgroundTexture;
        panelLeftTexture = Assets.panelLeftTexture;
        panelRightTexture = Assets.panelRightTexture;
        
        hudRenderer = new HudRenderer();
        inventoryRenderer = new InventoryRenderer();
        customerInfoRenderer = new CustomerInfoRenderer();
        messageRenderer = new MessageRenderer();
        workshopRenderer = new WorkshopRenderer();
        employeeRenderer = new EmployeeRenderer();
        stockRenderer = new StockRenderer();
        saleMenuRenderer = new SaleMenuRenderer();
        helpRenderer = new HelpRenderer();
        dayReportRenderer = new DayReportRenderer();

        itemManager = new ItemManager();

        Inventory = new ArrayList<>();
        sellingStock = new ArrayList<>();
        employees = new ArrayList<>();

        customerManager.reputation = reputation;
        currentCustomer = customerManager.createRandomCustomer();

        Inventory.add(new Item("Vieille radio", 45, 20, 20, "Commun", 10, "Electronique"));
        Inventory.add(new Item("Chaise en bois", 70, 15, 20, "Commun", 5, "Mobilier"));
        Inventory.add(new Item("Vieux vélo", 30, 50, 20, "Commun", 15, "Mécanique"));
        dailyItemsReceived = Inventory.size();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (dayReport) {
            batch.begin();

            dayReportRenderer.render(batch, font, day, dailyMoneyEarned, dailyMoneySpent, dailyItemsSold,
                    dailyItemsReceived, dailyHappyCustomers, dailyNeutralCustomers, dailyUnhappyCustomers,
                    dailySalesRefused, dailySalariesPaid, dailyReputationChange);


            
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                startNextDay();
            }
            
            return;
        }

        handleInputs();

        if (showWorkshopMenu) {
            renderWorkshopMenu();
            return;
        }

        if (showStockMenu) {
            renderStockMenu();
            return;
        }

        if (showSaleMenu) {
            renderSaleMenu();
            return;
        }

        if (showEmployeeMenu) {

            handleEmployeeMenuInput();
            renderEmployeeMenu();
            return;
        }

        if (showHelpMenu) {
            renderHelpMenu();
            return;
        }

        handleInventorySelection();

        if (Inventory.isEmpty() && Gdx.input.isKeyJustPressed(GameKeys.SPACE)) {
            endDay();
            renderDayReport();
            return;
        }

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(panelLeftTexture, 0, 80);
        float rightX = Gdx.graphics.getWidth() - panelRightTexture.getWidth();
        batch.draw(panelRightTexture, rightX, 80);
        font.draw(batch, "F1 = Aide | S = Stock | A = Ateliers", 20, 475);

        int statsX = 430;

        hudRenderer.render(batch, font, day, money, energy, maxEnergy, reputation);
        font.draw(batch, "Clients ravis : " + happyCustomers, statsX, 330);
        font.draw(batch, "Clients neutres : " + neutralCustomers, statsX, 300);
        font.draw(batch, "Clients decus : " + unhappyCustomers, statsX, 270);
        font.draw(batch, "Employes : " + employees.size(), statsX, 240);

        inventoryRenderer.render(batch, font, Inventory, selectedIndex);
        customerInfoRenderer.render(batch, font, currentCustomer);
        messageRenderer.render(batch, font, message);

        if (Inventory.isEmpty()) {
            font.draw(batch, "Aucun objet en stock. Appuyez sur ESPACE pour passer au jour suivant.", 100, 300);
        }

        batch.end();

    }

    private void endDay() {
        for (Employee employee : employees) {
            int repairResult = employeeManager.repairEmployee(employee, Inventory);
            if (repairResult > 0) {
                dailyItemsRepairedByEmployees++;
            } else if (repairResult < 0) {
                dailyEmployeeMistakes++;
            }
        }

        int salaries = employeeManager.getTotalSalaries(employees);
        money -= salaries;
        dailySalariesPaid = salaries;
        dailyMoneySpent += salaries;

        processCustomersToday();
        dailyProfit = dailyMoneyEarned - dailyMoneySpent;
        dayReport = true;
    }

    private void startNextDay() {
        resetDailyStats();
        energy = maxEnergy;
        selectedIndex = 0;
        repairBonus = 0;
        day++;
        generateNewItems();
        customerManager.reputation = reputation;
        currentCustomer = customerManager.createRandomCustomer();
        dayReport = false;

        saveGame();
    }

    private void BuyFromCustomer() {
        currentCustomer = customerManager.getOrCreateCustomer(currentCustomer, reputation);

        SaleResult result = customerManager.tryBuyItem(currentCustomer, sellingStock);

        message = result.message;

        if (result.refused){
            dailySalesRefused++;
            return;
        }

        if (!result.success){
            return;
        }

            sellItemToCustomer(result.soldItem, result.soldIndex);
    }

    private void sellItemToCustomer(Item item, int index){
        money += item.salePrice;
        dailyMoneyEarned += item.salePrice;
        dailyItemsSold++;
        if (customerManager.isGoodDeal(item)){
            reputation++;
            dailyReputationChange++;
        }
        if (customerManager.givesCollectorBonus(currentCustomer, item)) {
            money += GameBalance.COLLECTOR_BONUS;
            dailyMoneyEarned += GameBalance.COLLECTOR_BONUS;
        }

        applyCustomerSatisfaction(item);

        customerManager.removeSoldItem(sellingStock, index);

        message = currentCustomer.name + " a acheté " + item.name + " pour " + item.salePrice + " euros.";
    }

    private void applyCustomerSatisfaction(Item item){
        if (item.condition >=70){
            reputation += GameBalance.HAPPY_REPUTATION_GAIN;
            happyCustomers++;
            dailyHappyCustomers++;
            dailyReputationChange += GameBalance.HAPPY_REPUTATION_GAIN;
        } else if (item.condition >=40){
            neutralCustomers++;
            dailyNeutralCustomers++;
        } else {
            reputation -= GameBalance.UNHAPPY_REPUTATION_LOSS;
            unhappyCustomers++;
            dailyUnhappyCustomers++;
            dailyReputationChange -= GameBalance.UNHAPPY_REPUTATION_LOSS;
        }
        reputation = Math.max(0, Math.min(100, reputation));
    }

    private boolean canRepair(Item item) {
        if (item.type.equals("Electronique")) {
            return electronicWorkshopLevel > 1;
        }
        if (item.type.equals("Mécanique")) {
            return mechanicalWorkshopLevel > 1;
        }
        if (item.type.equals("Mobilier")) {
            return woodWorkshopLevel > 1;
        }
        if (item.type.equals("Textile")) {
            return textileWorkshopLevel > 1;
        }
        if (item.type.equals("Décoration") || item.type.equals("Divers")) {
            return decorationWorkshopLevel > 1;
        }
        return false;
    }

    private int getWorkshopUpgradeCost(int level) {
        return 100 * level;
    }

    private int upgradeWorkshop(int currentLevel, String workshopName) {
        int cost = getWorkshopUpgradeCost(currentLevel);

        if (money < cost) {
            message = "Pas assez d'argent pour améliorer " + workshopName + ".";
            return currentLevel;
        }

        money -= cost;
        dailyMoneySpent += cost;
        currentLevel++;

        message = workshopName + " amélioré niveau " + currentLevel + " !";
        return currentLevel;
    }

    private int getWorkshopLevelForItem(Item item) {
        if (item.type.equals("Electronique")) {
            return electronicWorkshopLevel;
        }
        if (item.type.equals("Mécanique")) {
            return mechanicalWorkshopLevel;
        }
        if (item.type.equals("Mobilier") || item.type.equals("Meuble")) {
            return woodWorkshopLevel;
        }
        if (item.type.equals("Textile")) {
            return textileWorkshopLevel;
        }
        if (item.type.equals("Décoration") || item.type.equals("Divers")) {
            return decorationWorkshopLevel;
        }
        return 0;
    }

    private int getCustomersPerDay() {
        if (reputation < GameBalance.BAD_REPUTATION_THRESHOLD) {
            return 1;
        } else if (reputation < GameBalance.GOOD_REPUTATION_THRESHOLD) {
            return 2;
        } else if (reputation < GameBalance.EXCELLENT_REPUTATION_THRESHOLD) {
            return 3;
        } else {
            return 4;
        }
    }

    private void handleInventorySelection(){

        if (Inventory.isEmpty()){
            return;
        }

         if (Gdx.input.isKeyJustPressed(GameKeys.DOWN)) {
            selectedIndex++;

            if (selectedIndex >= Inventory.size()) {
                selectedIndex = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.UP)) {
            selectedIndex--;

            if (selectedIndex < 0) {
                selectedIndex = Inventory.size() - 1;
            }
        }
    }

    private void buyCoffee(){
        if (energy >= maxEnergy) {
            message = "Votre énergie est déjà au maximum.";
            return;
        }
        if (money >= coffeeCost) {
            money -= coffeeCost;
            dailyMoneySpent += coffeeCost;
            int restoredEnergy = Math.min(coffeeEnergyBoost, maxEnergy - energy);
            energy += restoredEnergy;
            message = "Vous avez bu un café. Énergie restaurée de " + restoredEnergy + ".";
        } else {
            message = "Pas assez d'argent pour acheter un café.";
        }
    }

    private void buyRepairKit(){
        if (money >= GameBalance.REPAIR_KIT_COST) {
                money -= GameBalance.REPAIR_KIT_COST;
                dailyMoneySpent += GameBalance.REPAIR_KIT_COST;
                repairBonus += GameBalance.REPAIR_KIT_BONUS;
                message = "Vous avez acheté un kit de réparation. -5 energie sur les réparations d'aujourd'hui.";
            } else {
                message = "Pas assez d'argent pour acheter un kit de réparation.";
            }
    }

    private void renderDayReport(){
        batch.begin();
            int x = 100;
            int y = 420;
            int line = 24;
            font.draw(batch, "Rapport du jour " + day + " : ", x, y);
            y -= line;
            font.draw(batch, "Argent gagné : " + dailyMoneyEarned + "euros", x, y);
            y -= line;
            font.draw(batch, "Objets vendus : " + dailyItemsSold, x, y);
            y -= line;
            font.draw(batch,
                    "Changement de réputation : " + (dailyReputationChange >= 0 ? "+" : "") + dailyReputationChange,
                    x, y);
            y -= line;
            font.draw(batch,
                    "Clients ravis : " + dailyHappyCustomers,
                    x,
                    y);
            y -= line;
            font.draw(batch,
                    "Clients neutres : " + dailyNeutralCustomers,
                    x,
                    y);
            y -= line;
            font.draw(batch,
                    "Clients decus : " + dailyUnhappyCustomers,
                    x,
                    y);
            y -= line;
            font.draw(batch, "Objets recus : " + dailyItemsReceived, x, y);
            y -= line;
            font.draw(batch, "Objets repares par les employes : " + dailyItemsRepairedByEmployees, x, y);
            y -= line;
            font.draw(batch, "Erreurs des employes : " + dailyEmployeeMistakes, x, y);
            y -= line;
            font.draw(batch, "Argent depense : " + dailyMoneySpent + " euros", x, y);
            y -= line;
            font.draw(batch, "Salaires payes : " + dailySalariesPaid + " euros", x, y);
            y -= line;
            font.draw(batch, "Ventes refusees : " + dailySalesRefused, x, y);
            y -= line;
            font.draw(batch, "Bénéfice net : " + dailyProfit + " euros", x, y);
            y -= line;
            font.draw(batch, "Appuyez sur ENTRÉE pour continuer.", x, y);

            batch.end();
    }

    private void renderHelpMenu(){
        batch.begin();

            helpRenderer.render(batch, font);

            batch.end();
    }

    private void renderWorkshopMenu(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                electronicWorkshopLevel = upgradeWorkshop(electronicWorkshopLevel, "Atelier electronique");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                mechanicalWorkshopLevel = upgradeWorkshop(mechanicalWorkshopLevel, "Atelier mecanique");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                woodWorkshopLevel = upgradeWorkshop(woodWorkshopLevel, "Atelier mobilier");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                decorationWorkshopLevel = upgradeWorkshop(decorationWorkshopLevel, "Atelier decoration");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                textileWorkshopLevel = upgradeWorkshop(textileWorkshopLevel, "Atelier Textile");
            }

            batch.begin();
            workshopRenderer.render(batch,
                 font,
                 electronicWorkshopLevel,
                 mechanicalWorkshopLevel, 
                 woodWorkshopLevel, 
                 decorationWorkshopLevel, 
                 textileWorkshopLevel);
                 batch.end();
            
    }

    private void handleEmployeeMenuInput(){
        if (!employees.isEmpty() && Gdx.input.isKeyJustPressed(GameKeys.UP)){
            selectedEmployeeIndex--;

            if (selectedEmployeeIndex < 0){
                selectedEmployeeIndex = employees.size() -1;
            }
        }

        if (!employees.isEmpty() && Gdx.input.isKeyJustPressed(GameKeys.DOWN)){
            selectedEmployeeIndex++;

            if (selectedEmployeeIndex >= employees.size()){
                selectedEmployeeIndex = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            recruitEmployee();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            trainSelectedEmployee();
        }
    }

    private void renderEmployeeMenu(){

        batch.begin();

        employeeRenderer.render(batch, font, employees, selectedEmployeeIndex);

        batch.end();
    }

    private void renderStockMenu(){
        
        batch.begin();

            stockRenderer.render(batch, font, Inventory, sellingStock, maxInventorySize, maxSellingStockSize, storageLevel, storageUpgradeCost);

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
                if (money >= storageUpgradeCost) {
                    money -= storageUpgradeCost;
                    dailyMoneySpent += storageUpgradeCost;
                    storageLevel++;
                    maxInventorySize += GameBalance.STORAGE_CAPACITY_GAIN;
                    maxSellingStockSize += GameBalance.STORAGE_CAPACITY_GAIN;
                    storageUpgradeCost += GameBalance.STORAGE_UPGRADE_COST;
                    message = "Entrepot amélioré à niveau " + storageLevel + " ! Capacité augmentée.";
                } else {
                    message = "Pas assez d'argent pour améliorer l'entrepot.";
                }
            }

            batch.end();
    }

    private void renderSaleMenu() {
    if (Inventory.isEmpty()) {
        showSaleMenu = false;
        message = "Aucun objet à mettre en vente.";
        return;
    }

    selectedIndex = Math.max(
            0,
            Math.min(selectedIndex, Inventory.size() - 1)
    );

    Item selectedItem = Inventory.get(selectedIndex);

    if (Gdx.input.isKeyJustPressed(GameKeys.PLUS)
            || Gdx.input.isKeyJustPressed(GameKeys.EQUALS)) {
        currentSalePrice += 5;
    }

    if (Gdx.input.isKeyJustPressed(GameKeys.MINUS)) {
        currentSalePrice = Math.max(1, currentSalePrice - 5);
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

        if (sellingStock.size() >= maxSellingStockSize) {
            message = "Le stock de vente est plein.";
            showSaleMenu = false;
            return;
        }

        boolean moved = itemManager.moveToSellingStock(
                selectedItem,
                Inventory,
                sellingStock,
                currentSalePrice,
                maxSellingStockSize
        );

        if (!moved) {
            message = "Impossible de mettre cet objet en vente.";
            return;
        }

        if (Inventory.isEmpty()) {
            selectedIndex = 0;
        } else {
            selectedIndex = Math.min(
                    selectedIndex,
                    Inventory.size() - 1
            );
        }

        showSaleMenu = false;
        message = "Objet mis en vente.";
        return;
    }

    if (Gdx.input.isKeyJustPressed(GameKeys.CANCEL)) {
        showSaleMenu = false;
        return;
    }

    batch.begin();
    saleMenuRenderer.render(
            batch,
            font,
            selectedItem,
            currentSalePrice
    );
    batch.end();
}

    private void repairItem(){
        Item selectedItem = Inventory.get(selectedIndex);
        
        if (!canRepair(selectedItem)){
            message = "L'atelier correspondant n'est pas débloqué.";
            return;
        }

        int workshopLevel = getWorkshopLevelForItem(selectedItem);

        int totalBonus = repairBonus + Math.max(0, workshopLevel -1);
        int repairAmount = itemManager.getRepairAmount(selectedItem, workshopLevel);

        RepairResult result = itemManager.repairItem(selectedItem, energy, totalBonus, repairAmount);

        message = result.message;

        if (result.success){
            energy -= result.energyCost;
        }
    }

    private void handleInputs(){
        if (showWorkshopMenu) {
            if (Gdx.input.isKeyJustPressed(GameKeys.WORKSHOP)
                    || Gdx.input.isKeyJustPressed(GameKeys.CANCEL)) {
                showWorkshopMenu = false;
            }
            return;
        }
        if (showStockMenu) {
            if (Gdx.input.isKeyJustPressed(GameKeys.STOCK)
                    || Gdx.input.isKeyJustPressed(GameKeys.CANCEL)) {
                showStockMenu = false;
            }
            return;
        }
        if (showEmployeeMenu) {
            if (Gdx.input.isKeyJustPressed(GameKeys.EMPLOYEE)
                    || Gdx.input.isKeyJustPressed(GameKeys.CANCEL)) {
                showEmployeeMenu = false;
            }
            return;
        }
        if (showHelpMenu) {
            if (Gdx.input.isKeyJustPressed(GameKeys.HELP)
                    || Gdx.input.isKeyJustPressed(GameKeys.CANCEL)) {
                showHelpMenu = false;
            }
            return;
        }
        if (showSaleMenu) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.KIT)) {
            buyRepairKit();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.AZERTYMODE)){
            GameKeys.switchKeyboardMode();
            message = "Mode clavier : " + (GameKeys.azertyMode ? "AZERTY" : "QWERTY");
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.EMPLOYEE)) {
            showEmployeeMenu = !showEmployeeMenu;
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.HELP)) {
            showHelpMenu = !showHelpMenu;
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.SAVE)){
            saveGame();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.LOAD)){
            loadGame();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.COFFEE)) {
            buyCoffee();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.REPAIR)) {

            repairItem();
            return;
        }

         if (Gdx.input.isKeyJustPressed(GameKeys.WORKSHOP)) {
            showWorkshopMenu = !showWorkshopMenu;
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.STOCK)) {
            showStockMenu = !showStockMenu;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            BuyFromCustomer();
            customerManager.reputation = reputation;
            currentCustomer = customerManager.createRandomCustomer();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.SELL)) {

            if (Inventory.isEmpty()) {
                message = "Aucun objet à mettre en vente.";
                return;
            }

            if (sellingStock.size() >= maxSellingStockSize) {
                message = "Stock de vente plein.";
                return;
            }

            if (selectedIndex >= Inventory.size()) {
                selectedIndex = Inventory.size() - 1;
            }

            if (selectedIndex < 0) {
                selectedIndex = 0;
            }

            currentSalePrice = itemManager.getDefaultSalePrice(Inventory.get(selectedIndex));
            showSaleMenu = true;
        }
    }

    private void recruitEmployee() {
        if (money < GameBalance.EMPLOYEE_RECRUIT_COST) {
            message = "Pas assez d'argent pour recruter un employé.";
            return;
        }

        money -= GameBalance.EMPLOYEE_RECRUIT_COST;
        dailyMoneySpent += GameBalance.EMPLOYEE_RECRUIT_COST;
        employees.add(employeeManager.createEmployee(employees.size() + 1));
        selectedEmployeeIndex = employees.size() - 1;
        message = "Nouvel employé recruté.";
    }

    private void trainSelectedEmployee() {
        if (employees.isEmpty()) {
            message = "Aucun employé à former.";
            return;
        }
        if (money < GameBalance.EMPLOYEE_TRAINING_COST) {
            message = "Pas assez d'argent pour former cet employé.";
            return;
        }

        selectedEmployeeIndex = Math.max(0, Math.min(selectedEmployeeIndex, employees.size() - 1));
        money -= GameBalance.EMPLOYEE_TRAINING_COST;
        dailyMoneySpent += GameBalance.EMPLOYEE_TRAINING_COST;
        employeeManager.trainEmployee(employees.get(selectedEmployeeIndex));
        message = employees.get(selectedEmployeeIndex).name + " a été formé.";
    }

    private void saveGame() {
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
        data.storageLevel = storageLevel;
        data.storageUpgradeCost = storageUpgradeCost;
        data.Inventory = Inventory;
        data.sellingStock = sellingStock;
        data.employees = employees;
        data.selectedEmployeeIndex = selectedEmployeeIndex;
        data.azertyMode = GameKeys.azertyMode;
        data.currentCustomer = currentCustomer;
        data.happyCustomers = happyCustomers;
        data.neutralCustomers = neutralCustomers;
        data.unhappyCustomers = unhappyCustomers;
        data.repairBonus = repairBonus;
        data.dailyMoneyEarned = dailyMoneyEarned;
        data.dailyItemsSold = dailyItemsSold;
        data.dailyReputationChange = dailyReputationChange;
        data.dailyHappyCustomers = dailyHappyCustomers;
        data.dailyNeutralCustomers = dailyNeutralCustomers;
        data.dailyUnhappyCustomers = dailyUnhappyCustomers;
        data.dailyItemsReceived = dailyItemsReceived;
        data.dailyItemsRepairedByEmployees = dailyItemsRepairedByEmployees;
        data.dailyEmployeeMistakes = dailyEmployeeMistakes;
        data.dailySalariesPaid = dailySalariesPaid;
        data.dailySalesRefused = dailySalesRefused;
        data.dailyMoneySpent = dailyMoneySpent;
        data.dailyProfit = dailyProfit;

        saveManager.saveGame(data);
        message = saveManager.getMessage();
    }

    private void loadGame() {
        SaveData data = saveManager.loadGame();
        if (data == null) {
            message = saveManager.getMessage();
            return;
        }

        money = data.money;
        day = Math.max(1, data.day);
        maxEnergy = data.maxEnergy > 0 ? data.maxEnergy : 100;
        energy = Math.max(0, Math.min(data.energy, maxEnergy));
        reputation = Math.max(0, Math.min(100, data.reputation));
        maxInventorySize = data.maxInventorySize > 0 ? data.maxInventorySize : 10;
        maxSellingStockSize = data.maxSellingStockSize > 0 ? data.maxSellingStockSize : 10;
        electronicWorkshopLevel = Math.max(0, data.electronicWorkshopLevel);
        mechanicalWorkshopLevel = Math.max(0, data.mechanicalWorkshopLevel);
        woodWorkshopLevel = Math.max(0, data.woodWorkshopLevel);
        decorationWorkshopLevel = Math.max(0, data.decorationWorkshopLevel);
        textileWorkshopLevel = Math.max(0, data.textileWorkshopLevel);
        storageLevel = data.storageLevel > 0 ? data.storageLevel : 1;
        storageUpgradeCost = data.storageUpgradeCost > 0 ? data.storageUpgradeCost : 120;

        Inventory = data.Inventory != null ? data.Inventory : new ArrayList<>();
        sellingStock = data.sellingStock != null ? data.sellingStock : new ArrayList<>();
        employees = data.employees != null ? data.employees : new ArrayList<>();
        normalizeLoadedItems(Inventory);
        normalizeLoadedItems(sellingStock);
        normalizeLoadedEmployees();
        maxInventorySize = Math.max(maxInventorySize, Inventory.size());
        maxSellingStockSize = Math.max(maxSellingStockSize, sellingStock.size());

        selectedIndex = Inventory.isEmpty()
            ? 0
            : Math.max(0, Math.min(data.selectedIndex, Inventory.size() - 1));
        selectedEmployeeIndex = employees.isEmpty()
            ? 0
            : Math.max(0, Math.min(data.selectedEmployeeIndex, employees.size() - 1));

        currentCustomer = data.currentCustomer;
        if (currentCustomer == null) {
            customerManager.reputation = reputation;
            currentCustomer = customerManager.createRandomCustomer();
        } else {
            if (currentCustomer.name == null || currentCustomer.name.isEmpty()) {
                currentCustomer.name = "Client";
            }
            currentCustomer.budget = Math.max(0, currentCustomer.budget);
            if (currentCustomer.wantedItems == null) {
                currentCustomer.wantedItems = "";
            }
            if (currentCustomer.customerType == null || currentCustomer.customerType.isEmpty()) {
                currentCustomer.customerType = "Normal";
            }
        }

        happyCustomers = Math.max(0, data.happyCustomers);
        neutralCustomers = Math.max(0, data.neutralCustomers);
        unhappyCustomers = Math.max(0, data.unhappyCustomers);
        repairBonus = Math.max(0, data.repairBonus);
        dailyMoneyEarned = data.dailyMoneyEarned;
        dailyItemsSold = Math.max(0, data.dailyItemsSold);
        dailyReputationChange = data.dailyReputationChange;
        dailyHappyCustomers = Math.max(0, data.dailyHappyCustomers);
        dailyNeutralCustomers = Math.max(0, data.dailyNeutralCustomers);
        dailyUnhappyCustomers = Math.max(0, data.dailyUnhappyCustomers);
        dailyItemsReceived = Math.max(0, data.dailyItemsReceived);
        dailyItemsRepairedByEmployees = Math.max(0, data.dailyItemsRepairedByEmployees);
        dailyEmployeeMistakes = Math.max(0, data.dailyEmployeeMistakes);
        dailySalariesPaid = Math.max(0, data.dailySalariesPaid);
        dailySalesRefused = Math.max(0, data.dailySalesRefused);
        dailyMoneySpent = Math.max(0, data.dailyMoneySpent);
        dailyProfit = data.dailyProfit;

        GameKeys.azertyMode = data.azertyMode;
        GameKeys.applyKeyboardMode();
        closeAllMenus();
        message = saveManager.getMessage();
    }

    private void normalizeLoadedItems(ArrayList<Item> items) {
        items.removeIf(item -> item == null);
        for (Item item : items) {
            item.condition = Math.max(0, Math.min(100, item.condition));
            item.value = Math.max(1, item.value);
            item.salePrice = Math.max(1, item.salePrice);
            item.energyCost = Math.max(1, item.energyCost);
            if (item.rarety == null || item.rarety.isEmpty()) {
                item.rarety = "Commun";
            } else if ("Epique".equals(item.rarety)) {
                item.rarety = "Épique";
            }
            if ("Meuble".equals(item.type)) {
                item.type = "Mobilier";
            } else if (item.type == null || item.type.isEmpty()) {
                item.type = item.name == null ? "Divers" : itemManager.getTypeFromName(item.name);
            }
        }
    }

    private void normalizeLoadedEmployees() {
        employees.removeIf(employee -> employee == null);
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            if (employee.name == null || employee.name.isEmpty()) {
                employee.name = "Employé " + (i + 1);
            }
            employee.level = Math.max(1, employee.level);
            employee.skill = Math.max(0, employee.skill);
            employee.dailySalary = Math.max(0, employee.dailySalary);
            if ("Meuble".equals(employee.specialty)) {
                employee.specialty = "Mobilier";
            }
            if (employee.specialty == null || employee.specialty.isEmpty()) {
                employee.specialty = "Décoration";
            }
        }
    }

    private void closeAllMenus() {
        dayReport = false;
        showWorkshopMenu = false;
        showSaleMenu = false;
        showStockMenu = false;
        showHelpMenu = false;
        showEmployeeMenu = false;
    }

    private void resetDailyStats() {
        dailyMoneyEarned = 0;
        dailyItemsSold = 0;
        dailyHappyCustomers = 0;
        dailyNeutralCustomers = 0;
        dailyUnhappyCustomers = 0;
        dailyReputationChange = 0;
        dailyItemsReceived = 0;
        dailyItemsRepairedByEmployees = 0;
        dailyEmployeeMistakes = 0;
        dailySalariesPaid = 0;
        dailySalesRefused = 0;
        dailyMoneySpent = 0;
        dailyProfit = 0;
    }

    private void generateNewItems() {
        int numberOfNewItems = random.nextInt(GameBalance.NEW_ITEMS_RANDOM_RANGE) + GameBalance.MIN_NEW_ITEMS_PER_DAY;
        for (int i = 0; i < numberOfNewItems; i++) {
            if (Inventory.size() < maxInventorySize) {
                Inventory.add(itemManager.createRandomItem());
                dailyItemsReceived++;
            } else {
                message = "Votre inventaire est plein, vous ne pouvez pas accepter de nouveaux objets.";
                break;
            }
        }
    }

    private void processCustomersToday(){
         int customersToday = getCustomersPerDay();

        for (int i = 0; i < customersToday; i++) {
            customerManager.reputation = reputation;
            currentCustomer = customerManager.createRandomCustomer();
            BuyFromCustomer();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        Assets.dispose();
    }

}
