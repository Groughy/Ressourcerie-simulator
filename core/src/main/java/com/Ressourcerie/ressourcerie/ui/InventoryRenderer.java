package com.Ressourcerie.ressourcerie.ui;

import java.util.ArrayList;

import com.Ressourcerie.ressourcerie.Assets;
import com.Ressourcerie.ressourcerie.items.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InventoryRenderer {

    public void render(SpriteBatch batch, BitmapFont font, ArrayList<Item> inventory, int selectedIndex) {
       int y = 320;
       font.draw(batch, "Inventaire", 40, 360);

       for (int i = 0; i < inventory.size(); i++){
            Item item = inventory.get(i);
            String prefix = i == selectedIndex ? "> " : "  ";
            Texture icon = Assets.getIconForType(item.type);

            batch.draw(icon, 40, y - 22, 26, 26);
            font.draw(batch, prefix + item.name + " | Etat : " + item.condition + "%" + " | Type : " + item.type, 72, y);
            y -= 28;
       }
    }

}
