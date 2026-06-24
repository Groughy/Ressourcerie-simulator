package com.Ressourcerie.ressourcerie.menus;

public class WorkshopMenu {
    public boolean showWorkshopMenu = false;

    private void renderWorkshopMenu(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                electronicWorkshopLevel = upgradeWorkshop(electronicWorkshopLevel, "Atelier electronique");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                mechanicalWorkshopLevel = upgradeWorkshop(mechanicalWorkshopLevel, "Atelier mecanique");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                woodWorkshopLevel = upgradeWorkshop(woodWorkshopLevel, "Atelier mobilier");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                decorationWorkshopLevel = upgradeWorkshop(decorationWorkshopLevel, "Atelier decoration");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                textileWorkshopLevel = upgradeWorkshop(textileWorkshopLevel, "Atelier Textile");
            }

            batch.begin();

            font.draw(batch, "=== ATELIERS ===", 100, 420);
            font.draw(batch, "1, - Electronique niv. " + electronicWorkshopLevel, 100, 370);
            font.draw(batch, "2, - Mécanique niv. " + mechanicalWorkshopLevel, 100, 340);
            font.draw(batch, "3, - Mobilier niv. " + woodWorkshopLevel, 100, 310);
            font.draw(batch, "4, - Décoration niv. " + decorationWorkshopLevel, 100, 280);
            font.draw(batch, "5, - Textile niv. " + textileWorkshopLevel, 100, 250);
            font.draw(batch, "Appuyez sur le numéro de l'atelier pour l'améliorer.", 100, 220);
            font.draw(batch, "Appuyez sur A pour fermer ce menu.", 100, 190);

            batch.end();
    }

    private int getWorkshopUpgradeCost(int level) {
        return 100 * level;
    }

    private int upgradeWorkshop(int currentLevel, String workshopName) {
        int cost = getWorkshopUpgradeCost(currentLevel);

        if (money < cost) {
            message = "Pas assez d'argent pour améliorer " + workshopName + ".";
            return currentLevel;
        }

        money -= cost;
        currentLevel++;

        message = workshopName + " amélioré niveau " + currentLevel + " !";
        return currentLevel;
    }

    if (showWorkshopMenu) {

            renderWorkshopMenu();
            return;
        }

}
