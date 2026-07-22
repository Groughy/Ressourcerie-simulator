package com.Ressourcerie.ressourcerie.results;

import com.Ressourcerie.ressourcerie.items.Item;

public class SaleResult extends ActionResult {

    public final Item soldItem;
    public final int soldIndex;

    public SaleResult(boolean success, Item soldItem, String message, int soldIndex) {
        super(success, message);
        this.soldItem = soldItem;
        this.soldIndex = soldIndex;
    }
}