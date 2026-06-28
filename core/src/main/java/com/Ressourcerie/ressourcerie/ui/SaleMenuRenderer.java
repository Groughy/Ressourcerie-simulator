package com.Ressourcerie.ressourcerie.ui;

import com.Ressourcerie.ressourcerie.items.Item;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class SaleMenuRenderer {

    public void render(
        SpriteBatch batch,
        BitmapFont font,
        Item selectedItem,
        int currentSalePrice
    ){
        font.draw(batch, "=== MISE EN VENTE ===", 100, 420);

        font.draw(batch, selectedItem.name, 100, 380);
        font.draw(batch, "Valeur conseillee : " + selectedItem.value + " euros", 100, 340);
        font.draw(batch, "Prix choisi : " + currentSalePrice + " euros", 100, 300);
        font.draw(batch, "+ / - = Modifier le prix", 100, 250);
        font.draw(batch, "ENTREE = Confirmer la vente", 100, 220);
        font.draw(batch, "ECHAP = Annuler la vente", 100, 190);
    }

}
