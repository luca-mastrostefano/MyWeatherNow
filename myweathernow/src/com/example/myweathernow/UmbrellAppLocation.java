package com.example.myweathernow;

import android.location.*;

import java.util.*;

/**
 * Created by ele on 05/01/15.
 */
public class UmbrellAppLocation {
    private int id;
    private Location location;
    private int ping;

    public UmbrellAppLocation(){
    }

    public UmbrellAppLocation(int id, Location l, int p){
        this.id = id;
        this.location = l;
        this.ping = p;
    }

    public int incrementPing(int add){
        this.ping += add;
        return this.ping;
    }

    public Date getTimestamp(){
        return new Date(this.location.getTime());
    }

    public Location getLocation() {
        return location;
    }

    public int getPing() {
        return ping;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
