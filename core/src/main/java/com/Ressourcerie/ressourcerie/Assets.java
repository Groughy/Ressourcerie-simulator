package com.Ressourcerie.ressourcerie;

import com.badlogic.gdx.graphics.Texture;

public class Assets {

    public static Texture backgroundTexture;
    public static Texture panelLeftTexture;
    public static Texture panelRightTexture;
    public static Texture topbarTexture;
    public static Texture messagebarTexture;

    public static void load() {
        backgroundTexture = new Texture("ui/background.png");
        panelLeftTexture = new Texture("ui/panel_left.png");
        panelRightTexture = new Texture("ui/panel_right.png");
        topbarTexture = new Texture("ui/topbar.png");
        messagebarTexture = new Texture("ui/messagebar.png");
    }

    public static void dispose() {
        backgroundTexture.dispose();
        panelLeftTexture.dispose();
        panelRightTexture.dispose();

        if (topbarTexture != null) {
            topbarTexture.dispose();
        }

        if (messagebarTexture != null) {
            messagebarTexture.dispose();
        }
    }
}