package com.Ressourcerie.ressourcerie.managers;

import java.util.Random;
import java.util.ArrayList;
import com.Ressourcerie.ressourcerie.customer.Customer;
import com.Ressourcerie.ressourcerie.items.Item;
import com.Ressourcerie.ressourcerie.config.GameBalance;
import com.Ressourcerie.ressourcerie.results.SaleResult;




public class CustomerManager {

    private Random random = new Random();
    
    public int reputation;


public Customer createRandomCustomer() {


        String[] names = { "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy" };
        String[] wantedItems = { "Vieille radio", "Lampe cassée", "Chaise en bois", "Vieux vélo", "Table abîmée",
                "Ordinateur obsolète", "Télévision ancienne", "Machine à écrire", "Guitare désaccordée", "Canapé usé",
                "Vase ébréché", "Montre cassée", "Appareil photo vintage", "Jouet en bois", "Livre ancien" };
        String name = names[random.nextInt(names.length)];
        int budget = random.nextInt(GameBalance.CUSTOMER_BUDGET_RANGE) + GameBalance.MIN_CUSTOMER_BUDGET;
        String wantedItem = wantedItems[random.nextInt(wantedItems.length)];
        String[] customerTypes = {
                "Normal",
                "Collectionneur",
                "Bricoleur",
                "Exigeant"
        };
        String customerType;

        if (reputation > 80 && random.nextInt(100) < 40) {

            customerType = "Collectionneur";

        } else {

            customerType = customerTypes[random.nextInt(customerTypes.length)];
        }
        return new Customer(name, budget, wantedItem, customerType);
    }

    public boolean customerWantsItem(Customer customer, Item item){
        return item != null
            && customer != null
            && item.name != null
            && item.name.equals(customer.wantedItems);
    }

    public boolean customerCanPay(Customer customer, Item item){
        return item.salePrice <= customer.budget;
    }

    public boolean isPriceTooHigh(Item item){
        return item.salePrice > item.value * GameBalance.MAX_ACCEPTABLE_PRICE_MULTIPLIER;
    }

    public boolean isGoodDeal(Item item){
        return item.salePrice < item.value * GameBalance.GOOD_DEAL_PRICE_MULTIPLIER;
    }

    public Customer getOrCreateCustomer(Customer currentCustomer, int reputation){
        this.reputation = reputation;
        
        if (currentCustomer == null){
            return createRandomCustomer();
        }

        return currentCustomer;
    }

    public boolean acceptsItem(Customer customer, Item item){
        if ("Exigeant".equals(customer.customerType) && item.condition < 70){
            return false;
        }

        return true;
    }

    public SaleResult tryBuyItem(Customer customer, ArrayList<Item> sellingStock){
        if (customer == null){
            return new SaleResult (false, false, null, -1, "Aucun client disponible");
        }

        if (sellingStock == null || sellingStock.isEmpty()){
            return new SaleResult (false, false, null, -1, "Aucun objet à vendre");
        }

        for (int i = 0; i < sellingStock.size(); i++){
            Item item = sellingStock.get(i);

            if (!customerWantsItem(customer, item)){
                continue;
            }

            if (!customerCanPay(customer, item)){
                return new SaleResult(false, true, item, i, customer.name + " n'a pas assez d'argent.");
            }

            if (isPriceTooHigh(item)){
                return new SaleResult(false, true, item, i, customer.name + " trouve le prix trop élevé.");
            }

            if (!acceptsItem(customer, item)){
                return new SaleResult(false, true, item, i, customer.name + " refuse d'acheter un objet en mauvais état.");
            }

            return new SaleResult(true, false, item, i, customer.name + " acheter " + item.name + ".");
        }

        return new SaleResult(false, false, null, -1, customer.name + " ne trouve aucun objet qui l'intéresse.");
    }
}
