package com.Ressourcerie.ressourcerie;

public class Employee {

    public String name;
    public int skill;
    public int dailySalary;
    public int level;

    public Employee (String name, int skill, int dailySalary){
        this.name = name;
        this.skill = skill;
        this.dailySalary = dailySalary;
        this.level = 1;
    }

    public int getRepairPower(){
        return 5 + (level * 5);
    }

    public Employee(){}
}
