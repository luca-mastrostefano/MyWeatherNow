package com.example.myweathernow.background_check;

import android.content.*;
import android.location.*;
import com.example.myweathernow.persistency.*;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class LocationsDetector {
    private final UserLocation tempLoc;

    public LocationsDetector(Context c) {
        this.tempLoc = new UserLocation(c);
    }

    public boolean addLocationToHistory(Location currentLocation) {
        boolean showNotification = false;
        // first time
        if (!this.tempLoc.hasLocation()) {
            this.tempLoc.setLocation(currentLocation);
            showNotification = true;
        } else {
            Location oldLoc = this.tempLoc.getLastLocation();
            // due location sono diverse se distanti più di 200m
            if (currentLocation.distanceTo(oldLoc) >= 200) {
                // controllo quanto valeva l'ultimo ping
                int oldPing = this.tempLoc.getLastPing();
                if (oldPing >= 6) {
                    showNotification = false;
                }
                // salvo l'ultima come corrente
                this.tempLoc.setLocation(currentLocation);
            } else {
                // distanza < 200m, approssimiamo a stessa location
                // aggiorno quella di prima aumentando il ping
                int updatedPing = this.tempLoc.updateLocation();
                if (updatedPing >= 6) {
                    showNotification = true;
                    // TODO se ping >= 6 aggiungo al db come location importante
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
