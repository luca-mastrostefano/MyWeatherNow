package com.example.myweathernow.background_check;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import com.example.myweathernow.MeteoInfo;
import com.example.myweathernow.persistency.UserID;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class APIManager {

    private static final String URL = "http://www.translated.net/luca_checker/check.php";

    public MeteoInfo getMeteoInfo(Context context, Location location) throws Exception{
        MeteoInfo meteoInfo = null;
        try {
            final HttpGet getRequest = new HttpGet(APIManager.URL);
            HttpParams params = new BasicHttpParams();
            UserID userID = UserID.getInstance(context);
            if(userID.isValid()) {
                params.setLongParameter("userid", userID.getUserID());
            }
            params.setDoubleParameter("latitude", location.getLatitude());
            params.setDoubleParameter("longitude", location.getLongitude());
            getRequest.setParams(params);
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
                    final Date now = new Date();
                    meteoInfo = new MeteoInfo();
                    JSONObject data = jsonResponse.getJSONObject("data");
                    if(!userID.isValid()){
                        userID.storeUserID(data.getLong("id"));
                    }
                }

            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw new Exception("Can't perform API call");
        }
        return null;
    }

}
