package com.emanuel.shotgun;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateTripActivity extends AppCompatActivity {

    long departTime;
    long returnTime;

    // ui references
    AutoCompleteTextView tvTitle;
    AutoCompleteTextView tvLocation;
    TextView tvDepartTime;
    TextView tvReturnTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        tvDepartTime = (TextView) findViewById(R.id.depart_time);
        tvReturnTime = (TextView) findViewById(R.id.return_time);

        Calendar c = Calendar.getInstance();
        departTime = c.getTimeInMillis();
        returnTime = c.getTimeInMillis() + 7200000;
        tvDepartTime.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", departTime));
        tvReturnTime.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", returnTime));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_trip, menu);
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

    public void selectDateTime(View v){
        DialogFragment fragment = new DateTimePickerFragment();
        fragment.show(getFragmentManager(), "picker");
    }

    public void cancel(View v){
        Intent intent = new Intent(this, TripFeed.class);
        startActivity(intent);
    }

    public void createTrip(View v){
        // TODO ADD TRIP TO FIREBASE
        Intent intent = new Intent(this, TripFeed.class);
        startActivity(intent);
    }
}
