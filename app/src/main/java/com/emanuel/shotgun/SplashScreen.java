package com.emanuel.shotgun;

import android.content.Intent;
import android.content.SharedPreferences;
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

        // Only doing this for demo
        initializeDatabase();

        //Set logged in user
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs),0);
        String username = sharedPref.getString(getString(R.string.username_key),getString(R.string.guest));

        if(!username.equals("")){
            Intent intent = new Intent(this,TripFeedActivity.class);
            startActivity(intent);
        }
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

            // SAVE THE CURRENT USER AS NO ONE
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs),0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.username_key), "");
            editor.commit();

            // Add default users
            ArrayList<User> users = createUsers();
            for(User user : users){
                db.addUser(user, "asdf");
            }
        }

        if(!db.tripExists("San Diego")){

            // Add default trips
            ArrayList<Trip> trips = createTrips(db);
            for(Trip trip : trips){
                db.addTrip(trip);
            }
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

    private ArrayList<Trip> createTrips(DBHelper db ){

        ArrayList<Trip> trips = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(2016,4,28,3,0,0);
        long startDate = c.getTimeInMillis();

        int danielId = db.getUser("danDub").id;
        int koreyId = db.getUser("kBaines").id;
        int emanId = db.getUser("eMan").id;
        int caitlinId = db.getUser("cMills").id;
        int sydId = db.getUser("sSmith").id;

        Trip sd = new Trip();
        startDate += (3 * CreateTripActivity.DAY_IN_MILLIS);
        sd.name = "San Diego";
        sd.location = "San Diego, CA";
        sd.description = "I'm heading to San Diego this weekend. Anyone need a ride?";
        sd.setDepartDateTime(startDate);
        sd.setReturnDateTime(startDate + (3 * CreateTripActivity.DAY_IN_MILLIS));
        sd.creatorId = danielId;
        trips.add(sd);

        Trip sm = new Trip();
        startDate += (6 * CreateTripActivity.DAY_IN_MILLIS);
        sm.name = "Santa Monica";
        sm.location = "Santa Monica, CA";
        sm.description = "Anyone down for a stroll on the pier?";
        sm.setDepartDateTime(startDate);
        sm.setReturnDateTime(startDate + CreateTripActivity.DAY_IN_MILLIS);
        sm.creatorId = caitlinId;
        trips.add(sm);

        Trip corona = new Trip();
        startDate -= (11 * CreateTripActivity.DAY_IN_MILLIS);
        corona.name = "Corona";
        corona.location = "Corona, CA";
        corona.description = "Just need to grab something real quick. Anyone need a ride?";
        corona.setDepartDateTime(startDate);
        corona.setReturnDateTime(startDate + CreateTripActivity.DAY_IN_MILLIS);
        corona.creatorId = sydId;
        trips.add(corona);

        Trip yosemite = new Trip();
        startDate += (30 * CreateTripActivity.DAY_IN_MILLIS);
        yosemite.name = "Yosemite";
        yosemite.location = "Yosemite, CA";
        yosemite.description = "HALF DOME! Let's do it!";
        yosemite.setDepartDateTime(startDate);
        yosemite.setReturnDateTime(startDate + (4 * CreateTripActivity.DAY_IN_MILLIS));
        yosemite.creatorId = koreyId;
        trips.add(yosemite);

        return trips;
    }
}

























