package com.Ressourcerie.ressourcerie.results;

import com.Ressourcerie.ressourcerie.items.Item;

public class SaleResult extends ActionResult {

    public final Item soldItem;
    public final int soldIndex;
    public final boolean refused;

    public SaleResult(boolean success, boolean refused,Item soldItem, int soldIndex, String message) {
        super(success, message);
        this.soldItem = soldItem;
        this.soldIndex = soldIndex;
        this.refused = refused;
    }
}