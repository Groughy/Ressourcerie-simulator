package com.Ressourcerie.ressourcerie.employees;

public class Employee {

    public String name;
    public int skill;
    public int dailySalary;
    public int level;
    public String specialty;

    public Employee (String name, int skill, int dailySalary, String specialty){
        this.name = name;
        this.skill = skill;
        this.dailySalary = dailySalary;
        this.specialty = specialty;
        this.level = 1;
    }

    public int getRepairPower(){
        return 5 + (level * 5);
    }

    public Employee(){}
}
