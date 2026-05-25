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

        if (energy >= selectedItem.energyCost) {
            energy -= selectedItem.energyCost;
            selectedItem.repair();
        } else {
            message = "Pas assez d'energie pour reparer cet objet.";
        }
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

        if (Inventory.isEmpty()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                nextDay();
            }
        }

        batch.begin();
        font.draw(batch, "Argent : " + money + "€", 500, 450);
        font.draw(batch, "Jour : " + day, 500, 400);
        font.draw(batch, " R = Réparer | V = Mettre en vente | Haut/Bas = Sélection ", 100, 450);
        font.draw(batch, "Énergie : " + energy + "/" + maxEnergy, 500, 350);
        font.draw(batch, message, 100, 380);
        font.draw(batch, "Objets en vente : ", 100, 350);
        font.draw(batch, "Réputation : " + reputation, 500, 320);
        font.draw(batch, "Clients ravis : " + happyCustomers, 500, 290);
        font.draw(batch, "Clients mécontents : " + unhappyCustomers, 500, 260);
        font.draw(batch, "Clients neutres : " + neutralCustomers, 500, 230);
        int y = 300;

        int sellingY = 260;
        for (Item item : sellingStock) {
            font.draw(batch,
                "  " + item.name + " - " + item.value + "€",500,sellingY
            );
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
                y
            );

            y -= 40;

        }
        if (Inventory.isEmpty()) {
            font.draw(batch, "Aucun objet en stock. Appuyez sur ESPACE pour passer au jour suivant.", 100, 300);
        }

        font.draw(batch, "Client : " + currentCustomer.name + " | Budget : " + currentCustomer.budget + "€ | Veut : " + currentCustomer.wantedItems, 100, 120);

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

    private void BuyFromCustomer(){
    for (int i = 0; i < sellingStock.size(); i++) {
            Item item = sellingStock.get(i);
            if (item.name.equals(currentCustomer.wantedItems) && (item.value <= currentCustomer.budget)) {
                money += item.value;
                sellingStock.remove(i);
                message = "Vous avez vendu " + item.name + " à " + currentCustomer.name + " pour " + item.value + "€.";
                if (item.condition >= 70){
                    reputation += 5;
                    happyCustomers++;
                    message = currentCustomer.name + " est très satisfait de son achat !";
                } else if (item.condition >= 40){
                    neutralCustomers++;
                    message = currentCustomer.name + " est satisfait de son achat.";
                } else {
                    reputation -= 5;
                    unhappyCustomers++;
                    message = currentCustomer.name + " est mécontent de son achat.";
                }
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
