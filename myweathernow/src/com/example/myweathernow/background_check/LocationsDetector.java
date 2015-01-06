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
        // first time
        if (!this.tempLoc.hasLocation()) {
            this.tempLoc.addLocation(currentLocation);
            // TODO se è la prima volta mostro subito la notifica ? e se si sta spostando?
            return true;
        } else {
            Location oldLoc = this.tempLoc.getOldLocation();
            // due location sono diverse se distanti più di 200m
            if (currentLocation.distanceTo(oldLoc) >= 200) {
                // controllo quanto valeva l'ultimo ping
                int oldPing = this.tempLoc.getOldPing();
                if (oldPing >= 6) {
                    // TODO leva la notifica
                    return false;
                }
                // salvo l'ultima come corrente
                this.tempLoc.addLocation(currentLocation);

            } else {
                // distanza < 200m, approssimiamo a stessa location
                // aggiorno quella di prima aumentando il ping
                int updatedPing = this.tempLoc.updateLocation();
                if (updatedPing >= 6) {
                    //TODO mostro la notifica
                    return true;
                }
            }

        }
        return false;
    }

    public boolean isHome(Location currentLocation){
        return true;
    }

    public boolean isResting(Location currentLocation){
        return false;
    }
}
