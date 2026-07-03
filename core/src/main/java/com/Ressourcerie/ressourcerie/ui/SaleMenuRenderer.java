package com.Ressourcerie.ressourcerie.ui;

import com.Ressourcerie.ressourcerie.Assets;
import com.Ressourcerie.ressourcerie.items.Item;
import com.badlogic.gdx.graphics.Texture;
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

        Texture icon = Assets.getIconForType(selectedItem.type);
        batch.draw(icon, 100, 300, 72, 72);
        font.draw(batch, selectedItem.name, 190, 370);
        font.draw(batch, "Valeur conseillee : " + selectedItem.value + " euros", 190, 340);
        font.draw(batch, "Prix choisi : " + currentSalePrice + " euros", 190, 310);
        font.draw(batch, "+ / - = Modifier le prix", 100, 250);
        font.draw(batch, "ENTREE = Confirmer la vente", 100, 220);
        font.draw(batch, "ECHAP = Annuler la vente", 100, 190);
    }

}
