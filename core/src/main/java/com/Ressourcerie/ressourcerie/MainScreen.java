package com.Ressourcerie.ressourcerie;

import com.badlogic.gdx.Screen;

import java.util.ArrayList;
import java.util.Random;

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

    @Override
    public void show() {
        random = new Random();
        batch = new SpriteBatch();
        font = new BitmapFont();
        Inventory = new ArrayList<>();

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
            message = "Objet repare !";
        } else {
            message = "Pas assez d'energie pour reparer cet objet.";
            System.out.println(message);
        }
    }
}

        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {

            if (!Inventory.isEmpty()) {

                Item selectedItem = Inventory.get(selectedIndex);

                money += selectedItem.value;

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
        font.draw(batch, " R = Réparer | V = Vendre | Haut/Bas = Sélection ", 100, 450);
        font.draw(batch, "Énergie : " + energy + "/" + maxEnergy, 500, 350);
        font.draw(batch, message, 100, 380);
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
                + " - Energie : " + item.energyCost,
                100,
                y
            );

            y -= 40;

        }
        if (Inventory.isEmpty()) {
            font.draw(batch, "Aucun objet en stock. Appuyez sur ESPACE pour passer au jour suivant.", 100, 300);
        }
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
