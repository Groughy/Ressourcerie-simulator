package com.Ressourcerie.ressourcerie;

import com.badlogic.gdx.Screen;

import java.util.ArrayList;
import java.util.Random;

import com.Ressourcerie.ressourcerie.customer.Customer;
import com.Ressourcerie.ressourcerie.items.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Customer currentCustomer;
    private int reputation = 50;
    private int happyCustomers = 0;
    private int unhappyCustomers = 0;
    private int neutralCustomers = 0;
    private boolean dayReport = false;
    private boolean showWorkshopMenu = false;
    private boolean showSaleMenu = false;
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
    private int electronicWorkshopLevel = 0;
    private int woodWorkshopLevel = 0;
    private int mechanicalWorkshopLevel = 1;
    private int decorationWorkshopLevel = 1;
    private int textileWorkshopLevel = 0;
    private int currentSalePrice = 0;
    private int maxInventorySize = 10;
    private int maxSellingStockSize = 10;

    @Override
    public void show() {
        random = new Random();
        batch = new SpriteBatch();
        font = new BitmapFont();
        Inventory = new ArrayList<>();
        sellingStock = new ArrayList<>();
        currentCustomer = createRandomCustomer();

        Inventory.add(new Item("Vieille radio", 45, 20, 20, "Commun", 10, "Electronique"));
        Inventory.add(new Item("Chaise en bois", 70, 15, 20, "Commun", 5, "Mobilier"));
        Inventory.add(new Item("Vieux vélo", 30, 50, 20, "Commun", 15, "Mécanique"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            currentSalePrice = Inventory.get(selectedIndex).value;

            if (selectedIndex >= Inventory.size()) {
                selectedIndex = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            currentSalePrice = Inventory.get(selectedIndex).value;

            if (selectedIndex < 0) {
                selectedIndex = Inventory.size() - 1;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS) || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            currentSalePrice += 5;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            currentSalePrice -= 5;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {

            if (!Inventory.isEmpty()) {

                Item selectedItem = Inventory.get(selectedIndex);
                if (!canRepair(selectedItem)) {
                    message = "Améliore ton atelier pour faire ça. Atelier requis : " + selectedItem.type;
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

        if (dayReport) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                dayReport = false;
                nextDay();
                return;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            showWorkshopMenu = !showWorkshopMenu;
        }

        if (showWorkshopMenu) {

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
            return;
        }

        if (dayReport) {

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
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            BuyFromCustomer();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {

            if (!Inventory.isEmpty()) {
                currentSalePrice = Inventory.get(selectedIndex).value;
                showSaleMenu = true;
                Item selectedItem = Inventory.get(selectedIndex);
                selectedItem.salePrice = currentSalePrice;
                if(sellingStock.size() >= maxSellingStockSize){
                    message = "Stock de vente plein.";
                    showSaleMenu = false;
                    return;
                }
                sellingStock.add(selectedItem);
                Inventory.remove(selectedIndex);

                if (selectedIndex >= Inventory.size()) {
                    selectedIndex = Inventory.size() - 1;
                }

                if (selectedIndex < 0) {
                    selectedIndex = 0;
                }
            }
        }

        if (showSaleMenu) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)
                    || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
                currentSalePrice += 5;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
                currentSalePrice -= 5;

                if (currentSalePrice < 1) {
                    currentSalePrice = 1;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                Item selectedItem = Inventory.get(selectedIndex);
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
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                showSaleMenu = false;
            }

            batch.begin();

            Item selectedItem = Inventory.get(selectedIndex);

            font.draw(batch, "=== MISE EN VENTE ===", 100, 420);
            font.draw(batch, selectedItem.name, 100, 380);
            font.draw(batch, "Valeur conseillee : " + selectedItem.value + " euros", 100, 340);
            font.draw(batch, "Prix choisi : " + currentSalePrice + " euros", 100, 300);
            font.draw(batch, "+ / - = modifier le prix", 100, 250);
            font.draw(batch, "ENTREE = confirmer", 100, 220);
            font.draw(batch, "ECHAP = annuler", 100, 190);

            batch.end();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (money >= 40) {
                money -= 40;
                repairBonus += 5;
                message = "Vous avez acheté un kit de réparation. -5 energie sur les réparations d'aujourd'hui.";
            } else {
                message = "Pas assez d'argent pour acheter un kit de réparation.";
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            if (money >= workshopUpgradeCost) {
                money -= workshopUpgradeCost;
                workshopLevel++;
                repairBonus += 2;
                workshopUpgradeCost += 100;
                message = "Vous avez amélioré votre atelier à niveau " + workshopLevel
                        + ". Bonus de réparation augmenté de 5.";
            } else {
                message = "Pas assez d'argent pour améliorer l'atelier.";
            }
        }

        if (Inventory.isEmpty()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                dayReport = true;
            }
        }

        batch.begin();
        font.draw(batch, "Argent : " + money + "€", 500, 450);
        font.draw(batch, "Jour : " + day, 500, 400);
        font.draw(batch,
                " R = Réparer | V = Mettre en vente | Haut/Bas = Sélection | B = Acheter café (15€ / + 20 energie) ",
                100, 450);
        font.draw(batch, "T = Acheter kit de réparation (40€ / -5 énergie sur réparations)", 100, 420);
        font.draw(batch, "Énergie : " + energy + "/" + maxEnergy, 500, 350);
        font.draw(batch, message, 100, 380);
        font.draw(batch, "Objets en vente : ", 100, 350);
        font.draw(batch, "Réputation : " + reputation, 500, 320);
        font.draw(batch, "Clients ravis : " + happyCustomers, 500, 290);
        font.draw(batch, "Clients mécontents : " + unhappyCustomers, 500, 260);
        font.draw(batch, "Clients neutres : " + neutralCustomers, 500, 230);
        font.draw(batch, "Bonus de réparation : " + repairBonus, 500, 200);
        font.draw(batch, "Atelier niveau : " + workshopLevel, 500, 170);
        font.draw(batch, "Ameliorer atelier : " + workshopUpgradeCost + "€", 100, 60);
        int y = 300;

        int sellingY = 260;
        for (Item item : sellingStock) {
            font.draw(batch,
                    "  " + item.name + " - " + item.salePrice + "€", 500, sellingY);
            sellingY -= 30;
        }

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
                    100,
                    y);

            y -= 40;

        }
        if (Inventory.isEmpty()) {
            font.draw(batch, "Aucun objet en stock. Appuyez sur ESPACE pour passer au jour suivant.", 100, 300);
        }

        font.draw(batch, "Client : " + currentCustomer.name + " | Budget : " + currentCustomer.budget + "€ | Veut : "
                + currentCustomer.wantedItems + " | Type : " + currentCustomer.customerType, 100, 120);

        batch.end();

    }

    private void nextDay() {

        day++;
        energy = maxEnergy;

        int numberOfNewItems = random.nextInt(3) + 2;
        for (int i = 0; i < numberOfNewItems; i++) {
            if (Inventory.size() < maxInventorySize){
                Inventory.add(createRandomItem());
            }else{
                message = "Votre inventaire est plein, vous ne pouvez pas accepter de nouveaux objets.";
                break;
            }
        }

        selectedIndex = 0;
        currentCustomer = createRandomCustomer();
        BuyFromCustomer();
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
        int conditions = random.nextInt(81) + 20;
        int values = random.nextInt(41) + 10;
        String rarity;
        int salePrice = values;
        int rarityRoll = random.nextInt(100);
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

    private Customer createRandomCustomer() {
        String[] names = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy" };
        String[] wantedItems = { "Vieille radio", "Lampe cassée", "Chaise en bois", "Vieux vélo", "Table abîmée",
                "Ordinateur obsolète", "Télévision ancienne", "Machine à écrire", "Guitare désaccordée", "Canapé usé",
                "Vase ébréché", "Montre cassée", "Appareil photo vintage", "Jouet en bois", "Livre ancien" };
        String name = names[random.nextInt(names.length)];
        int budget = random.nextInt(201) + 50;
        String wantedItem = wantedItems[random.nextInt(wantedItems.length)];
        String[] customerTypes = {
                "Normal",
                "Collectionneur",
                "Bricoleur",
                "Exigeant"
        };
        String customerType = customerTypes[random.nextInt(customerTypes.length)];
        return new Customer(name, budget, wantedItem, customerType);
    }

    private void BuyFromCustomer() {
        
        for (int i = 0; i < sellingStock.size(); i++) {
            Item item = sellingStock.get(i);
            int expectedPrice = item.value;

        if (item.salePrice > expectedPrice * 1.5){
            message = currentCustomer.name + " trouve le prix excessif.";

            return;
        }
        if (item.salePrice < expectedPrice * 1.5){
            reputation ++;
            message = currentCustomer.name + " pense avoir une excellente affaire.";
        }
            if (item.name.equals(currentCustomer.wantedItems) && (item.salePrice <= currentCustomer.budget)) {
                if (currentCustomer.customerType.equals("Exigeant")) {
                    if (item.condition < 70) {
                        message = currentCustomer.name + "refuse un objet de mauvaise qualité.";
                        return;
                    }
                }
                if (currentCustomer.customerType.equals("Collectionneur")) {
                    if (item.rarety.equals("Rare")
                            || item.rarety.equals("Epique")
                            || item.rarety.equals("Légendaire")) {
                        money += 20;
                        message = currentCustomer.name + "paie un bonus pour un objet rare !";
                    }
                }
                if (currentCustomer.customerType.equals("Bricoleur")) {
                    if (item.condition < 40) {
                        reputation += 1;
                    }
                }
                money += item.salePrice;
                sellingStock.remove(i);
                message = "Vous avez vendu " + item.name + " à " + currentCustomer.name + " pour " + item.salePrice
                        + "€.";
                if (item.condition >= 70) {
                    reputation += 5;
                    happyCustomers++;
                    dailyHappyCustomers++;
                    message = currentCustomer.name + " est très satisfait de son achat !";
                } else if (item.condition >= 40) {
                    neutralCustomers++;
                    dailyNeutralCustomers++;
                    message = currentCustomer.name + " est satisfait de son achat.";
                } else {
                    reputation -= 5;
                    unhappyCustomers++;
                    dailyUnhappyCustomers++;
                    message = currentCustomer.name + " est mécontent de son achat.";
                }
                dailyMoneyEarned += item.salePrice;
                dailyItemsSold++;
                dailyReputationChange += item.condition >= 70 ? 5 : item.condition >= 40 ? 0 : -5;
                return;
            }
        }
        message = currentCustomer.name + " n'a pas acheté d'objet aujourd'hui.";
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
        if (item.type.equals("Meuble")) {
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
