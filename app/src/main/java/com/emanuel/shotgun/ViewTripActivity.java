package com.emanuel.shotgun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.emanuel.shotgun.utils.Trip;
import com.emanuel.shotgun.utils.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class ViewTripActivity extends AppCompatActivity {

    public final static String TRIP_ID_KEY = "TripIdKey";
    public final static String TRIP_NAME_KEY = "TripNameKey";
    public final static String TRIP_LOCATION_KEY = "TripLocationKey";
    public final static String TRIP_DESCRIPTION_KEY = "TripDescriptionKey";
    public final static String TRIP_DEPART_TIME_KEY = "TripDepartTimeKey";
    public final static String TRIP_RETURN_TIME_KEY = "TripReturnTimeKey";

    private Trip selectedTrip;
    private ArrayList<User> usersForTrip;
    private ArrayAdapter<User> userAdapter;

    // UI elements
    private TextView tripNameView;
    private TextView tripLocationView;
    private TextView tripDescriptionView;
    private TextView tripDepartView;
    private TextView tripReturnView;
    private ListView ridersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        // Get values from intent
        Intent intent = getIntent();
        selectedTrip.id = intent.getIntExtra(TRIP_ID_KEY,0);
        selectedTrip.name = intent.getStringExtra(TRIP_NAME_KEY);
        selectedTrip.location = intent.getStringExtra(TRIP_LOCATION_KEY);
        selectedTrip.description = intent.getStringExtra(TRIP_DESCRIPTION_KEY);
        selectedTrip.setDepartDateTime(intent.getLongExtra(TRIP_DEPART_TIME_KEY, 0));
        selectedTrip.setReturnDateTime(intent.getLongExtra(TRIP_RETURN_TIME_KEY, 0));

        // Set up UI elements
        tripNameView = (TextView) findViewById(R.id.trip_name);
        tripLocationView = (TextView) findViewById(R.id.trip_location);
        tripDescriptionView = (TextView) findViewById(R.id.trip_description);
        tripDepartView = (TextView) findViewById(R.id.depart_time);
        tripReturnView = (TextView) findViewById(R.id.return_time);

        // Set proper values to text fields
        tripNameView.setText(selectedTrip.name);
        tripLocationView.setText(selectedTrip.location);
        tripDescriptionView.setText(selectedTrip.description);
        tripDepartView.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", selectedTrip.getDepartDateTime()));
        tripReturnView.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", selectedTrip.getReturnDateTime()));

        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        usersForTrip = db.getUsersForTrip(selectedTrip);
        db.close();

        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersForTrip);
        ridersView.setAdapter(userAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void joinTrip_Click(View v){
        // TODO TEST
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs), 0);
        String username = sharedPref.getString(getString(R.string.username_key),getString(R.string.guest));

        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User user = db.getUser(username);
        db.joinTrip(selectedTrip.id, user.id);
        db.close();

        usersForTrip.add(user);
        userAdapter.notifyDataSetChanged();
    }
}
