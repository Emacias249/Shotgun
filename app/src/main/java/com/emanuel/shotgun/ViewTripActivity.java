package com.emanuel.shotgun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewTripActivity extends AppCompatActivity {

    public final static String TRIP_NAME_KEY = "TripNameKey";
    public final static String TRIP_LOCATION_KEY = "TripLocationKey";
    public final static String TRIP_DESCRIPTION_KEY = "TripDescriptionKey";
    public final static String TRIP_DEPART_TIME_KEY = "TripDepartTimeKey";
    public final static String TRIP_RETURN_TIME_KEY = "TripReturnTimeKey";

    private String tripName;
    private String tripLocation;
    private String tripDescription;
    private long tripDepartTime;
    private long tripReturnTime;

    // UI elements
    private TextView tripNameView;
    private TextView tripLocationView;
    private TextView tripDescriptionView;
    private TextView tripDepartView;
    private TextView tripReturnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        // Get values from intent
        Intent intent = getIntent();
        tripName = intent.getStringExtra(TRIP_NAME_KEY);
        tripLocation = intent.getStringExtra(TRIP_LOCATION_KEY);
        tripDescription = intent.getStringExtra(TRIP_DESCRIPTION_KEY);
        tripDepartTime = intent.getLongExtra(TRIP_DEPART_TIME_KEY, 0);
        tripReturnTime = intent.getLongExtra(TRIP_RETURN_TIME_KEY, 0);

        // Set up UI elements
        tripNameView = (TextView) findViewById(R.id.trip_name);
        tripLocationView = (TextView) findViewById(R.id.trip_location);
        tripDescriptionView = (TextView) findViewById(R.id.trip_description);
        tripDepartView = (TextView) findViewById(R.id.depart_time);
        tripReturnView = (TextView) findViewById(R.id.return_time);

        // Set proper values to text fields
        tripNameView.setText(tripName);
        tripLocationView.setText(tripLocation);
        tripDescriptionView.setText(tripDescription);
        tripDepartView.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", tripDepartTime));
        tripReturnView.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", tripReturnTime));
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
}
