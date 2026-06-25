package com.Ressourcerie.ressourcerie.input;

import com.badlogic.gdx.Input;

public class GameKeys {
    public static int REPAIR = Input.Keys.R;
    public static int SELL = Input.Keys.V;
    public static int WORKSHOP = Input.Keys.A;
    public static int COFFEE = Input.Keys.B;
    public static int EMPLOYEE = Input.Keys.E;
    public static int KIT = Input.Keys.T;
    public static int STOCK = Input.Keys.S;
    public static int HELP = Input.Keys.F1;
    public static int SAVE = Input.Keys.F5;
    public static int LOAD = Input.Keys.F9;
    public static int CANCEL = Input.Keys.ESCAPE;
    public static int DOWN = Input.Keys.DOWN;
    public static int UP = Input.Keys.UP;
    public static int EQUALS = Input.Keys.EQUALS;
    public static int PLUS = Input.Keys.PLUS;
    public static int MINUS = Input.Keys.MINUS;
    public static int SPACE = Input.Keys.SPACE;
    public static int ENTER = Input.Keys.ENTER;
    public static int AZERTYMODE = Input.Keys.F2;

    public static boolean azertyMode = true;

    public static void switchKeyboardMode(){
        azertyMode = !azertyMode;
        applyKeyboardMode();
    }

    public static void applyKeyboardMode(){
        if (azertyMode){
            REPAIR = Input.Keys.R;
            SELL = Input.Keys.V;
            WORKSHOP = Input.Keys.A;
            EMPLOYEE = Input.Keys.E;
            STOCK = Input.Keys.S;
        } else {
            REPAIR = Input.Keys.R;
            SELL = Input.Keys.V;
            WORKSHOP = Input.Keys.Q;
            EMPLOYEE = Input.Keys.E;
            STOCK = Input.Keys.S;
        }
    }
}
