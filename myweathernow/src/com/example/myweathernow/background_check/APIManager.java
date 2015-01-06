package com.example.myweathernow.background_check;

import android.content.Context;
import android.location.Location;
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

    private static final String URL = "http://www.translated.net/luca_checker/check.php";

    public WeatherInfo getWeatherInfo(Context context, Location location) throws Exception{
        try {
            UserID userID = UserID.getInstance(context);
            final HttpGet getRequest = this.createGetRequest(userID, location);
            final HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(getRequest);
            final StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
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
            throw new Exception("Can't perform API call");
        }
        throw new Exception("Can't perform API call");
    }

    private HttpGet createGetRequest(UserID userID, Location location){
        final HttpGet getRequest = new HttpGet(APIManager.URL);
        HttpParams params = new BasicHttpParams();
        if(userID.isValid()) {
            params.setLongParameter("userid", userID.getUserID());
        }
        params.setDoubleParameter("latitude", location.getLatitude());
        params.setDoubleParameter("longitude", location.getLongitude());
        getRequest.setParams(params);
        return getRequest;
    }

}
