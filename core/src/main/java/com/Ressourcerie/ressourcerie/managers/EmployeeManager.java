package com.Ressourcerie.ressourcerie.managers;

import com.Ressourcerie.ressourcerie.employees.Employee;
import com.Ressourcerie.ressourcerie.items.Item;

import com.Ressourcerie.ressourcerie.config.GameBalance;

import java.util.List;
import java.util.Random;

public class EmployeeManager {

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
                int failChance = GameBalance.EMPLOYEE_BASE_FAIL_CHANCE - (employee.level * GameBalance.EMPLOYEE_FAIL_REDUCTION_PER_LEVEL);

                if (failChance < GameBalance.EMPLOYEE_MIN_FAIL_CHANCE) {
                    failChance = GameBalance.EMPLOYEE_MIN_FAIL_CHANCE;
                }

                if (random.nextInt(100) < failChance) {
                    item.condition -= GameBalance.EMPLOYEE_FAILURE_DAMAGE;

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
