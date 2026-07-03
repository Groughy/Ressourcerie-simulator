package com.Ressourcerie.ressourcerie;

import java.text.Normalizer;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

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

    private static boolean loaded;

    public static void load() {
        if (loaded) {
            return;
        }

        backgroundTexture = new Texture("ui/background.png");
        panelLeftTexture = new Texture("ui/panel_left.png");
        panelRightTexture = new Texture("ui/panel_right.png");

        if (Gdx.files.internal("ui/topbar.png").exists()) {
            topbarTexture = new Texture("ui/topbar.png");
        }
        if (Gdx.files.internal("ui/messagebar.png").exists()) {
            messagebarTexture = new Texture("ui/messagebar.png");
        }

        iconElectronic = new Texture("ui/icons/icon_electronic.png");
        iconMechanic = new Texture("ui/icons/icon_mechanic.png");
        iconFurniture = new Texture("ui/icons/icon_furniture.png");
        iconDecoration = new Texture("ui/icons/icon_decoration.png");
        iconTextile = new Texture("ui/icons/icon_textile.png");

        iconElectronic.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        iconMechanic.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        iconFurniture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        iconDecoration.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        iconTextile.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        loaded = true;
    }

    public static Texture getIconForType(String type) {
        if (type == null) {
            return iconDecoration;
        }

        String normalizedType = Normalizer.normalize(type, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT);

        switch (normalizedType) {
            case "electronique":
                return iconElectronic;
            case "mecanique":
                return iconMechanic;
            case "mobilier":
            case "meuble":
                return iconFurniture;
            case "textile":
                return iconTextile;
            case "decoration":
            case "divers":
            default:
                return iconDecoration;
        }
    }

    public static void dispose() {
        disposeTexture(backgroundTexture);
        disposeTexture(panelLeftTexture);
        disposeTexture(panelRightTexture);
        disposeTexture(topbarTexture);
        disposeTexture(messagebarTexture);
        disposeTexture(iconElectronic);
        disposeTexture(iconMechanic);
        disposeTexture(iconFurniture);
        disposeTexture(iconDecoration);
        disposeTexture(iconTextile);

        backgroundTexture = null;
        panelLeftTexture = null;
        panelRightTexture = null;
        topbarTexture = null;
        messagebarTexture = null;
        iconElectronic = null;
        iconMechanic = null;
        iconFurniture = null;
        iconDecoration = null;
        iconTextile = null;
        loaded = false;
    }

    private static void disposeTexture(Texture texture) {
        if (texture != null) {
            texture.dispose();
        }
    }
}
