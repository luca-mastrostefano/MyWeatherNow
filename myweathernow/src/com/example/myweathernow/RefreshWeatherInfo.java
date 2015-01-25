package com.example.myweathernow;

import android.os.AsyncTask;
import com.example.myweathernow.background_check.APIManager;
import com.example.myweathernow.persistency.WeatherManager;

/**
 * Created by lucamastrostefano on 06/01/15.
 */
public class RefreshWeatherInfo extends AsyncTask<Void, WeatherManager, WeatherManager> {

    private MWNhome homeActivity;

    public RefreshWeatherInfo(MWNhome homeActivity){
        this.homeActivity = homeActivity;
    }

    @Override
    protected WeatherManager doInBackground(Void... voids) {
        APIManager apiManager = new APIManager(APIManager.InformationType.DETAILED, APIManager.Day.TODAY);
        try {
            return apiManager.getWeatherInfo(this.homeActivity, null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(WeatherManager... values) {

    }

    @Override
    protected void onPostExecute(WeatherManager result) {
        if(result != null){
            this.homeActivity.refreshUI(result);
        }
    }

}
