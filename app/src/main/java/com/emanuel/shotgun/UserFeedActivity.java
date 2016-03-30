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
import com.emanuel.shotgun.utils.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserFeedActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<User> users;
    ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        users = db.getUsers();
        db.close();

        adapter = new UserAdapter(this, users);
        listView.setAdapter(adapter);
    }

    private class UserAdapter extends ArrayAdapter<User> {
        private final Context context;
        private final ArrayList<User> users;

        public UserAdapter(Context context, ArrayList<User> users) {
            super(context, -1, users);
            this.context = context;
            this.users = users;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_user, parent, false);
            TextView tv_name = (TextView) rowView.findViewById(R.id.username);

            tv_name.setText(users.get(position).firstName + " " + users.get(position).lastName);


            return rowView;
        }
    }
}
