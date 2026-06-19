package com.Ressourcerie.ressourcerie.managers;

import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;

public class EmployeeManager {

    private void recruitEmployee(){
        int cost = 200;

        if (money < cost){
            message = "Pas assez d'argent.";
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

        String specialty = spectialties[random.nextInt(specialties.length)];

        Employee employee =
            new Employee("Employe " + (employees.size() + 1), 10, 20, specialty);
        
            employees.add(employee);

            message = employee.name + " recrute.";
    }

    private void trainEmployee(Employee employee){

        int cost = 100;

        if (money < cost){
            message = "Pas assez d'argent.";
            return;
        }

        money -= cost;
        employee.level++;
        message = employee.name + " entraîné.";
    }

    private void repairEmployee(Employee employee){
        for (Item item : Inventory){
                if (item.condition < 100 && item.type.equals(employee.specialty)){
                    item.repair(employee.getRepairPower());
                    break;
                }
            }
            money -= employee.dailySalary;
    }

}
