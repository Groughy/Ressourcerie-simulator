package com.Ressourcerie.ressourcerie.ui;

import java.util.ArrayList;

import com.Ressourcerie.ressourcerie.Assets;
import com.Ressourcerie.ressourcerie.items.Item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class StockRenderer {

    public void render(
        SpriteBatch batch,
        BitmapFont font,
        ArrayList<Item> inventory,
        ArrayList<Item> sellingStock,
        int maxInventorySize,
        int maxSellingStockSize,
        int storageLevel,
        int storageUpgradeCost
    ){
        font.draw(batch, "=== STOCK / ENTREPOT ===", 100, 430);

        font.draw(batch, "Entrepot niveau : " + storageLevel, 100, 390);
        font.draw(batch, "Stock reparation : " + inventory.size() + "/" + maxInventorySize, 100, 360);
        font.draw(batch, "Stock vente : " + sellingStock.size() + "/" + maxSellingStockSize, 100, 330);
        font.draw(batch, "Amelioration : " + storageUpgradeCost, 100, 300);
        font.draw(batch, "6 = Ameliorer l'entrepot", 100, 270);
        font.draw(batch, "S = Fermer", 100, 240);

        int y = 200;

        if (sellingStock.isEmpty()){
            font.draw(batch, "Aucun objet en vente", 100, y);
            return;
        }

        for (Item item : sellingStock){
            Texture icon = Assets.getIconForType(item.type);
            batch.draw(icon, 100, y - 16, 18, 18);
            font.draw(batch,
                item.name
                + " | Prix : " + item.salePrice
                + " | Etat : " + item.condition + "%"
                + " | Rarete : " + item.rarety
                + " | Type : " + item.type,
                124, y
            );
            y -= 20;
        }
    }

}
