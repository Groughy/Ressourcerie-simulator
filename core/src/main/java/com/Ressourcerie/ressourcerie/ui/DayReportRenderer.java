package com.Ressourcerie.ressourcerie.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.Gdx;

public class DayReportRenderer {

    public void render(
        SpriteBatch batch,
        BitmapFont font,
        int day,
        int dailyMoneyEarned,
        int dailyMoneySpent,
        int dailyItemsSold,
        int dailyItemsReceived,
        int dailyHappyCustomers,
        int dailyNeutralCustomers,
        int dailyUnhappyCustomers,
        int dailySalesRefused,
        int dailySalariesPaid,
        int dailyReputationChange
    ){
        int dailyProfit = dailyMoneyEarned - dailyMoneySpent;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        float reportWidth = 400;

        float leftX = centerX - reportWidth / 2f;
        float rightX = centerX + 70;

        float titleY  = centerY + 220;

        float startY = centerY + 140;

        float line = 35;

        GlyphLayout title = new GlyphLayout();

        title.setText(font, "=== RAPPORT JOURNALIER ===");
        font.draw(batch, title, (screenWidth - title.width) / 2f, titleY);

        float y = startY;
        font.draw(batch, "Objets reçus : " + dailyItemsReceived, leftX, y);
        y -= line;

        font.draw(batch, "Objets vendus : " + dailyItemsSold, leftX, y);
        y -= line;

        font.draw(batch, "Argent gagné : " + dailyMoneyEarned + " euros", leftX, y);
        y -= line;

        font.draw(batch, "Argent dépensé : " + dailyMoneySpent + " euros", leftX, y);
        y -= line;

        font.draw(batch, "Salaires payés : " + dailySalariesPaid + " euros", leftX, y);
        y -= line;

        font.draw(batch, "Profit : " + dailyProfit + " euros", leftX, y);

        float y2 = startY;
        font.draw(batch, "Clients heureux : " + dailyHappyCustomers, rightX, y2);
        y2 -= line;

        font.draw(batch, "Clients neutres : " + dailyNeutralCustomers, rightX, y2);
        y2 -= line;

        font.draw(batch, "Clients mécontents : " + dailyUnhappyCustomers, rightX, y2);
        y2 -= line;

        font.draw(batch, "Ventes refusées : " + dailySalesRefused, rightX, y2);
        y2 -= line;

        font.draw(batch, "Changement de réputation : " + (dailyReputationChange >= 0 ? "+" : "") + dailyReputationChange, rightX, y2);

        GlyphLayout layout = new GlyphLayout();

        layout.setText(font, "Appuyez sur Entrée pour continuer");
        font.draw(batch, layout, (screenWidth - layout.width) / 2f, 50);
    }

}
