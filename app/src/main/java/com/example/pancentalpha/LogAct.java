package com.example.pancentalpha;

import static com.example.pancentalpha.FBHelper.fBAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LogAct extends AppCompatActivity {
    EditText emet, pset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        emet = findViewById(R.id.emet);
        pset = findViewById(R.id.pset);

    }

    public void reg(View view) {
        String email = emet.getText().toString();
        String pass = pset.getText().toString();
        if (email.isEmpty() || pass.isEmpty()) Toast.makeText(this, "Data Missing", Toast.LENGTH_SHORT).show();
        else{
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Connecting");
            pd.setMessage("Creating User...");
            pd.show();
            fBAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if(task.isSuccessful()){
                        Log.i("FB", "Register Success");
                        FirebaseUser user = fBAuth.getCurrentUser();
                        Toast.makeText(LogAct.this, "Successfully registered user"+user.getUid(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Exception e = task.getException();
                        if(e instanceof FirebaseAuthInvalidUserException) Toast.makeText(LogAct.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        else if(e instanceof FirebaseAuthWeakPasswordException) Toast.makeText(LogAct.this, "Weak Password", Toast.LENGTH_SHORT).show();
                        else if(e instanceof FirebaseAuthUserCollisionException) Toast.makeText(LogAct.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                        else if(e instanceof FirebaseAuthInvalidCredentialsException) Toast.makeText(LogAct.this, "Gnenral Auth failure", Toast.LENGTH_SHORT).show();
                        else if(e instanceof FirebaseNetworkException) Toast.makeText(LogAct.this, "Network Error", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(LogAct.this, "Unknown Error. Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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