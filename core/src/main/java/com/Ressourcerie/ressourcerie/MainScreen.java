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
    private int dailyMoneyEarned = 0;
    private int dailyItemsSold = 0;
    private int dailyReputationChange = 0;
    private int dailyHappyCustomers = 0;
    private int dailyNeutralCustomers = 0;
    private int dailyUnhappyCustomers = 0;
    private int coffeeEnergyBoost = 20;
    private int coffeeCost = 10;
    private int repairBonus = 0;

    @Override
    public void show() {
        random = new Random();
        batch = new SpriteBatch();
        font = new BitmapFont();
        Inventory = new ArrayList<>();
        sellingStock = new ArrayList<>();
        currentCustomer = createRandomCustomer();

        Inventory.add(new Item("Vieille radio", 45, 20, "Commun", 10));
        Inventory.add(new Item("Chaise en bois", 70, 15, "Commun", 5));
        Inventory.add(new Item("Vieux vélo", 30, 50, "Commun", 15));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;

            if (selectedIndex >= Inventory.size()) {
                selectedIndex = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;

            if (selectedIndex < 0) {
                selectedIndex = Inventory.size() - 1;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {

            if (!Inventory.isEmpty()) {

                Item selectedItem = Inventory.get(selectedIndex);

                int repairCost = selectedItem.energyCost - repairBonus;
                if (repairCost < 1) {
                    repairCost = 1;
                }
                if (energy >= repairCost) {
                    energy -= repairCost;
                    selectedItem.repair();
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

                Item selectedItem = Inventory.get(selectedIndex);

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (money >= 40) {
                money -= 40;
                repairBonus += 5;
                message = "Vous avez acheté un kit de réparation. -5 energie sur les réparations d'aujourd'hui.";
            } else {
                message = "Pas assez d'argent pour acheter un kit de réparation.";
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
        int y = 300;

        int sellingY = 260;
        for (Item item : sellingStock) {
            font.draw(batch,
                    "  " + item.name + " - " + item.value + "€", 500, sellingY);
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
                            + " - Energie : " + item.energyCost,
                    100,
                    y);

            y -= 40;

        }
        if (Inventory.isEmpty()) {
            font.draw(batch, "Aucun objet en stock. Appuyez sur ESPACE pour passer au jour suivant.", 100, 300);
        }

        font.draw(batch, "Client : " + currentCustomer.name + " | Budget : " + currentCustomer.budget + "€ | Veut : "
                + currentCustomer.wantedItems, 100, 120);

        batch.end();

    }

    private void nextDay() {

        day++;
        energy = maxEnergy;

        int numberOfNewItems = random.nextInt(3) + 2;
        for (int i = 0; i < numberOfNewItems; i++) {
            Inventory.add(createRandomItem());
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
        int conditions = random.nextInt(81) + 20;
        int values = random.nextInt(41) + 10;
        String rarity;
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
        return new Item(name, conditions, values, rarity, energyCost);
    }

    private Customer createRandomCustomer() {
        String[] names = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy" };
        String[] wantedItems = { "Vieille radio", "Lampe cassée", "Chaise en bois", "Vieux vélo", "Table abîmée",
                "Ordinateur obsolète", "Télévision ancienne", "Machine à écrire", "Guitare désaccordée", "Canapé usé",
                "Vase ébréché", "Montre cassée", "Appareil photo vintage", "Jouet en bois", "Livre ancien" };
        String name = names[random.nextInt(names.length)];
        int budget = random.nextInt(201) + 50;
        String wantedItem = wantedItems[random.nextInt(wantedItems.length)];
        return new Customer(name, budget, wantedItem);
    }

    private void BuyFromCustomer() {
        for (int i = 0; i < sellingStock.size(); i++) {
            Item item = sellingStock.get(i);
            if (item.name.equals(currentCustomer.wantedItems) && (item.value <= currentCustomer.budget)) {
                money += item.value;
                sellingStock.remove(i);
                message = "Vous avez vendu " + item.name + " à " + currentCustomer.name + " pour " + item.value + "€.";
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
                dailyMoneyEarned += item.value;
                dailyItemsSold++;
                dailyReputationChange += item.condition >= 70 ? 5 : item.condition >= 40 ? 0 : -5;
                return;
            }
        }
        message = currentCustomer.name + " n'a pas acheté d'objet aujourd'hui.";
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
