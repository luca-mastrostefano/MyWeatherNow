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
        //Set city and daye
        ((TextView) this.findViewById(R.id.city)).setText("Roma");
        ((TextView) this.findViewById(R.id.date)).setText(this.normalizeDate(dateFormatter.format(weatherManager.getDate())));

        //Set daily info
        double daily_intensity = weatherManager.getOverview(this.day).get(WeatherInfo.Period.DAILY).getRainIntensity();
        ((TextView) this.findViewById(R.id.intensity_today_value)).setText(daily_intensity + "");
        double daily_probability = weatherManager.getOverview(this.day).get(WeatherInfo.Period.DAILY).getRainProbability();
        ((TextView) this.findViewById(R.id.probability_today_value)).setText(daily_probability + "");

        //Set sentence and image of the day
        Map.Entry<PhraseMaker.Slot, String> suggestion = PhraseMaker.getPhrase(weatherManager, this.day);
        PhraseMaker.Slot phase = suggestion.getKey();
        if(phase == PhraseMaker.Slot.NO_RAIN || phase == PhraseMaker.Slot.VERY_LOW){
            ((ImageView) this.findViewById(R.id.umbrella_suggestion)).setImageResource(R.drawable.closed_umbrella_color);
        }else{
            ((ImageView) this.findViewById(R.id.umbrella_suggestion)).setImageResource(R.drawable.open_umbrella_color);
        }
        ((TextView) this.findViewById(R.id.rain_suggestion)).setText(suggestion.getValue());

        //Refresh charts
        List<WeatherInfo> details = weatherManager.getDetails(this.day);
        LineChart chartProbabilities = (LineChart) findViewById(R.id.chartProbabilities);
        ChartUtil.fill(chartProbabilities, details, true);
        LineChart chartIntensities = (LineChart) findViewById(R.id.chartIntensities);
        ChartUtil.fill(chartIntensities, details, false);
        //TODO bla bla bla with details
    }

    private String normalizeDate(String date){
        StringBuilder b = new StringBuilder(date);
        int i = 0;
        do {
            b.replace(i, i + 1, b.substring(i,i + 1).toUpperCase());
            i =  b.indexOf(" ", i) + 1;
        } while (i > 0 && i < b.length());
        return b.toString();
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

