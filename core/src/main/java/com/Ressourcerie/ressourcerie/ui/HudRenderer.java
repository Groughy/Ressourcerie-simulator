package com.Ressourcerie.ressourcerie.ui;

import com.Ressourcerie.ressourcerie.managers.ReputationManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HudRenderer {

    public void render(
        SpriteBatch batch,
        BitmapFont font,
        int day,
        int money,
        int energy,
        int maxEnergy,
        int reputation){
            font.draw(batch, "Jour : " + day, 20, 450);
            font.draw(batch, "Argent : " + money + " euros", 130, 450);
            font.draw(batch, "Énergie : " + energy + "/" + maxEnergy, 330, 450);
            font.draw(batch, "Réputation : " + reputation + " (" + ReputationManager.getReputationRank(reputation) + ")", 20, 425);
        }

}
