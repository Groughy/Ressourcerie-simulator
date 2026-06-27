package com.Ressourcerie.ressourcerie.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class WorkshopRenderer {

    public void render(
        SpriteBatch batch, 
        BitmapFont font, 
        int electronicLevel,
        int mechanicLevel,
        int woodLevel,
        int decorationLevel,
        int textileLevel
    ){

        font.draw(batch, "=== ATELIERS ===", 100,420);

        font.draw(batch, "1 - Electronique niv. " + electronicLevel, 100, 370);
        font.draw(batch, "2 - Mecanique niv. " + mechanicLevel, 100, 340);
        font.draw(batch, "3 - Bois niv. " + woodLevel, 100, 310);
        font.draw(batch, "4 - Decoration niv. " + decorationLevel, 100, 280);
        font.draw(batch, "5 - Textile niv. " + textileLevel, 100, 250);

        font.draw(batch, "1/2/3/4/5 pour ameliorer un atelier", 100, 200);
        font.draw(batch, "A = fermer", 100, 170);

    }

}
