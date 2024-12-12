package com.example.pancentalpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class GyroLoc extends AppCompatActivity {
    SensorFusionHelper sF;
    Button end;
    TextView indTv;
    float[] pos1 = new float[3], pos2 = new float[3], postemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_loc);


        end = findViewById(R.id.endbtng);
        indTv = findViewById(R.id.indTvg);


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


    public void startLoc(View view)  {
        sF = new SensorFusionHelper(this);
        sF.start();
        postemp = sF.getCurrentPosition();
        pos1[0] = postemp[0];
        pos1[1] = postemp[1];
        pos1[2] = postemp[2];
        indTv.setText("X1: "+pos1[0]+", Y1: "+pos1[1]+", Z: "+pos1[2]);
        end.setEnabled(true);
    }

    public void endLoc(View view) {
        postemp = sF.getCurrentPosition();
        pos2[0] = postemp[0];
        pos2[1] = postemp[1];
        pos2[2] = postemp[2];
        indTv.setText("X1: "+pos1[0]+", Y1: "+pos1[1]+"Z1: "+pos1[2]+"\n X2: "+pos2[0]+", Y2: "+pos2[1]+"Z2: "+pos2[2]+"\n Distance: "+sF.distanceBetweenCoordinates(pos1,pos2)+"m");
        sF.stop();
    }



}