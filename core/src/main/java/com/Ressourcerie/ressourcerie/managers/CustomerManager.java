package com.Ressourcerie.ressourcerie.managers;

import java.util.Random;
import com.Ressourcerie.ressourcerie.customer.Customer;

import com.Ressourcerie.ressourcerie.config.GameBalance;




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

}
