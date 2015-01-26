package com.example.myweathernow;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.example.myweathernow.background_check.*;
import com.example.myweathernow.background_check.service.*;
import com.example.myweathernow.persistency.*;
import com.example.myweathernow.util.*;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.json.*;

import java.text.*;
import java.util.*;

public class MWNhome extends Activity {

    private static final DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM");
    private APIManager.Day day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prova);
        Log.i("MWNhome", "Entrato nella home");
        Log.i("Thread Home", ""  + Thread.currentThread().getId());
        this.day = APIManager.Day.TODAY;
        if(this.isFirstStart()){
            Log.e("MWNhome","Service not started, let's start it now!");
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        }else{
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        }
        try {
            Log.d("MWNhome" , "getting cached weatherManager");
            WeatherManager weatherManager = WeatherManager.getLast(this.getApplicationContext());
            if(weatherManager != null) {
                Log.d("MWNhome" , "calling refreshUI");
                this.refreshUI(weatherManager);
            }
        } catch (JSONException e) {
            //Cant get weatherInfo
        }
        Log.d("MWNhome" , "calling external service to refresh UI");
        RefreshWeatherInfo refreshWeatherInfo = new RefreshWeatherInfo(this);
        refreshWeatherInfo.execute();
    }

    public void refreshUI(WeatherManager weatherManager){
        Log.i("MWNhome", weatherManager.toString());
        ((TextView) this.findViewById(R.id.city)).setText("Roma");
        ((TextView) this.findViewById(R.id.date)).setText(dateFormatter.format(weatherManager.getDate()));
        List<WeatherInfo> details = weatherManager.getDetails(this.day);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.animateXY(1500,1500);
        Log.d("MWNhome-refreshUI" , "iterating " + details.size() + " weatherInfo");
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < details.size(); i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < details.size(); i++) {
            WeatherInfo weatherInfo = details.get(i);
            yVals.add(new Entry((float)weatherInfo.getRainProbability(), i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        //TODO bla bla bla with details
    }

    private boolean isFirstStart(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstStart = "first_start";
        boolean isFirstStart = sharedPreferences.getBoolean(firstStart, true);
//        Log.e("MWNhome","SharedPreferences firstStart disabled!");
//        if(isFirstStart && false){
        if(isFirstStart){
            final SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.putBoolean(firstStart, false);
            preferencesEditor.commit();
        }
        if(isFirstStart) {
            Log.i("MWNhome", "firstStart");
        }else{
            Log.i("MWNhome", "già partito");
        }
        return isFirstStart;
    }

    public void showTodayInfo(View view) {
        //TODO mostrare le informazioni di oggi
//        view.findViewById(R.id.button_today).setBackgroundColor();
    }

    public void showTomorrowInfo(View view) {
        //TODO mostrare le informazioni di domani
    }
}

