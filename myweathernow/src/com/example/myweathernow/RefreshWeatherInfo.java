package com.example.myweathernow;

import android.content.Context;
import android.os.AsyncTask;
import com.example.myweathernow.background_check.APIManager;
import com.example.myweathernow.persistency.WeatherInfo;

/**
 * Created by lucamastrostefano on 06/01/15.
 */
public class RefreshWeatherInfo extends AsyncTask<Void, WeatherInfo, WeatherInfo> {

    private Context context;

    public RefreshWeatherInfo(Context context){
        this.context = context;
    }

    @Override
    protected WeatherInfo doInBackground(Void... voids) {
        APIManager apiManager = new APIManager();

        //apiManager.getWeatherInfo(context, location);
        return null;
    }

    @Override
    protected void onProgressUpdate(WeatherInfo... values) {

    }

    @Override
    protected void onPostExecute(WeatherInfo result) {

    }

}
