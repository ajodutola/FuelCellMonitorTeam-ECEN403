//package com.example.myfirstapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.github.mikephil.charting.charts.BarChart;
////import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class HistoricalTrends extends AppCompatActivity {
//
//    BarChart barChart;
//    ArrayList<String> dates = new ArrayList<>();
//    Random random;
//    ArrayList<BarEntry> barEntries;
//    private Button returnHome;
//    private EditText startDate;
//    private EditText endDate;
//    String startValue;
//    String endValue;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_historical_trends);
//
//        startDate = (EditText) findViewById(R.id.startDate1);
//        endDate = (EditText) findViewById(R.id.endDate1);
//        //startValue = startDate.getText().toString();
//        //endValue = endDate.getText().toString();
//
//        returnHome = (Button) findViewById(R.id.returnHomeFromStatus);
//        returnHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openHome();
//            }
//        });
//
//        barChart = (BarChart) findViewById(R.id.historical_data);
//
//        createDailyBarGraph("2022/05/05", "2022/06/01");
//    }
//
//    public void createDailyBarGraph(String Date1, String Date2) {
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//
//        try{
//            Date date1 = simpleDateFormat.parse(Date1);
//            Date date2 = simpleDateFormat.parse(Date2);
//
//            Calendar mDate1 = Calendar.getInstance();
//            Calendar mDate2 = Calendar.getInstance();
//            mDate1.clear();
//            mDate2.clear();
//
//            mDate1.setTime(date1);
//            mDate2.setTime(date2);
//
//            dates = new ArrayList<>();
//            dates = getList(mDate1, mDate2);
//
//            barEntries = new ArrayList<>();
//            float max = 0f;
//            float value = 0f;
//            random = new Random();
//            for (int j = 0; j < dates.size(); j++){
//                max = 2f;
//                value = random.nextFloat() * max;
//                barEntries.add(new BarEntry(value, j));
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
//        BarData barData = new BarData(dates, barDataSet);
//        barChart.setData(barData);
//        barChart.setDescription("Historical Trends of Fuel Cells");
//
//        barChart.setTouchEnabled(true);
//        barChart.setDragEnabled(true);
//        barChart.setScaleEnabled(true);
//    }
//
//    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
//        ArrayList<String> list = new ArrayList<>();
//        while (startDate.compareTo(endDate) <= 0) {
//            list.add(getDate(startDate));
//            startDate.add(Calendar.DAY_OF_MONTH, 1);
//        }
//        return list;
//    }
//
//    public String getDate(Calendar cld){
//        String currDate = cld.get(Calendar.YEAR) + "/" + cld.get(Calendar.MONTH + 1) + "/"
//                + cld.get(Calendar.DAY_OF_MONTH);
//        try {
//            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(currDate);
//            currDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return currDate;
//    }
//
//    public void openHome(){
//        Intent intent = new Intent(this, Dashboard.class);
//        startActivity(intent);
//    }
//}