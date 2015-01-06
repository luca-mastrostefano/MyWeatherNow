package com.example.myweathernow.background_check;

import android.content.*;
import android.location.*;
import com.example.myweathernow.persistency.*;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class LocationsDetector {
    private final TemporanyLocation tempLoc;

    public LocationsDetector(Context c) {
        this.tempLoc = new TemporanyLocation(c);
    }

    public boolean addLocationToHistory(Location currentLocation) {
        boolean showNotification = false;
        // first time
        if (!this.tempLoc.hasLocation()) {
            this.tempLoc.addLocation(currentLocation);
            showNotification = true;
        } else {
            Location oldLoc = this.tempLoc.getOldLocation();
            // due location sono diverse se distanti più di 200m
            if (currentLocation.distanceTo(oldLoc) >= 200) {
                // controllo quanto valeva l'ultimo ping
                int oldPing = this.tempLoc.getOldPing();
                if (oldPing >= 6) {
                    showNotification = false;
                }
                // salvo l'ultima come corrente
                this.tempLoc.addLocation(currentLocation);
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
