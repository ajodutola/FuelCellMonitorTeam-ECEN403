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

        //adding 24 hour period to time array for x-axis
        times.add("00:00");
        times.add("01:00");
        times.add("02:00");
        times.add("03:00");
        times.add("04:00");
        times.add("05:00");
        times.add("06:00");
        times.add("07:00");
        times.add("08:00");
        times.add("09:00");
        times.add("10:00");
        times.add("11:00");
        times.add("12:00");
        times.add("13:00");
        times.add("14:00");
        times.add("15:00");
        times.add("16:00");
        times.add("17:00");
        times.add("18:00");
        times.add("19:00");
        times.add("20:00");
        times.add("21:00");
        times.add("22:00");
        times.add("23:00");
        times.add("24:00");

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
        scatterData = new ScatterData(getTimeLabels(), scatterDataSet);
        scatterChart.setData(scatterData);
        scatterChart.setDescription("Historical Trends of Fuel Cells");
        scatterChart.setTouchEnabled(true);
        scatterChart.setDragEnabled(true);
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
    private void updateScatterChart() {
        ArrayList<Float> test = voltagesGlobal; //local variable set to global variable so that values can accessed

        if (test != null) {
            //for loop adding values as bar entries
            for (int i = 0; i < scatterVoltages.size(); i++) {
                for (int j = 0; j < test.size(); j++) {
                    scatterEntries.add(new Entry(scatterVoltages.get(i).get(j), i + 1));
                }
            }
        }

        scatterData = new ScatterData(getTimeLabels(), scatterDataSet); // update scatter data
        scatterChart.setData(scatterData); // update scatter chart
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scatterChart.notifyDataSetChanged(); // notify chart that data has changed
                scatterChart.invalidate(); // refresh chart
            }
        });
    }

    // method to get time labels for scatter chart
    private ArrayList<String> getTimeLabels() {
        ArrayList<String> times = new ArrayList<>();
        for (int i = 0; i < scatterVoltages.size(); i++) {
            times.add(String.format("%02d:00", i)); // add hour label for each voltage level
        }
        return times;
    }


    public void openHome(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}