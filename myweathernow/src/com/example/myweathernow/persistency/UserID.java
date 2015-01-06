package com.example.myweathernow.persistency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lucamastrostefano on 05/01/15.
 */
public class UserID {

    private static UserID instance = null;
    private final SharedPreferences sharedPreferences;
    private static final String preferencesName = "myweathernow_preferences";
    private static final String userID_KEY = "user_id";
    private long userID;

    private UserID(final Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.userID = this.getUserID();
    }

    public static UserID getInstance(Context context){
        if(instance == null){
            instance = new UserID(context);
        }
        return instance;
    }

    public void storeUserID(long userID) {
        this.userID = userID;
        final SharedPreferences.Editor preferencesEditor = this.sharedPreferences.edit();
        preferencesEditor.putLong(UserID.userID_KEY, userID);
        preferencesEditor.commit();
    }

    public long getUserID() {
        this.userID = this.sharedPreferences.getLong(UserID.userID_KEY, -1);
        return this.userID;
    }

    public boolean isValid(){
        return this.userID > 0;
    }
}
