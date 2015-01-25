package com.example.myweathernow;

import android.content.Context;
import android.os.AsyncTask;
import com.example.myweathernow.background_check.APIManager;
import com.example.myweathernow.persistency.WeatherInfo;

/**
 * Created by lucamastrostefano on 06/01/15.
 */
public class RefreshWeatherInfo extends AsyncTask<Void, WeatherInfo, WeatherInfo> {

    private MWNhome homeActivity;

    public RefreshWeatherInfo(MWNhome homeActivity){
        this.homeActivity = homeActivity;
    }

    @Override
    protected WeatherInfo doInBackground(Void... voids) {
        APIManager apiManager = new APIManager(APIManager.InformationType.OVERVIEW, APIManager.WHEN.TODAY);
        try {
            return apiManager.getWeatherInfo(this.homeActivity, null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(WeatherInfo... values) {

    }

    @Override
    protected void onPostExecute(WeatherInfo result) {
        if(result != null){
            this.homeActivity.refreshUI(result);
        }
    }

}
