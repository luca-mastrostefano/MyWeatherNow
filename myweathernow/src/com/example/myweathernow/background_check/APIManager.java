package com.example.myweathernow.background_check;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.example.myweathernow.persistency.WeatherInfo;
import com.example.myweathernow.persistency.UserID;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class APIManager {

    private static final String URL = "http://robertotucci.netsons.org/myweathernow/api/rain/get.php";
    public static enum InformationType{
        OVERVIEW,
        DETAILED;
    }
    public static enum WHEN{
        TODAY,
        TOMORROW;
    }
    private InformationType informationType;
    private WHEN when;

    public APIManager(InformationType informationType, WHEN when){
        this.informationType = informationType;
        this.when = when;
    }

    public WeatherInfo getWeatherInfo(Context context, Location location) throws Exception{
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
                final JSONObject jsonResponse = new JSONObject(responseString.trim());
                if(jsonResponse.getInt("status") == 200){
                    JSONObject data = jsonResponse.getJSONObject("data");
                    if(!userID.isValid()){
                        userID.storeUserID(data.getLong("id"));
                    }
                    return WeatherInfo.creteWeatherInfoFromJson(context, jsonResponse);
                }

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
        if(userID.isValid()) {
            params.setLongParameter("userid", userID.getUserID());
        }
        if(location != null) {
            params.setDoubleParameter("latitude", location.getLatitude());
            params.setDoubleParameter("longitude", location.getLongitude());
        }
        params.setParameter("type", this.informationType.toString());
        params.setParameter("when", this.when.toString());
        params.setLongParameter("date", System.currentTimeMillis());
        getRequest.setParams(params);
        getRequest.addHeader("Cache-Control", "no-cache");
        return getRequest;
    }

}
