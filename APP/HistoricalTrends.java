package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HistoricalTrends extends AppCompatActivity {

    ScatterChart scatterChart;
    ArrayList<String> times = new ArrayList<>();
    ArrayList<Entry> scatterEntries = new ArrayList<>();
    ScatterDataSet scatterDataSet;
    ScatterData scatterData;
    private Button returnHome;
    ArrayList<Float> voltages = new ArrayList<Float>();
    ArrayList<Float> voltagesHour = new ArrayList<Float>();
    float voltageLevel;
    int hour = 0;

    private DatabaseReference databaseRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    String fc1Alert;
    String fc1Date;
    String fc1Voltage;
    ArrayList<Float> voltagesGlobal = new ArrayList<Float>(); //global voltages array
    ArrayList<ArrayList<Float>> scatterVoltages = new ArrayList<>();


    private static final String TAG = "error";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_trends);

        returnHome = (Button) findViewById(R.id.returnHomeFromStatus);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        scatterChart = (ScatterChart) findViewById(R.id.historical_data);
        final GlobalClass voltagesGlobal3 = (GlobalClass) getApplicationContext(); //global variable for voltage array

        //read info from database
        databaseRef = database.getReference("fuelcells");

        for (int i = 1; i <= 16; i++) { //iterate through fuel cells
            DatabaseReference fuelcellRef = databaseRef.child("fuelcell" + String.valueOf(i));
            fuelcellRef.addValueEventListener(new ValueEventListener() { //updates every time changes are made to database
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String key = ds.getKey();
                            if (key.equals("alert")){
                                fc1Alert = ds.getValue(String.class);
                            } else if (key.equals("date")){
                                fc1Date = ds.getValue(String.class);
                            } else if (key.equals("voltageLevel")){
                                fc1Voltage = ds.getValue(String.class);
                            }
                        }
                    }

                    voltageLevel = Float.parseFloat(fc1Voltage);
                    voltages.add(voltageLevel);
                    voltagesGlobal = voltages;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        // Wait for some time to ensure all the data has been retrieved from the database
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createHistoricalGraph();
            }
        }, 1000);

    }

    private void createHistoricalGraph() { //create status graph
        ArrayList<Float> test = voltagesGlobal; //local variable set to global variable so that values can accessed

        scatterVoltages.add(test);

        scatterChart = (ScatterChart) findViewById(R.id.historical_data);
        scatterEntries = new ArrayList<>();

        //setting up graph
        scatterDataSet = new ScatterDataSet(scatterEntries, "Fuel Cells");
        scatterDataSet.setColors(ColorTemplate.LIBERTY_COLORS, 255);
        scatterDataSet.setValueTextColor(0);
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterDataSet.setScatterShapeSize(0.5f);
        scatterData = new ScatterData(getTimeLabels(), scatterDataSet);
        scatterChart.setData(scatterData);
        scatterChart.setDescription("Historical Trends of Fuel Cells");
        scatterChart.setTouchEnabled(true);
        scatterChart.setDragEnabled(true);
        scatterChart.setPinchZoom(true);
        scatterChart.setScaleEnabled(true);

        //refreshing the graph with new data
        scatterChart.notifyDataSetChanged();
        scatterChart.invalidate();


        // start a new thread to update the voltage levels every hour
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (hour < 24) { // run for 24 hours
                    try {
                        Thread.sleep(3600000); // wait for an hour
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateVoltageList(); // update voltage list with a new voltage level
                    updateScatterChart(); // update scatter chart with all voltage levels
                    hour++;
                }
            }
        }).start();
    }

    // method to update the scatter voltage list with a new voltage level
    private void updateVoltageList() {
        ArrayList<Float> test = voltagesGlobal; //local variable set to global variable so that values can accessed
        scatterVoltages.add(test);
    }

    // method to update the scatter chart with all voltage levels
//    private void updateScatterChart() {
//        ArrayList<Float> test = voltagesGlobal; //local variable set to global variable so that values can accessed
//
//        if (test != null) {
//            //for loop adding values as bar entries
//            for (int i = 0; i < scatterVoltages.size(); i++) {
//                for (int j = 0; j < test.size(); j++) {
//                    scatterEntries.add(new Entry(scatterVoltages.get(i).get(j), i));
//                }
//            }
//        }
//
//        scatterData = new ScatterData(getTimeLabels(), scatterDataSet); // update scatter data
//        scatterChart.setData(scatterData); // update scatter chart
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                scatterChart.notifyDataSetChanged(); // notify chart that data has changed
//                scatterChart.invalidate(); // refresh chart
//            }
//        });
//    }

    private void updateScatterChart() {
        ArrayList<Float> test = voltagesGlobal; // local variable set to global variable so that values can be accessed

        if (test != null) {
            // create a new ArrayList<Entry> to store the new entries that we will add to the chart
            ArrayList<Entry> newEntries = new ArrayList<>();

            // loop through all the values in scatter voltages
            for (int i = 0; i < scatterVoltages.size(); i++) {
                for (int j = 0; j < test.size(); j++){
                    // create a new Entry object with the x-value set to the index of the voltage and the y-value set to the current hour
                    Entry newEntry = new Entry(scatterVoltages.get(i).get(j), i); //Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    // add the new entry to newEntries
                    newEntries.add(newEntry);
                }
            }

            // add all the entries in scatterEntries to newEntries
            newEntries.addAll(scatterEntries);

            // update scatterDataSet with the new scatterEntries
            scatterDataSet = new ScatterDataSet(newEntries, "Fuel Cells");
            scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            scatterDataSet.setColors(ColorTemplate.LIBERTY_COLORS, 255);

            // set the data of scatterChart to a new ScatterData object created using getTimeLabels() and scatterDataSet
            scatterData = new ScatterData(getTimeLabels(), scatterDataSet);
            scatterChart.setData(scatterData);

            // notify the chart that the data has changed and refresh it
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scatterChart.notifyDataSetChanged();
                    scatterChart.invalidate();
                }
            });
        }
    }


    // method to get time labels for scatter chart
    private ArrayList<String> getTimeLabels() {
        ArrayList<String> timesL = new ArrayList<>();
        for (int i = 0; i < scatterVoltages.size(); i++) {
            timesL.add(String.format("%02d:00", i)); // add hour label for each voltage level
        }
        return timesL;
    }


    public void openHome(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}