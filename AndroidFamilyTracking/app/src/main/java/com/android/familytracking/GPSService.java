package com.android.familytracking;

import android.*;
import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by PC13 on 9/9/2017.
 */

public class GPSService extends Service {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static final String TAG = "GPSService";
    private String MemberId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        DBHandler SQLite = new DBHandler(this);
        MemberId = SQLite.getMember();
        final DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Location").child(MemberId);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Lokasi lokasi = new Lokasi();
                lokasi.setLatitude(location.getLatitude());
                lokasi.setLongitude(location.getLongitude());
                Intent i = new Intent("location_update");
                i.putExtra("Longitude", location.getLongitude());
                i.putExtra("Latitude", location.getLatitude());
                Reference.setValue(lokasi);
                Log.e(TAG, "Latitude = " + location.getLatitude() + "");
                Log.e(TAG, "Longitude = " + location.getLongitude() + "");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.e(TAG, "onStatusChanged: " + s);
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.e(TAG, "onProviderEnabled: " + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.e(TAG, "onProviderDisabled: " + s);
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //noinspection MissingPermission
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            //noinspection MissingPermission
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
