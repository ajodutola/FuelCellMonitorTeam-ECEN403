package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    public CardView card1, card2, card3, card4, errorCard;

    ArrayList<Float> voltages = new ArrayList<Float>();
    float voltageLevel;
    String fc1Alert;
    String fc1Date;
    String fc1Voltage;

    float minValue, maxValue;

    //variables for reading from database
    private DatabaseReference databaseRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final String TAG = "error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final TextView errorDetection = findViewById(R.id.errorDashboard);

        //card1 = (CardView) findViewById(R.id.fuel_cell_stack);

        card2 = (CardView) findViewById(R.id.indiv_fuel_cell);
        card3 = (CardView) findViewById(R.id.historical_data);
        card4 = (CardView) findViewById(R.id.status_page);
        errorCard = (CardView) findViewById(R.id.setMarkers);

        //card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        errorCard.setOnClickListener(this);

        final GlobalClass voltagesGlobalD = (GlobalClass) getApplicationContext(); //global variable for voltage array
        //read info from database
        databaseRef = database.getReference("fuelcells");

        for (int i = 1; i <= 16; i++) { //iterate through fuel cells
            DatabaseReference fuelcellRef = databaseRef.child("fuelcell" + String.valueOf(i));
            fuelcellRef.addValueEventListener(new ValueEventListener() { //updates every time changes are made to database
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            fc1Alert = ds.getValue(String.class);
                            fc1Date = ds.getValue(String.class);
                            fc1Voltage = ds.getValue(String.class);
                        }
                    }

                    voltageLevel = Float.parseFloat(fc1Voltage);
                    voltages.add(voltageLevel);
                    voltagesGlobalD.setVoltages(voltages);

                    ArrayList<Float> test = voltagesGlobalD.getVoltages(); //local variable set to global variable
                    //ArrayList<int> fuelcellError = null; //list of problem cells

                    Intent intent = getIntent();
                    minValue = intent.getFloatExtra("minVolt", 0);
                    maxValue = intent.getFloatExtra("maxVolt", 2);
                    try {
                        for (int i = 0; i < test.size(); i++) {
                            if ((test.get(i) < minValue) || (test.get(i) > maxValue) || (test.get(i) == null)) {
                                //fuelcellError.add(i);
                                errorDetection.setText("Fuel Cell " + (i + 1) + " is out of range");
                                System.out.println("inside for loop :)");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error detected with for loop!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

//        ArrayList<Float> test = voltagesGlobalD.getVoltages(); //local variable set to global variable
//        //ArrayList<int> fuelcellError = null; //list of problem cells
//
//        Intent intent = getIntent();
//        minValue = intent.getFloatExtra("minVolt", 0);
//        maxValue = intent.getFloatExtra("maxVolt", 2);
//        try {
//            for (int i = 0; i < test.size(); i++) {
//                if ((test.get(i) < minValue) || (test.get(i) > maxValue)) {
//                    //fuelcellError.add(i);
//                    errorDetection.setText("Fuel Cell " + (i + 1) + " is out of range");
//                    System.out.println("inside for loop :)");
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error detected with for loop!");
//        }

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

            case R.id.setMarkers:
                i = new Intent(this, date_input.class);
                startActivity(i);
                break;
        }
    }
}