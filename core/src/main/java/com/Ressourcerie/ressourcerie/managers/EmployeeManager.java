package com.Ressourcerie.ressourcerie.managers;

import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;

public class EmployeeManager {

    private int money;
    private java.util.Random random = new java.util.Random();
    private java.util.ArrayList<Employee> employees;
    private java.util.List<Item> inventory;

    public EmployeeManager(){
        employees = new java.util.ArrayList<>();
        inventory = new java.util.ArrayList<>();
    }

    public void setMoney(int money){
        this.money = money;
    }

    public void setEmployees(java.util.ArrayList<Employee> employees){
        this.employees = employees;
    }

    public void setInventory(java.util.List<Item> inventory){
        this.inventory = inventory;
    }
    public void recruitEmployee(){
        int cost = 200;

        if (money < cost){
            return;
        }

        money -= cost;

        String[] specialties = {
            "Electronique",
            "Mécanique",
            "Mobilier",
            "Décoration",
            "Textile"
        };

        String specialty = specialties[random.nextInt(specialties.length)];

        Employee employee =
            new Employee("Employe " + (employees.size() + 1), 10, 20, specialty);
        
            employees.add(employee);
    }

    public void trainEmployee(Employee employee){

        int cost = 100;

        if (money < cost){
            return;
        }

        money -= cost;
        employee.level++;
    }

    public void repairEmployee(Employee employee){
        for (Item item : inventory){
                if (item.condition < 100 && item.type.equals(employee.specialty)){
                    int failChance = 20 - (employee.level * 3);

                    if (failChance < 5){
                        failChance = 5;
                    }

                    if (random.nextInt(100) < failChance){
                        item.condition -= 5;

                        if (item.condition < 0){
                            item.condition = 0;
                        }

                        message = employee.name + " a rate une reparation.";
                    } else{
                        item.repair(employee.getRepairPower());
                        message = employee.name + " a reussi une reparation.";
                    }
                    break;
                }
            }
            money -= employee.dailySalary;
    }

}
