package com.Ressourcerie.ressourcerie;

import com.badlogic.gdx.graphics.Texture;

public class Assets {

    public static Texture backgroundTexture;
    public static Texture panelLeftTexture;
    public static Texture panelRightTexture;
    public static Texture topbarTexture;
    public static Texture messagebarTexture;

    public static Texture iconElectronic;
    public static Texture iconMechanic;
    public static Texture iconFurniture;
    public static Texture iconDecoration;
    public static Texture iconTextile;

    public static void load() {
        backgroundTexture = new Texture("ui/background.png");
        panelLeftTexture = new Texture("ui/panel_left.png");
        panelRightTexture = new Texture("ui/panel_right.png");
        topbarTexture = new Texture("ui/topbar.png");
        messagebarTexture = new Texture("ui/messagebar.png");
        iconElectronic = new Texture("ui/icon_electronic.png");
        iconMechanic = new Texture("ui/icon_mechanic.png");
        iconFurniture = new Texture("ui/icon_furniture.png");
        iconDecoration = new Texture("ui/icon_decoration.png");
        iconTextile = new Texture("ui/icon_textile.png");
    }

    public static void dispose() {
        backgroundTexture.dispose();
        panelLeftTexture.dispose();
        panelRightTexture.dispose();
        iconElectronic.dispose();
        iconMechanic.dispose();
        iconFurniture.dispose();
        iconDecoration.dispose();
        iconTextile.dispose();

        if (topbarTexture != null) {
            topbarTexture.dispose();
        }

        if (messagebarTexture != null) {
            messagebarTexture.dispose();
        }
    }
}