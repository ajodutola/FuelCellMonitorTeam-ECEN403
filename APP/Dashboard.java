package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    public CardView card1, card2, card3, card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //card1 = (CardView) findViewById(R.id.fuel_cell_stack);
        card2 = (CardView) findViewById(R.id.indiv_fuel_cell);
        card3 = (CardView) findViewById(R.id.historical_data);
        card4 = (CardView) findViewById(R.id.status_page);

        //card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
           /* case R.id.fuel_cell_stack:
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;*/

            case R.id.indiv_fuel_cell:
                i = new Intent(this, IndividualFuelCells.class);
                startActivity(i);
                break;

            case R.id.historical_data:
                i = new Intent(this, HistoricalTrends.class);
                startActivity(i);
                break;

            case R.id.status_page:
                i = new Intent(this, StatusPage.class);
                startActivity(i);
                break;
        }
    }
}