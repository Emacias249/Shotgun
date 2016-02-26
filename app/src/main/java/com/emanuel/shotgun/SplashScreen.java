package com.emanuel.shotgun;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    public void btnClick(View v){

        Intent intent;

        switch(v.getId()){  // determine which button was pressed
            case R.id.btn_sign_in:
                intent = new Intent(this, LoginActivity.class); // navigate to LoginActivity
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                intent = new Intent(this, SignUpActivity.class); // navigate to SignUpActivity
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
