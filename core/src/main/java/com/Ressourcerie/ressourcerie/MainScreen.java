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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
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
    private int coffeeEnergyBoost = 20;
    private int coffeeCost = 10;
    private int repairBonus = 0;
    private int workshopLevel = 1;
    private int workshopUpgradeCost = 100;
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

        String reputationRank;

        if (reputation < 30) {
            reputationRank = "Mauvaise";
        } else if (reputation < 70) {
            reputationRank = "Correcte";
        } else if (reputation < 90) {
            reputationRank = "Excellente";
        } else {
            reputationRank = "Legendaire";
        }

        if (showHelpMenu) {

            renderHelpMenu();
            return;
        }

        batch.begin();
        font.draw(batch, "F1 = Aide | S = Stock, | A = Ateliers", 40, 450);
        font.draw(batch, message, 100, 380);

        int statsX = 430;

        font.draw(batch, "Argent : " + money + " euros", statsX, 450);
        font.draw(batch, "Jour : " + day, statsX, 420);
        font.draw(batch, "Energie : " + energy + "/" + maxEnergy, statsX, 390);
        font.draw(batch, "Reputation : " + reputation + " (" + reputationRank + ")", statsX, 360);
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

        font.draw(batch, "Message : " + message, 40, 60);

        int y = 300;

        for (int i = 0; i < Inventory.size(); i++) {
            Item item = Inventory.get(i);

            String prefix = "  ";

            if (i == selectedIndex) {
                prefix = "> ";
            }

            font.draw(batch,
                    prefix + item.name
                            + " - Etat : " + item.condition + "%"
                            + " - Rarete : " + item.rarety
                            + " - Energie : " + getFinalRepairCost(item)
                            + " - Type : " + item.type,
                    40,
                    y);

            y -= 35;

        }
        if (Inventory.isEmpty()) {
            font.draw(batch, "Aucun objet en stock. Appuyez sur ESPACE pour passer au jour suivant.", 100, 300);
        }

        batch.end();

    }

    private void nextDay() {

        saveManager.saveGame();
        day++;
        energy = maxEnergy;

        for (Employee employee : employees){
            employeeManager.repairEmployee(employee);
        }

        if (!employees.isEmpty()){
            message = "Salaires payes : " + (employees.size() * 20) + " euros.";
        }

        int numberOfNewItems = random.nextInt(3) + 2;
        for (int i = 0; i < numberOfNewItems; i++) {
            if (Inventory.size() < maxInventorySize) {
                Inventory.add(createRandomItem());
            } else {
                message = "Votre inventaire est plein, vous ne pouvez pas accepter de nouveaux objets.";
                break;
            }
        }

        int customersToday = getCustomersPerDay();

        for (int i = 0; i < customersToday; i++) {

            currentCustomer = customerManager.createRandomCustomer();
            BuyFromCustomer();
        }

        selectedIndex = 0;
        dailyMoneyEarned = 0;
        dailyItemsSold = 0;
        dailyHappyCustomers = 0;
        dailyNeutralCustomers = 0;
        dailyUnhappyCustomers = 0;
        dailyReputationChange = 0;
        repairBonus = 0;

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
                continue;
            }
            if (!customerCanPay(item)){
                message = currentCustomer.name + " n'a pas assez d'argent.";
                return;
            }
            if (isPriceTooHigh(item)){
                message = currentCustomer.name + " trouve le prix trop élevé.";
                return;
            }
            if (currentCustomer.customerType.equals("Exigeant") && item.condition < 70){
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
            font.draw(batch, "Argent gagné : " + dailyMoneyEarned + "€", 100, 170);
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
            font.draw(batch, "Appuyez sur ENTRÉE pour continuer.", 100, 80);

            batch.end();
    }

    private void renderHelpMenu(){
        batch.begin();

            font.draw(batch, "=== AIDE / COMMANDES ===", 100, 430);

            font.draw(batch, "F1 = Fermer l'aide", 100, 390);
            font.draw(batch, "F5 = Sauvegarder", 100, 360);
            font.draw(batch, "F9 = Charger", 100, 330);
            font.draw(batch, "Flèches = Selectionner un objet", 100, 300);
            font.draw(batch, "R = Reparer", 100, 270);
            font.draw(batch, "V = Mise en vente", 100, 240);
            font.draw(batch, "S = Stock", 100, 210);
            font.draw(batch, "B = Acheter un cafe", 100, 180);
            font.draw(batch, "T = Acheter un kit", 100, 150);
            font.draw(batch, "A = Ateliers", 100, 120);
            font.draw(batch, "C = Faire venir un client test", 100, 90);
            font.draw(batch, "ESPACE = Rapport / jour suivant si inventaire vide", 100, 60);
            font.draw(batch, "ECHAP = Annuler certains menus", 100, 30);
            font.draw(batch, "E = Ouvrir le menu des employes", 450, 390);

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

            font.draw(batch, "=== ATELIERS ===", 100, 420);
            font.draw(batch, "1, - Electronique niv. " + electronicWorkshopLevel, 100, 370);
            font.draw(batch, "2, - Mécanique niv. " + mechanicalWorkshopLevel, 100, 340);
            font.draw(batch, "3, - Mobilier niv. " + woodWorkshopLevel, 100, 310);
            font.draw(batch, "4, - Décoration niv. " + decorationWorkshopLevel, 100, 280);
            font.draw(batch, "5, - Textile niv. " + textileWorkshopLevel, 100, 250);
            font.draw(batch, "Appuyez sur le numéro de l'atelier pour l'améliorer.", 100, 220);
            font.draw(batch, "Appuyez sur A pour fermer ce menu.", 100, 190);

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

        font.draw(batch, "=== EMPLOYES ===", 100, 430);
        font.draw(batch, "1 = Recruter un employe (200 euros)", 100, 390);
        font.draw(batch, "2 = Former un employe (100 euros)", 100, 360);
        font.draw(batch, "E = Fermer", 100, 330);

        int y = 310;

        if (employees.isEmpty()){
            font.draw(batch, "Aucun employe recrute.", 100, y);
        }
            for (int i = 0; i < employees.size(); i++){
                Employee employee = employees.get(i);
                String prefix = "  ";

                if (i == selectedEmployeeIndex){
                    prefix = "> ";
                }

                font.draw(batch, prefix + employee.name
                    + " | Niveau : " + employee.level
                    + " | Salaire : " + employee.dailySalary
                    + " | Reparation : " + employee.getRepairPower()
                    + " | Specialite : " + employee.specialty, 100, y
                );

                y -= 30;
            }

        batch.end();
    }

    private void renderStockMenu(){
         batch.begin();
            font.draw(batch, "=== ENTREPOT ===", 100, 430);
            font.draw(batch, "Entrepot niveau : " + storageLevel, 100, 390);
            font.draw(batch, "Stock réparation : " + Inventory.size() + "/" + maxInventorySize, 100, 360);
            font.draw(batch, "Stock vente : " + sellingStock.size() + "/" + maxSellingStockSize, 100, 330);
            font.draw(batch, "Amélioration entrepot : " + storageUpgradeCost + "euros", 100,300);
            font.draw(batch, "6 = Améliorer entrepot", 100, 270);
            int y = 230;

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

            if (sellingStock.isEmpty()) {
                font.draw(batch, "Aucun objet en vente.", 100, y);
            } else {
                for (Item item : sellingStock) {
                    font.draw(batch,
                            item.name
                                    + " | Prix : " + item.salePrice + " euros"
                                    + " | Etat : " + item.condition + "%"
                                    + " | Rarete : " + item.rarety
                                    + " | Type : " + item.type,
                            100,
                            y);

                    y -= 30;
                }
            }

            font.draw(batch, "S = fermer", 100, 80);

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

            font.draw(batch, "=== MISE EN VENTE ===", 100, 420);
            font.draw(batch, selectedItem.name, 100, 380);
            font.draw(batch, "Valeur conseillee : " + selectedItem.value + " euros", 100, 340);
            font.draw(batch, "Prix choisi : " + currentSalePrice + " euros", 100, 300);
            font.draw(batch, "+ / - = modifier le prix", 100, 250);
            font.draw(batch, "ENTREE = confirmer", 100, 220);
            font.draw(batch, "ECHAP = annuler", 100, 190);

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
    }

}
