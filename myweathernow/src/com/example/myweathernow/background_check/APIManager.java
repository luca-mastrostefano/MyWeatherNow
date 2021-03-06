package com.example.myweathernow.background_check;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.example.myweathernow.persistency.WeatherManager;
import com.example.myweathernow.persistency.UserID;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class APIManager {

    private static final String URL = "http://topbestever.com/myweathernow/api/rain/get.php?type=detailed";
    public static enum InformationType{
        OVERVIEW,
        DETAILED;
    }
    public static enum Day {
        TODAY,
        TOMORROW, Day;
    }
    private InformationType informationType;
    private Day day;

    public APIManager(InformationType informationType, Day day){
        this.informationType = informationType;
        this.day = day;
    }

    public WeatherManager getWeatherInfo(Context context, Location location) throws Exception{
        Log.d("APIManager", "start");
        try {
            UserID userID = UserID.getInstance(context);
            final HttpGet getRequest = this.createGetRequest(userID, location);
            final HttpClient httpclient = new DefaultHttpClient();
            Log.d("APIManager", "httpSent");
            HttpResponse response = httpclient.execute(getRequest);
            final StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                Log.d("APIManager", "httpResponse arrived");
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                String responseString = out.toString();
                Log.d("APIManager", responseString);
                final JSONObject jsonResponse = new JSONObject(responseString.trim());
                Log.d("APIManager", jsonResponse.toString());
                return WeatherManager.creteWeatherManagerFromJson(context, jsonResponse);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        Log.d("APIManager", "can't perform API call");
        throw new Exception("Can't perform API call");
    }

    private HttpGet createGetRequest(UserID userID, Location location){
        final HttpGet getRequest = new HttpGet(APIManager.URL);
        HttpParams params = new BasicHttpParams();
        if(location != null) {
            params.setDoubleParameter("latitude", location.getLatitude());
            params.setDoubleParameter("longitude", location.getLongitude());
        }
        params.setParameter("type", this.informationType.toString());
        //params.setParameter("when", this.day.toString());
        params.setLongParameter("date", System.currentTimeMillis());
        //getRequest.setParams(params);
        getRequest.addHeader("Cache-Control", "no-cache");
        return getRequest;
    }

}
