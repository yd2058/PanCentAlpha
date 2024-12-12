package com.example.pancentalpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GyroAng extends AppCompatActivity {
    SensorFusionHelper sFang;
    TextView angTv;
    float[] pos1 = new float[3], pos2 = new float[3],pos3 = new float[3], postemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_ang);

        angTv = findViewById(R.id.angTv);
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

    public void frloc(View view) {
        sFang = new SensorFusionHelper(this);
        sFang.start();
        postemp = sFang.getCurrentPosition();
        pos1[0] = postemp[0];
        pos1[1] = postemp[1];
        pos1[2] = postemp[2];
    }

    public void secLoc(View view) {
        postemp = sFang.getCurrentPosition();
        pos2[0] = postemp[0];
        pos2[1] = postemp[1];
        pos2[2] = postemp[2];
    }

    public void thiLoc(View view) {
        postemp = sFang.getCurrentPosition();
        pos3[0] = postemp[0];
        pos3[1] = postemp[1];
        pos3[2] = postemp[2];
        angTv.setText("angle: "+sFang.angleBetweenCoordinates(pos1,pos2,pos3));
        sFang.stop();

    }
}