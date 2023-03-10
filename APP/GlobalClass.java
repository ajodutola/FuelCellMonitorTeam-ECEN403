package com.example.myfirstapp;

import android.app.Application;

import java.util.ArrayList;

public class GlobalClass extends Application {
    private ArrayList<Float> voltages = getVoltages();

    public ArrayList<Float> getVoltages() {
        return voltages;
    }

    public void setVoltages(ArrayList<Float> voltages) {
        this.voltages = voltages;
    }
}
