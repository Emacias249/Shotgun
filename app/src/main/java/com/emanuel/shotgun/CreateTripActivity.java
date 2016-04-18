package com.emanuel.shotgun;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.emanuel.shotgun.utils.InputValidator;
import com.emanuel.shotgun.utils.Trip;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Calendar;

public class CreateTripActivity extends AppCompatActivity {

    public static final long DAY_IN_MILLIS = 86400000;
    long departTime;
    long returnTime;
    boolean returnTimeSet = false;

    // UI references
    EditText et_Title;
    EditText et_Address;
    EditText et_City;
    TextView et_Description;
    TextView tvDepartTime;
    TextView tvReturnTime;
    Spinner states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        states = (Spinner) findViewById(R.id.states);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.us_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(adapter);

        et_Title = (EditText) findViewById(R.id.title);
        et_Description = (EditText) findViewById(R.id.description);
        et_Address = (EditText) findViewById(R.id.address);
        et_City = (EditText) findViewById(R.id.city);
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
        LayoutInflater inflater = LayoutInflater.from(CreateTripActivity.this);
        final View picker = inflater.inflate(R.layout.date_time_picker, null);
        final int id = v.getId();

        // Set default selected date and time when picker opens
        final DatePicker datePicker = (DatePicker) picker.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) picker.findViewById(R.id.time_picker);

        Calendar calendar = Calendar.getInstance();
        if(id == R.id.depart_time){
            calendar.setTimeInMillis(departTime);
        } else{
            calendar.setTimeInMillis(returnTime);
        }
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        timePicker.setCurrentHour(calendar.getTime().getHours());
        timePicker.setCurrentMinute(calendar.getTime().getMinutes());

        // Open the dialog
        AlertDialog dialog = new AlertDialog.Builder(CreateTripActivity.this)
                .setView(picker)
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedDay = datePicker.getDayOfMonth();
                        int selectedMonth = datePicker.getMonth();
                        int selectedYear = datePicker.getYear();
                        int selectedHour = timePicker.getCurrentHour();
                        int selectedMinute = timePicker.getCurrentMinute();
                        Calendar c = Calendar.getInstance();
                        c.set(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMinute);
                        switch(id){
                            case R.id.depart_time:
                                departTime = c.getTimeInMillis();
                                tvDepartTime.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", departTime));
                                if(!returnTimeSet) {
                                    returnTime = c.getTimeInMillis() + 7200000;
                                    tvReturnTime.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", returnTime));
                                }
                                break;
                            case R.id.return_time:
                                returnTime = c.getTimeInMillis();
                                tvReturnTime.setText(String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", returnTime));
                                returnTimeSet = true;
                                break;
                            default: break;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    public void cancel(View v){
        Intent intent = new Intent(this, TripFeedActivity.class);
        startActivity(intent);
    }

    public void createTrip(View v){

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs),0);
        String username = sharedPref.getString(getString(R.string.username_key),getString(R.string.guest));

        String tripName = et_Title.getText().toString();
        String tripDescription = et_Description.getText().toString();
        String tripAddress = et_Address.getText().toString();
        String tripCity = et_City.getText().toString();
        String tripState = states.getSelectedItem().toString();

        if(tripName.equals("")){
            Toast.makeText(this,"Title is a required field",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tripCity.equals("")){
            Toast.makeText(this,"City is a required field",Toast.LENGTH_SHORT).show();
            return;
        }

        Trip trip = new Trip();
        trip.name = tripName;
        // TODO: LOCATION NEEDS TO BE MORE SPECIFIC
        trip.location = tripCity + ", " + tripState;
        trip.description = tripDescription;
        trip.setDepartDateTime(departTime);
        trip.setReturnDateTime(returnTime);

        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        trip.creatorId = db.getUser(username).id;
        db.addTrip(trip);
        db.close();

        Intent intent = new Intent(this, TripFeedActivity.class);
        startActivity(intent);
    }

}
