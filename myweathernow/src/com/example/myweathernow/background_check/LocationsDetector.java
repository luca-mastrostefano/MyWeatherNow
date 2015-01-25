package com.example.myweathernow.background_check;

import android.content.*;
import android.location.*;
import android.util.*;
import com.example.myweathernow.persistency.*;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class LocationsDetector {

    private final UserLocationHandler userLocationHandler;
    public static final int DISTANCE_THREASHOLD = 250;

    public LocationsDetector(Context c) {
        this.userLocationHandler = new UserLocationHandler(c);
    }

    public boolean addLocationToHistory(Location currentLocation) {
        boolean showNotification = false;
        // first time - se non ha la location è perchè ho appena isntallato l'app
        if (!this.userLocationHandler.hasLocation()) {
            this.userLocationHandler.setLocation(currentLocation);
            showNotification = true;
        } else if(this.userLocationHandler.isKnownLocation(currentLocation)){
            showNotification = true;
        }else{
            Location oldLocation = this.userLocationHandler.getLastLocation();
            // due location sono diverse se distanti più di 250m
            if (currentLocation.distanceTo(oldLocation) >= DISTANCE_THREASHOLD) {
                Log.i("si è spostato di", ""+currentLocation.distanceTo(oldLocation));
                Log.i("prima location", "" + oldLocation.getLatitude() + " " + oldLocation.getLongitude());
                Log.i("seconda location", "" + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                showNotification = false;
                // salvo l'ultima come corrente
                this.userLocationHandler.setLocation(currentLocation);
            } else {
                // distanza < 200m, approssimiamo a stessa location
                // aggiorno quella di prima aumentando il ping
                int updatedPing = this.userLocationHandler.increasePing();
                Log.i("è fermo da ", ""+updatedPing);
                if (updatedPing >= this.userLocationHandler.PING_THREASHOLD) {
                    showNotification = true;
                    this.userLocationHandler.storeKnownLocation(currentLocation);
                    // TODO se ping >= 3 aggiungo al db come location importante
                }else{
                    showNotification = false;
                }
            }

        }
        return showNotification;
    }

    //TODO controllare il cambioo di location, se quando cambia va in una location conosciuta
    //mostrola notifica (tipo quando entro a casa)

    public boolean isHome(Location currentLocation) {
        return true;
    }

    public boolean isResting(Location currentLocation) {
        return false;
    }
}
