package com.example.pancentalpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Decimet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decimet);
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