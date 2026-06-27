package com.Ressourcerie.ressourcerie.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MessageRenderer {

    public void render(SpriteBatch batch, BitmapFont font, String message){
        if (message == null || message.isEmpty()){
            return;
        }
        font.draw(batch, message, 40, 40);
    }

}
