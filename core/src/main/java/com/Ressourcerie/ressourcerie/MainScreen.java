package com.Ressourcerie.ressourcerie;

import java.util.ArrayList;
import java.util.Random;

import com.Ressourcerie.ressourcerie.customer.Customer;
import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;

import com.Ressourcerie.ressourcerie.managers.SaveManager;
import com.Ressourcerie.ressourcerie.managers.CustomerManager;
import com.Ressourcerie.ressourcerie.managers.EmployeeManager;

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

    private int selectedIndex = 0;
    private ArrayList<Item> Inventory;
    private int money = 100;
    private int day = 1;
    private Random random = new Random();
    private int energy = 100;
    private int maxEnergy = 100;
    private String message = "";
    private ArrayList<Item> sellingStock;
    private ArrayList<Employee> employees;
    private Customer currentCustomer;
    private int reputation = 50;
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
    private int coffeeEnergyBoost = 20;
    private int coffeeCost = 10;
    private int repairBonus = 0;
    private int electronicWorkshopLevel = 2;
    private int woodWorkshopLevel = 2;
    private int mechanicalWorkshopLevel = 2;
    private int decorationWorkshopLevel = 1;
    private int textileWorkshopLevel = 0;
    private int currentSalePrice = 0;
    private int maxInventorySize = 10;
    private int maxSellingStockSize = 10;
    private int storageLevel = 1;
    private int storageUpgradeCost = 120;
    private int selectedEmployeeIndex = 0;
    private SaveManager saveManager = new SaveManager();
    private CustomerManager customerManager = new CustomerManager();
    private EmployeeManager employeeManager = new EmployeeManager();

    @Override
    public void show() {
        random = new Random();
        batch = new SpriteBatch();
        font = new BitmapFont();

        backgroundTexture = new Texture("ui/background.png");
        panelLeftTexture = new Texture("ui/panel_left.png");
        panelRightTexture = new Texture("ui/panel_right.png");
        
        hudRenderer = new HudRenderer();
        inventoryRenderer = new InventoryRenderer();
        customerInfoRenderer = new CustomerInfoRenderer();
        messageRenderer = new MessageRenderer();
        workshopRenderer = new WorkshopRenderer();
        employeeRenderer = new EmployeeRenderer();
        stockRenderer = new StockRenderer();
        saleMenuRenderer = new SaleMenuRenderer();
        helpRenderer = new HelpRenderer();

        Inventory = new ArrayList<>();
        sellingStock = new ArrayList<>();
        employees = new ArrayList<>();
        
        currentCustomer = customerManager.createRandomCustomer();

        Inventory.add(new Item("Vieille radio", 45, 20, 20, "Commun", 10, "Electronique"));
        Inventory.add(new Item("Chaise en bois", 70, 15, 20, "Commun", 5, "Mobilier"));
        Inventory.add(new Item("Vieux vélo", 30, 50, 20, "Commun", 15, "Mécanique"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInventorySelection();
        handleInputs();

        if (dayReport) {
            if (Gdx.input.isKeyJustPressed(GameKeys.ENTER)) {
                dayReport = false;
                nextDay();
                return;
            }
        }

        if (showWorkshopMenu) {

           renderWorkshopMenu();
            return;
        }

        if (dayReport) {

            renderDayReport();
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

        if (Inventory.isEmpty()) {
            if (Gdx.input.isKeyJustPressed(GameKeys.SPACE)) {
                dayReport = true;
            }
        }        

        if (showHelpMenu) {

            renderHelpMenu();
            return;
        }

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(panelLeftTexture, 0, 80);
        float rightX = Gdx.graphics.getWidth() - panelRightTexture.getWidth();
        batch.draw(panelRightTexture, rightX, 80);
        font.draw(batch, "F1 = Aide | S = Stock, | A = Ateliers", 40, 450);
        font.draw(batch, message, 100, 380);

        int statsX = 430;

        hudRenderer.render(batch, font, day, money, energy, maxEnergy, reputation);
        font.draw(batch, "Clients ravis : " + happyCustomers, statsX, 330);
        font.draw(batch, "Clients neutres : " + neutralCustomers, statsX, 300);
        font.draw(batch, "Clients decus : " + unhappyCustomers, statsX, 270);
        font.draw(batch, "Employes : " + employees.size(), statsX, 240);

        font.draw(batch, "Client : " + currentCustomer.name
                + " | Budget : " + currentCustomer.budget
                + " | Veut : " + currentCustomer.wantedItems
                + " | Type : " + currentCustomer.customerType,
                40,
                90);

        inventoryRenderer.render(batch, font, Inventory, selectedIndex);
        customerInfoRenderer.render(batch, font, currentCustomer);
        messageRenderer.render(batch, font, message);

        if (Inventory.isEmpty()) {
            font.draw(batch, "Aucun objet en stock. Appuyez sur ESPACE pour passer au jour suivant.", 100, 300);
        }

        batch.end();

    }

    private void nextDay() {

        energy = maxEnergy;

        for (Employee employee : employees){
            employeeManager.repairEmployee(employee);
        }

        int salaries = employeeManager.getTotalSalaries(employees);
        money -= salaries;

        dailySalariesPaid = salaries;
        dailyMoneySpent += salaries;

        
        processCustomersToday();
        selectedIndex = 0;
        resetDailyStats();
        generateNewItems();
        repairBonus = 0;

        day++;
        saveManager.saveGame();
    }

    private Item createRandomItem() {
        String[] names = { "Vieille radio", "Lampe cassée", "Chaise en bois", "Vieux vélo", "Table abîmée",
                "Ordinateur obsolète", "Télévision ancienne", "Machine à écrire", "Guitare désaccordée", "Canapé usé",
                "Vase ébréché", "Montre cassée", "Appareil photo vintage", "Jouet en bois", "Livre ancien" };
        String name = names[random.nextInt(names.length)];
        String type = getTypeFromName(name);
        int conditions;
        if (reputation < 30){
            conditions = random.nextInt(41) + 10;
        }
        else if (reputation < 70){
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
        if (reputation > 95){
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

    private boolean customerWantsItem(Item item){
        return item.name.equals(currentCustomer.wantedItems);
    }

    private boolean customerCanPay(Item item){
        return item.salePrice <= currentCustomer.budget;
    }

    private boolean isPriceTooHigh(Item item){
        return item.salePrice > item.value * 1.5;
    }

    private boolean isGoodDeal(Item item){
        return item.salePrice < item.value * 0.8;
    }


    private void BuyFromCustomer() {

        for (int i = 0; i < sellingStock.size(); i++) {
            Item item = sellingStock.get(i);

            if (!customerWantsItem(item)){
                dailySalesRefused++;
                continue;
            }
            if (!customerCanPay(item)){
                dailySalesRefused++;
                message = currentCustomer.name + " n'a pas assez d'argent.";
                return;
            }
            if (isPriceTooHigh(item)){
                dailySalesRefused++;
                message = currentCustomer.name + " trouve le prix trop élevé.";
                return;
            }
            if (currentCustomer.customerType.equals("Exigeant") && item.condition < 70){
                dailySalesRefused++;
                message = currentCustomer.name + " refuse d'acheter un objet en mauvais état.";
                return;
            }

            sellItemToCustomer(item, i);
            return;
    }
    message = currentCustomer.name + " n'a rien trouvé d'intéressant.";
    }

    private void sellItemToCustomer(Item item, int index){
        money += item.salePrice;
        dailyMoneyEarned += item.salePrice;
        dailyItemsSold++;
        if (isGoodDeal(item)){
            reputation++;
            dailyReputationChange++;
        }
        if (currentCustomer.customerType.equals("Collectionneur")
        && (item.rarety.equals("Rare")
        || item.rarety.equals("Epique")
        || item.rarety.equals("Légendaire"))){
            money += 20;
            dailyMoneyEarned += 20;
        }

        applyCustomerSatisfaction(item);

        sellingStock.remove(index);

        message = currentCustomer.name + " a acheté " + item.name + " pour " + item.salePrice + " euros.";
    }

    private void applyCustomerSatisfaction(Item item){
        if (item.condition >=70){
            reputation += 5;
            happyCustomers++;
            dailyHappyCustomers++;
            dailyReputationChange += 5;
        } else if (item.condition >=40){
            neutralCustomers++;
            dailyNeutralCustomers++;
        } else {
            reputation -= 5;
            unhappyCustomers++;
            dailyUnhappyCustomers++;
            dailyReputationChange -= 5;
        }
    }

    private String getTypeFromName(String name) {
        if (name.equals("Vieille radio")
                || name.equals("Lampe cassée")
                || name.equals("Ordinateur obsolète")
                || name.equals("Télévision ancienne")
                || name.equals("Appareil photo vintage")) {
            return "Electronique";
        }
        if (name.equals("Chaise en bois")
                || name.equals("Table abîmée")
                || name.equals("Canapé usé")) {
            return "Meuble";
        }
        if (name.equals("Vieux vélo")) {
            return "Mécanique";
        }
        if (name.equals("Machine à écrire")
                || name.equals("Jouet en bois")
                || name.equals("Livre ancien")
                || name.equals("Vase ébréché")) {
            return "Décoration";
        }
        return "Divers";
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
        if (item.type.equals("Meuble")) {
            return woodWorkshopLevel;
        }
        if (item.type.equals("Décoration") || item.type.equals("Divers")) {
            return decorationWorkshopLevel;
        }
        return 0;
    }

    private int getFinalRepairCost(Item item) {
        int workshopLevel = getWorkshopLevelForItem(item);
        int workshopBonus = workshopLevel - 1;
        int finalCost = item.energyCost - repairBonus - workshopBonus;
        if (finalCost < 1) {
            finalCost = 1;
        }
        return finalCost;
    }

    private int getRepairAmount(Item item) {
        int workshopLevel = getWorkshopLevelForItem(item);
        return 5 + (workshopLevel * 5);
    }

    private int getCustomersPerDay() {
        if (reputation < 30) {
            return 1;
        } else if (reputation < 60) {
            return 2;
        } else if (reputation < 90) {
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
       
            if (money >= coffeeCost) {
                money -= coffeeCost;
                energy += coffeeEnergyBoost;
                if (energy > maxEnergy) {
                    energy = maxEnergy;
                }
                message = "Vous avez bu un café. Énergie restaurée de " + coffeeEnergyBoost + ".";
            } else {
                message = "Pas assez d'argent pour acheter un café.";
            }

    }

    private void buyRepairKit(){
        if (money >= 40) {
                money -= 40;
                repairBonus += 5;
                message = "Vous avez acheté un kit de réparation. -5 energie sur les réparations d'aujourd'hui.";
            } else {
                message = "Pas assez d'argent pour acheter un kit de réparation.";
            }
    }

    private void renderDayReport(){
        batch.begin();

            font.draw(batch, "Rapport du jour " + day + " : ", 100, 200);
            font.draw(batch, "Argent gagné : " + dailyMoneyEarned + "euros", 100, 170);
            font.draw(batch, "Objets vendus : " + dailyItemsSold, 100, 140);
            font.draw(batch,
                    "Changement de réputation : " + (dailyReputationChange >= 0 ? "+" : "") + dailyReputationChange,
                    100, 110);
            font.draw(batch,
                    "Clients ravis : " + dailyHappyCustomers,
                    100,
                    320);

            font.draw(batch,
                    "Clients neutres : " + dailyNeutralCustomers,
                    100,
                    290);

            font.draw(batch,
                    "Clients decus : " + dailyUnhappyCustomers,
                    100,
                    260);
            font.draw(batch, "Objets recus : " + dailyItemsReceived, 100, 230);
            font.draw(batch, "Argent depense : " + dailyMoneySpent + " euros", 100, 200);
            font.draw(batch, "Salaires payes : " + dailySalariesPaid + " euros", 100, 170);
            font.draw(batch, "Ventes refusees : " + dailySalesRefused + " euros", 100, 140);
            font.draw(batch, "Appuyez sur ENTRÉE pour continuer.", 100, 80);

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
        if (employees.isEmpty()){
            return;
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.UP)){
            selectedEmployeeIndex--;

            if (selectedEmployeeIndex < 0){
                selectedEmployeeIndex = employees.size() -1;
            }
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.DOWN)){
            selectedEmployeeIndex++;

            if (selectedEmployeeIndex >= employees.size()){
                selectedEmployeeIndex = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            employeeManager.recruitEmployee();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            employeeManager.trainEmployee(employees.get(0));
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
                    storageLevel++;
                    maxInventorySize += 5;
                    maxSellingStockSize += 5;
                    storageUpgradeCost += 120;
                    message = "Entrepot amélioré à niveau " + storageLevel + " ! Capacité augmentée.";
                } else {
                    message = "Pas assez d'argent pour améliorer l'entrepot.";
                }
            }

            batch.end();
    }

    private void renderSaleMenu(){
        if (Inventory.isEmpty()) {
                showSaleMenu = false;
                message = "Aucun objet a mettre en vente.";
                return;
            }

            if (selectedIndex >= Inventory.size()) {
                selectedIndex = Inventory.size() - 1;
            }

            if (selectedIndex < 0) {
                selectedIndex = 0;
            }

            Item selectedItem = Inventory.get(selectedIndex);

            if (Gdx.input.isKeyJustPressed(GameKeys.PLUS)
                    || Gdx.input.isKeyJustPressed(GameKeys.EQUALS)) {
                currentSalePrice += 5;
            }

            if (Gdx.input.isKeyJustPressed(GameKeys.MINUS)) {
                currentSalePrice -= 5;

                if (currentSalePrice < 1) {
                    currentSalePrice = 1;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

                selectedItem.salePrice = currentSalePrice;

                sellingStock.add(selectedItem);
                Inventory.remove(selectedIndex);

                if (selectedIndex >= Inventory.size()) {
                    selectedIndex = Inventory.size() - 1;
                }

                if (selectedIndex < 0) {
                    selectedIndex = 0;
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

            saleMenuRenderer.render(batch, font, selectedItem, currentSalePrice);

            batch.end(); 
    }

    private void repairItem(){
        if (!Inventory.isEmpty()) {

                Item selectedItem = Inventory.get(selectedIndex);
                if (!canRepair(selectedItem)) {
                    message = "Améliore ton atelier pour faire ça. Atelier requis : " + selectedItem.type;
                    return;
                }

                if (selectedItem.condition >= 100){
                    message = "Cet objet est déjà entièrement réparé.";
                    return;
                }

                int repairCost = getFinalRepairCost(selectedItem);
                if (energy >= repairCost) {
                    energy -= repairCost;
                    int repairAmount = getRepairAmount(selectedItem);
                    selectedItem.repair(repairAmount);
                } else {
                    message = "Pas assez d'énergie pour réparer cet objet.";
                }

            }
    }

    private void handleInputs(){
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
            saveManager.saveGame();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.LOAD)){
            saveManager.loadGame();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.COFFEE)) {
            buyCoffee();
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.PLUS) || Gdx.input.isKeyJustPressed(GameKeys.EQUALS)) {
            currentSalePrice += 5;
        }

        if (Gdx.input.isKeyJustPressed(GameKeys.MINUS)) {
            currentSalePrice -= 5;
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
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)){
            employeeManager.recruitEmployee();
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

            currentSalePrice = Inventory.get(selectedIndex).value;
            showSaleMenu = true;
        }
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
    }

    private void generateNewItems() {
        int numberOfNewItems = random.nextInt(3) + 2;
        for (int i = 0; i < numberOfNewItems; i++) {
            if (Inventory.size() < maxInventorySize) {
                Inventory.add(createRandomItem());
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

        Assets.dispose();
    }

}
