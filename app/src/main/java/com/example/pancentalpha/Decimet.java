package com.example.pancentalpha;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Decimet extends AppCompatActivity {
    private static Decimet ins;
    TextView dBtv;
    LiveDbMeter liveDbMeter;
    EditText freqet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decimet);

        ins = this;
        dBtv = findViewById(R.id.dBtv);
        liveDbMeter = new LiveDbMeter(this);
        freqet = findViewById(R.id.freqet);
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

    public void startrec(View view) {
        liveDbMeter.start(Double.parseDouble(freqet.getText().toString()));
    }
    public static Decimet getInstance() {return ins;}

    public void updateDB(double db) {
        Decimet.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dBtv.setText(String.valueOf(db)+" dB");
            }
        });
    }

    public void stoprec(View view) {
        liveDbMeter.stop();
    }
}