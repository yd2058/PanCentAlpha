package com.example.pancentalpha;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class GpsLoc extends AppCompatActivity {
    FusedLocationProviderClient fLC;
    Button start, end;
    TextView dis;
    Location loc1, loc2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.strtbtn);
        end = findViewById(R.id.endbtn);
        dis = findViewById(R.id.indTv);

        fLC = LocationServices.getFusedLocationProviderClient(this);
        checkAndRequestLocationPermissions(this, 100);

    }

    public void startLocation(View view) {
        end.setEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
        fLC.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(this, location -> {
            if (location != null) {
                loc1 = location;
                dis.setText(loc1.getLatitude() + "," + loc1.getLongitude());
            }
        });

    }

    public void endLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
        fLC.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(this, location -> {
            if (location != null) {
                loc2 = location;
                dis.setText(loc1.getLatitude() + "," + loc1.getLongitude()+ "\n" + loc2.getLatitude() + "," + loc2.getLongitude() +"\n"+ "Distance: " + loc1.distanceTo(loc2)+" m");
            }
        });

    }

    public static boolean checkAndRequestLocationPermissions(Activity activity, int requestCode) {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean allPermissionsGranted = true;
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (allPermissionsGranted) {
            // Permissions are granted
            return true;
        } else {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.genmenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        String temp = item.getTitle().toString();
        if(temp.equals("user regist")) startActivity(new Intent(this, LogAct.class));
        else if(temp.equals("gps dist")) startActivity(new Intent(this, GpsLoc.class));
        else if(temp.equals("gyro dist")) startActivity(new Intent(this, GyroLoc.class));
        else if(temp.equals("gyro ang")) startActivity(new Intent(this, GyroAng.class));
        else if(temp.equals("decimeter")) startActivity(new Intent(this, Decimet.class));

        return super.onOptionsItemSelected(item);
    }


}