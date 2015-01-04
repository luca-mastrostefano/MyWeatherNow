package com.example.myweathernow.background_check;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import com.example.myweathernow.MeteoInfo;
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

    private static final String preferencesName = "myweathernow_preferences";
    private static final String userID_KEY = "user_id";
    private static final String URL = "http://www.translated.net/luca_checker/check.php";
    private final SharedPreferences sharedPreferences;

    protected APIManager(final Context context) {
        this.sharedPreferences = context.getSharedPreferences(APIManager.preferencesName, 0);
    }

    public MeteoInfo getMeteoInfo(Location location) throws Exception{
        final HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        int numberOfSitesChanged = 0;
        MeteoInfo meteoInfo = null;
        try {
            // System.out.println("Total sites: " + Arrays.toString(sites));
            final HttpGet getRequest = new HttpGet(APIManager.URL);
            //final List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            final long UID = this.getUserID();
            HttpParams params = new BasicHttpParams();
            params.setLongParameter("userid", UID);
            params.setDoubleParameter("latitude", location.getLatitude());
            params.setDoubleParameter("longitude", location.getLongitude());
            getRequest.setParams(params);
            response = httpclient.execute(getRequest);
            final StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                final JSONObject jsonResponse = new JSONObject(responseString.trim());
                if(jsonResponse.getInt("status") == 200){
                    final Date now = new Date();
                    meteoInfo = new MeteoInfo();
                    JSONObject data = jsonResponse.getJSONObject("data");
                    if(UID <= 0){
                        this.storeUserID(data.getLong("id"));
                    }
                }

            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw new Exception("Can't perform API call");
        }
        return null;
    }

    private void storeUserID(long user_id) {
        final SharedPreferences.Editor preferencesEditor = this.sharedPreferences.edit();
        preferencesEditor.putLong(APIManager.userID_KEY, user_id);
        preferencesEditor.commit();
    }

    private long getUserID() {
        return this.sharedPreferences.getLong(APIManager.userID_KEY, -1);
    }
}
