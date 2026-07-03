package com.Ressourcerie.ressourcerie.managers;

import com.Ressourcerie.ressourcerie.SaveData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SaveManager {

    private static final String SAVE_FILE_NAME = "save.json";
    private String message = "";

    public boolean saveGame(SaveData data) {
        if (data == null) {
            message = "Impossible de sauvegarder une partie vide.";
            return false;
        }

        try {
            Json json = new Json();
            FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
            file.writeString(json.prettyPrint(data), false, "UTF-8");
            message = "Partie sauvegardée.";
            return true;
        } catch (RuntimeException exception) {
            message = "Échec de la sauvegarde : " + exception.getMessage();
            return false;
        }
    }

    public SaveData loadGame() {
        FileHandle file = Gdx.files.local(SAVE_FILE_NAME);

        if (!file.exists()) {
            message = "Aucune sauvegarde trouvée.";
            return null;
        }

        try {
            Json json = new Json();
            String contents = file.readString("UTF-8");
            if (!contents.contains("\"day\"") || !contents.contains("\"money\"")) {
                message = "La sauvegarde est vide ou incomplète.";
                return null;
            }

            SaveData data = json.fromJson(SaveData.class, contents);
            if (data == null) {
                message = "La sauvegarde est vide ou invalide.";
                return null;
            }
            message = "Partie chargée.";
            return data;
        } catch (RuntimeException exception) {
            message = "Sauvegarde illisible : " + exception.getMessage();
            return null;
        }
    }

    public String getMessage() {
        return message;
    }
}
