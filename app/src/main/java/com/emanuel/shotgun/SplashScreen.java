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

import com.emanuel.shotgun.utils.Trip;
import com.emanuel.shotgun.utils.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initializeDatabase();
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

    private void initializeDatabase(){
        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!db.userExists("danDub")){

            // Add default users
            ArrayList<User> users = createUsers();
            for(User user : users){
                db.addUser(user, "asdf");
            }
        }

        if(!db.tripExists("San Diego")){
             // Add default trips
        }

        db.close();
    }

    private ArrayList<User> createUsers(){
        ArrayList<User> users = new ArrayList<>();

        User daniel = new User();
        daniel.username = "danDub";
        daniel.firstName = "Daniel";
        daniel.lastName = "Worthington";
        daniel.carMPG = 28;
        daniel.carOccupancy = 5;

        User emanuel = new User();
        emanuel.username = "eMan";
        emanuel.firstName = "Emanuel";
        emanuel.lastName = "Macias";
        emanuel.carMPG = 25;
        emanuel.carOccupancy = 4;

        User caitlin = new User();
        caitlin.username = "cMills";
        caitlin.firstName = "Caitlin";
        caitlin.lastName = "Mills";
        caitlin.carMPG = 30;
        caitlin.carOccupancy = 2;

        User sydney  = new User();
        sydney.username = "sSmith";
        sydney.firstName = "Sydney";
        sydney.lastName = "Smith";
        sydney.carMPG = 22;
        sydney.carOccupancy = 3;

        User korey = new User();
        korey.username = "kBaines";
        korey.firstName = "Korey";
        korey.lastName = "Baines";
        korey.carMPG = 35;
        korey.carOccupancy = 6;

        users.add(daniel);
        users.add(emanuel);
        users.add(caitlin);
        users.add(sydney);
        users.add(korey);

        return users;
    }

    private ArrayList<Trip> createTrips(){

        ArrayList<Trip> trips = new ArrayList<>();

        Trip trip = new Trip();
        trip.name = "San Diego";
        trip.location = "San Diego, CA";
        trip.description = "I'm heading to San Diego this weekend. Anyone need a ride?";
        trip.setDepartDateTime(Calendar.getInstance().getTimeInMillis());
        trip.setReturnDateTime(Calendar.getInstance().getTimeInMillis() + CreateTripActivity.DAY_IN_MILLIS);

        return trips;
    }
}

























