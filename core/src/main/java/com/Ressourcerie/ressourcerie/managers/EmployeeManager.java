package com.Ressourcerie.ressourcerie.managers;

import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;

import java.util.List;
import java.util.Random;

public class EmployeeManager {

    public static final int RECRUITMENT_COST = 200;
    public static final int TRAINING_COST = 100;

    private final Random random = new Random();

    public Employee createEmployee(int employeeNumber) {
        String[] specialties = {
            "Electronique",
            "Mécanique",
            "Mobilier",
            "Décoration",
            "Textile"
        };

        String specialty = specialties[random.nextInt(specialties.length)];
        return new Employee("Employé " + employeeNumber, 10, 20, specialty);
    }

    public void trainEmployee(Employee employee) {
        if (employee == null) {
            return;
        }
        employee.level++;
    }

    public int repairEmployee(Employee employee, List<Item> inventory) {
        if (employee == null || inventory == null) {
            return 0;
        }

        for (Item item : inventory) {
            if (item != null
                    && item.condition < 100
                    && employee.specialty != null
                    && employee.specialty.equals(item.type)) {
                int failChance = 20 - (employee.level * 3);

                if (failChance < 5) {
                    failChance = 5;
                }

                if (random.nextInt(100) < failChance) {
                    item.condition -= 5;

                    if (item.condition < 0) {
                        item.condition = 0;
                    }
                    return -1;
                } else {
                    item.repair(employee.getRepairPower());
                    return 1;
                }
            }
        }
        return 0;
    }

    public int getTotalSalaries(List<Employee> employees) {
        if (employees == null) {
            return 0;
        }

        int totalSalaries = 0;
        for (Employee employee : employees) {
            if (employee != null) {
                totalSalaries += Math.max(0, employee.dailySalary);
            }
        }
        return totalSalaries;
    }
}
