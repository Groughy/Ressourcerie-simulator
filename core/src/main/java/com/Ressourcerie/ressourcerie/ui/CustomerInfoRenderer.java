package com.Ressourcerie.ressourcerie.ui;

import com.Ressourcerie.ressourcerie.customer.Customer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CustomerInfoRenderer {

    public void render(SpriteBatch batch, BitmapFont font, Customer customer){
        if (customer == null) {
            return;
        }

        font.draw(batch, "Client : " + customer.name, 430, 200);
        font.draw(batch, "Budget : " + customer.budget + " euros", 430, 175);
        font.draw(batch, "Recherche : " + customer.wantedItems, 430, 150);
        font.draw(batch, "Type : " + customer.customerType, 430, 125);
    }

}
