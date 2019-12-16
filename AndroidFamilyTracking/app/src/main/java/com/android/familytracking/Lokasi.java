package com.android.familytracking;

import java.util.Date;

/**
 * Created by PC13 on 9/9/2017.
 */

public class Lokasi {

    private double longitude, latitude;

    public Lokasi(){
        latitude = 0;
        longitude = 0;
    }

    public Lokasi(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
