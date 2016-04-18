package com.emanuel.shotgun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.emanuel.shotgun.utils.Trip;

import java.sql.SQLException;
import java.util.ArrayList;

public class TripFeedActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Trip> trips;
    ArrayAdapter<Trip> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_feed);

        //Set logged in user
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs),0);
        String username = sharedPref.getString(getString(R.string.username_key),getString(R.string.guest));

        // initialize the list view
        listView = (ListView) findViewById(R.id.lv_trips);

        // initialize the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateTripActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load information from the database
        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        trips = db.getMyTrips(username);
        db.close();

        adapter = new TripAdapter(this,trips);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip selectedTrip = trips.get(position);
                Intent intent = new Intent(getBaseContext(),ViewTripActivity.class);
                intent.putExtra(ViewTripActivity.TRIP_ID_KEY, selectedTrip.id);
                intent.putExtra(ViewTripActivity.TRIP_NAME_KEY,selectedTrip.name);
                intent.putExtra(ViewTripActivity.TRIP_LOCATION_KEY,selectedTrip.location);
                intent.putExtra(ViewTripActivity.TRIP_DESCRIPTION_KEY,selectedTrip.description);
                intent.putExtra(ViewTripActivity.TRIP_DEPART_TIME_KEY,selectedTrip.getDepartDateTime());
                intent.putExtra(ViewTripActivity.TRIP_RETURN_TIME_KEY,selectedTrip.getReturnDateTime());
                startActivity(intent);
            }
        });
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
        Intent intent;

        switch(id){
            case R.id.action_find_friends:
                intent = new Intent(this, UserFeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_explore:
                return true;
            case R.id.action_sign_out:
                signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs),0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.username_key), "");
        editor.commit();
    }

    private class TripAdapter extends ArrayAdapter<Trip> {
        private final Context context;
        private final ArrayList<Trip> trips;

        public TripAdapter(Context context, ArrayList<Trip> trips){
                super(context, -1, trips);
                this.context = context;
                this.trips = trips;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_trip, parent, false);

            int remainingRiders;
            String creator;

            // Load information from the database
            DBHelper db = new DBHelper(getApplicationContext());
            try {
                db.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            remainingRiders = db.getRemainingSeats(trips.get(position));
            creator = db.getTripCreator(trips.get(position));
            db.close();

            TextView tv_name = (TextView) rowView.findViewById(R.id.trip_name);
            TextView tv_creator = (TextView) rowView.findViewById(R.id.trip_creator);
            TextView tv_seats = (TextView) rowView.findViewById(R.id.trip_seats);

            tv_name.setText(trips.get(position).name);
            tv_creator.setText(creator);
            tv_seats.setText(remainingRiders + " seats available");

            return rowView;
        }
    }
}
