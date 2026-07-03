package com.Ressourcerie.ressourcerie.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HelpRenderer {

    public void render(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, "=== AIDE / COMMANDES ===", 100, 430);

            font.draw(batch, "R = Reparer", 100, 380);
            font.draw(batch, "V = Mise en vente", 100, 350);
            font.draw(batch, "S = Stock", 100, 320);
            font.draw(batch, "A = Ateliers", 100, 290);
            font.draw(batch, "E = Employes", 100, 260);
            font.draw(batch, "C = Accueillir le client", 100, 230);

            font.draw(batch, "B = Cafe", 450, 380);
            font.draw(batch, "T = Kit outils", 450, 350);
            font.draw(batch, "F2 = AZERTY/QWERTY", 450, 320);
            font.draw(batch, "F5 = Sauvegarder", 450, 290);
            font.draw(batch, "F9 = Charger", 450, 260);

            font.draw(batch, "Fleches = Selection", 100, 180);
            font.draw(batch, "ENTREE = Valider", 100, 150);
            font.draw(batch, "ECHAP = Annuler", 100, 120);
            font.draw(batch, "F1 = Fermer aide", 100, 90);
    }

}
