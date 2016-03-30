package com.emanuel.shotgun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        listView = (ListView) findViewById(R.id.lv_trips);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateTripActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        trips = db.getTrips();
        db.close();

        adapter = new TripAdapter(this,trips);
        listView.setAdapter(adapter);
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
            TextView tv_name = (TextView) rowView.findViewById(R.id.trip_name);
            TextView tv_location = (TextView) rowView.findViewById(R.id.trip_location);

            tv_name.setText(trips.get(position).name);
            tv_location.setText(trips.get(position).location);

            return rowView;
        }
    }
}
