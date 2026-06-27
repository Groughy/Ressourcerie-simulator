package com.Ressourcerie.ressourcerie.ui;

import java.util.ArrayList;

import com.Ressourcerie.ressourcerie.employees.Employee;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EmployeeRenderer {

    public void render(SpriteBatch batch, BitmapFont font, ArrayList<Employee> employees, int selectedIndex) {
        font.draw(batch, "=== EMPLOYES ===", 100, 430);
        
        font.draw(batch, "1 - Recruter un employe", 100, 390);
        font.draw(batch, "2 = Former un employe", 100, 360);
        font.draw(batch, "E = Fermer", 100, 330);

        int y = 280;
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            String prefix = (i == selectedIndex) ? ">" : " ";
            font.draw(batch, prefix + employee.name + 
                " | Niveau : " + employee.level + 
                " | Salaire : " + employee.dailySalary + " euros" + 
                " | Specialite : " + employee.specialty +
                " | Reparation : " + employee.getRepairPower(), 100, y);
            y -= 30;
        }
    }

}
