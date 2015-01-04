package com.example.myweathernow.background_check;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.myweathernow.Meteo;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class APIManager {

    private static final String preferencesName = "myweathernow_preferences";
    private static final String userID_KEY = "user_id";
    private static final String URL = "http://www.translated.net/luca_checker/check.php";
    private static final String USER_ID_KEY = "userid";
    private final SharedPreferences sharedPreferences;

    protected APIManager(final Context context) {
        this.sharedPreferences = context.getSharedPreferences(APIManager.preferencesName, 0);
    }

    public Meteo getMeteoInfo(){
        final HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        int numberOfSitesChanged = 0;
        try {
            final List<Site> activeSites = new LinkedList<Site>();
            // System.out.println("Total sites: " + Arrays.toString(sites));
            final JSONArray jsonSites = new JSONArray();
            for (final Site site : sites) {
                if (Site.RunningStatus.PLAY.equals(site.getRunningStatus())) {
                    activeSites.add(site);
                    jsonSites.put(this.siteToJson(site));
                    // System.out.println("Sending: " + site.getPageName());
                }
            }
            // System.out.println("Active ones: " + activeSites);
            if (!activeSites.isEmpty()) {
                final HttpPost postRequest = new HttpPost(MyTask.URL);
                final List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                final String UID = Long.toString(this.userId);
                params.add(new BasicNameValuePair(MyTask.USER_ID_KEY, UID));
                params.add(new BasicNameValuePair(MyTask.SITES_KEY, jsonSites.toString()));
                postRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                response = httpclient.execute(postRequest);
                final StatusLine statusLine = response.getStatusLine();
                // System.out.println("Sending request:");
                // System.out.println(params.toString());
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    final ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                    // System.out.println("response:|" + responseString.toString() + "|");
                    if (!"[]".equals(responseString)) {
                        final JSONObject jsonResponse = new JSONObject(responseString.trim());
                        // Refresh sites
                        final Date now = new Date();
                        final Iterator<?> iterator = jsonResponse.keys();
                        final Map<Long, Site> id2site = Site.getMap(activeSites);
                        while (iterator.hasNext()) {
                            final String site_id_str = (String) iterator.next();
                            final long site_id = Long.parseLong(site_id_str);
                            final Site site = id2site.get(site_id);
                            if (site != null) {
                                site.setLastCheck(now);
                                site.setServerSynch(true);
                                if (jsonResponse.getInt(site_id_str) == 1) {
                                    // System.out.println(site.getId() + "  " + site.getPageName() + " CHANGED!");
                                    numberOfSitesChanged++;
                                    site.setSiteStatus(SiteStatus.CHANGED);
                                    site.setRunningStatus(Site.RunningStatus.PAUSE);
                                } else {
                                    // System.out.println(site.getId() + "  " + site.getPageName() + " NOT CHANGED!");
                                    site.setSiteStatus(SiteStatus.NOT_CHANGED);
                                }
                                site.update();
                            }
                        }
                    }
                } else {
                    // Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } else {
                // System.out.println("No active sites.");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return numberOfSitesChanged;
    }

    private void storeUserID(long user_id){
        final SharedPreferences.Editor preferencesEditor = this.sharedPreferences.edit();
        preferencesEditor.putLong(APIManager.userID_KEY, user_id);
        preferencesEditor.commit();
    }

    private void getUserID(){
        long userID = this.sharedPreferences.getLong(APIManager.userID_KEY, -1);
        if(userID == -1){

        }
    }
}
