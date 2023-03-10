package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IndividualFuelCells extends AppCompatActivity {

    private Button returnHome;
    ArrayList<Float> voltages = new ArrayList<Float>();
    float voltageLevel;
    String fc1Alert;
    String fc1Date;
    String fc1Voltage;


    //variables for reading from database
    private DatabaseReference databaseRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final String TAG = "error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_fuel_cells);

        final TextView voltage1 = findViewById(R.id.fuelcell1voltage);
        final TextView voltage2 = findViewById(R.id.fuelcell2voltage);
        final TextView voltage3 = findViewById(R.id.fuelcell3voltage);
        final TextView voltage4 = findViewById(R.id.fuelcell4voltage);
        final TextView voltage5 = findViewById(R.id.fuelcell5voltage);
        final TextView voltage6 = findViewById(R.id.fuelcell6voltage);
        final TextView voltage7 = findViewById(R.id.fuelcell7voltage);
        final TextView voltage8 = findViewById(R.id.fuelcell8voltage);
        final TextView voltage9 = findViewById(R.id.fuelcell9voltage);
        final TextView voltage10 = findViewById(R.id.fuelcell10voltage);
        final TextView voltage11 = findViewById(R.id.fuelcell11voltage);
        final TextView voltage12 = findViewById(R.id.fuelcell12voltage);
        final TextView voltage13 = findViewById(R.id.fuelcell13voltage);
        final TextView voltage14 = findViewById(R.id.fuelcell14voltage);
        final TextView voltage15 = findViewById(R.id.fuelcell15voltage);
        final TextView voltage16 = findViewById(R.id.fuelcell16voltage);


        returnHome = (Button) findViewById(R.id.returnHome);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        final GlobalClass voltagesGlobal2 = (GlobalClass) getApplicationContext(); //global variable for voltage array
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
                    voltagesGlobal2.setVoltages(voltages);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        ArrayList<Float> test = voltagesGlobal2.getVoltages(); //local variable set to global variable so that values can accessed

        voltage1.setText(String.valueOf(test.get(0)));
        voltage2.setText(String.valueOf(test.get(1)));
        voltage3.setText(String.valueOf(test.get(2)));
        voltage4.setText(String.valueOf(test.get(3)));
        voltage5.setText(String.valueOf(test.get(4)));
        voltage6.setText(String.valueOf(test.get(5)));
        voltage7.setText(String.valueOf(test.get(6)));
        voltage8.setText(String.valueOf(test.get(7)));
        voltage9.setText(String.valueOf(test.get(8)));
        voltage10.setText(String.valueOf(test.get(9)));
        voltage11.setText(String.valueOf(test.get(10)));
        voltage12.setText(String.valueOf(test.get(11)));
        voltage13.setText(String.valueOf(test.get(12)));
        voltage14.setText(String.valueOf(test.get(13)));
        voltage15.setText(String.valueOf(test.get(14)));
        voltage16.setText(String.valueOf(test.get(15)));

    }

    public void openHome(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}