package com.emanuel.shotgun;

import android.os.Bundle;
import android.app.Activity;

public class RequestRideActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
