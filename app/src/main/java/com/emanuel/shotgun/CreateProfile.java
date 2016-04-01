package com.emanuel.shotgun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.emanuel.shotgun.utils.User;

import java.sql.SQLException;

public class CreateProfile extends AppCompatActivity {

    public static final String USERNAME_KEY = "UsernameKey";
    public static final String PASSWORD_KEY = "PasswordKey";

    private String username;
    private String password;

    private EditText firstNameView;
    private EditText lastNameView;
    private EditText carCapacityView;
    private EditText carMPGView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        // Get username and password from previous screen
        Intent intent = getIntent();
        username = intent.getStringExtra(USERNAME_KEY);
        password = intent.getStringExtra(PASSWORD_KEY);

        // Set up UI Elements
        firstNameView = (EditText) findViewById(R.id.firstName);
        lastNameView = (EditText) findViewById(R.id.lastName);
        carCapacityView = (EditText) findViewById(R.id.carCapacity);
        carMPGView = (EditText) findViewById(R.id.carMPG);
    }

    public void createProfile(View view){
        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        int carCapacity = Integer.parseInt(carCapacityView.getText().toString());
        int carMPG = Integer.parseInt(carMPGView.getText().toString());

        if(!isNameValid(firstName)){
            Toast.makeText(CreateProfile.this, "First name is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isNameValid(lastName)){
            Toast.makeText(CreateProfile.this, "Last name is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isCarCapacityValid(carCapacity)){
            Toast.makeText(CreateProfile.this, "Car capacity is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isCarMPGValid(carMPG)){
            Toast.makeText(CreateProfile.this, "Car MPG is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.username = username;
        user.firstName = firstName;
        user.lastName = lastName;
        user.carOccupancy = carCapacity;
        user.carMPG = carMPG;

        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.addUser(user, password);
        db.close();

        // SAVE THE CURRENT USER AS THIS USER
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs),0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.username_key), username);
        editor.commit();

        Intent intent = new Intent(this, TripFeedActivity.class);
        startActivity(intent);
    }

    private boolean isNameValid(String name){
        // TODO: IMPLEMENT
        return !name.equals("");
    }

    private boolean isCarCapacityValid(int capacity){
        // TODO: IMPLEMENT
        return capacity >= 0;
    }

    private boolean isCarMPGValid(int mpg){
        // TODO: IMPLEMENT
        return mpg >= 0;
    }

}
