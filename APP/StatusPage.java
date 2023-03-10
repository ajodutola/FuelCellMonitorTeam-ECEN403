package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StatusPage extends AppCompatActivity {

    //defining variables needed for graph and reading from database
    BarChart barChart;
    ArrayList<String> fuelCells = new ArrayList<>();
    ArrayList<Float> voltages = new ArrayList<Float>();
    ArrayList<BarEntry> barEntries;
    BarDataSet barDataSet;
    BarData barData;
    float voltageLevel;

    private DatabaseReference databaseRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Button returnHome;
    private List<FuelCell> fuelCell = new ArrayList<>();
    FuelCellInformation fc1Info = new FuelCellInformation();

    String fc1Alert;
    String fc1Date;
    String fc1Voltage;

    private static final String TAG = "error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_page);

        returnHome = (Button) findViewById(R.id.returnHomeFromStatus);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        //array list for fuel cell names
        fuelCells.add("Fuel Cell 1");
        fuelCells.add("Fuel Cell 2");
        fuelCells.add("Fuel Cell 3");
        fuelCells.add("Fuel Cell 4");
        fuelCells.add("Fuel Cell 5");
        fuelCells.add("Fuel Cell 6");
        fuelCells.add("Fuel Cell 7");
        fuelCells.add("Fuel Cell 8");
        fuelCells.add("Fuel Cell 9");
        fuelCells.add("Fuel Cell 10");
        fuelCells.add("Fuel Cell 11");
        fuelCells.add("Fuel Cell 12");
        fuelCells.add("Fuel Cell 13");
        fuelCells.add("Fuel Cell 14");
        fuelCells.add("Fuel Cell 15");
        fuelCells.add("Fuel Cell 16");

        barChart = (BarChart) findViewById(R.id.status_page);
        barEntries = new ArrayList<>();
        final GlobalClass voltagesGlobal = (GlobalClass) getApplicationContext(); //global variable for voltage array

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
                    voltagesGlobal.setVoltages(voltages);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        ArrayList<Float> test = voltagesGlobal.getVoltages(); //local variable set to global variable so that values can accessed

        //for loop adding values as bar entries
        for (int i = 0; i < test.size(); i++) {
            barEntries.add(new BarEntry(test.get(i), i+1));
        }

        //setting up graph
        barDataSet = new BarDataSet(barEntries, "Fuel Cells");
        barDataSet.setColors(ColorTemplate.LIBERTY_COLORS, 255);
        barDataSet.setValueTextColor(0);
        barData = new BarData(fuelCells, barDataSet);
        barChart.setData(barData);
        barChart.setDescription("Status of Fuel Cells");
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

        //refreshing the graph with new data
        barChart.notifyDataSetChanged();
        barChart.invalidate();

    }

    //return home button functionality
    public void openHome(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

}


