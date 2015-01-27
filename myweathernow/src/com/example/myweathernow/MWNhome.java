package com.example.myweathernow;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.example.myweathernow.background_check.*;
import com.example.myweathernow.background_check.service.*;
import com.example.myweathernow.persistency.*;
import com.example.myweathernow.util.*;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.*;
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
            Log.e("MWNhome","Start service for test!");
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
        LineChart chartProbabilities = (LineChart) findViewById(R.id.chartProbabilities);
        ChartUtil.initializeTransparentGraph(chartProbabilities);
        LineChart chartIntensities = (LineChart) findViewById(R.id.chartIntensities);
        ChartUtil.initializeTransparentGraph(chartIntensities);
        Log.d("MWNhome" , "calling external service to refresh UI");
        RefreshWeatherInfo refreshWeatherInfo = new RefreshWeatherInfo(this);
        refreshWeatherInfo.execute();
    }

    public void refreshUI(WeatherManager weatherManager){
        Log.i("MWNhome", weatherManager.toString());
        Log.i("MWNhome", "Day: " + this.day);
        ((TextView) this.findViewById(R.id.city)).setText("Roma");
        ((TextView) this.findViewById(R.id.date)).setText(dateFormatter.format(weatherManager.getDate()));
        List<WeatherInfo> details = weatherManager.getDetails(this.day);

        LineChart chartProbabilities = (LineChart) findViewById(R.id.chartProbabilities);
        ChartUtil.fill(chartProbabilities, details, true);
        LineChart chartIntensities = (LineChart) findViewById(R.id.chartIntensities);
        ChartUtil.fill(chartIntensities, details, false);
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
            Log.i("MWNhome", "è la prima volta che parte l'applicazione");
        }else{
            Log.i("MWNhome", "applicazione già avviata in passato");
        }
        return isFirstStart;
    }

    public void showTodayInfo(View view) {
        this.handleButtonClick(APIManager.Day.TODAY);
    }

    public void showTomorrowInfo(View view) {
        this.handleButtonClick(APIManager.Day.TOMORROW);
    }

    private void handleButtonClick(APIManager.Day day){
        Button today = (Button)findViewById(R.id.button_today);
        Button tomorrow = (Button)findViewById(R.id.button_tomorrow);
        Button clickedButton;
        Button otherButton;
        if(APIManager.Day.TODAY.equals(day)){
            clickedButton = today;
            otherButton = tomorrow;
        }else{
            clickedButton = tomorrow;
            otherButton = today;
        }
        clickedButton.setBackgroundColor(Color.TRANSPARENT);
        clickedButton.setTextColor(Color.parseColor("#ffffff"));
        otherButton.setBackgroundColor(Color.parseColor("#80FFFFFF"));
        otherButton.setTextColor(Color.parseColor("#ff0c2c46"));
        this.day = day;
        try {
            WeatherManager weatherManager = WeatherManager.getLast(this.getApplicationContext());
            if(weatherManager != null) {
                this.refreshUI(weatherManager);
            }
        } catch (JSONException e) {
            //Cant get weatherInfo
        }
    }
}

