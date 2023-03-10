package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button indivFuelCells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indivFuelCells = (Button) findViewById(R.id.indivFuelCells);
        indivFuelCells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIndivFuelCells();
            }
        });
    }

    public void openIndivFuelCells() {
        Intent intent = new Intent(this, IndividualFuelCells.class);
        startActivity(intent);
    }
}