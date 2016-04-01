package com.emanuel.shotgun;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        DBHelper db = new DBHelper(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!isUsernameValid(username)){
            Toast.makeText(LoginActivity.this, "Username is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isPasswordValid(password)){
            Toast.makeText(LoginActivity.this, "Password is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!db.userExists(username)){
            Toast.makeText(LoginActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isUsernameValid(username) && isPasswordValid(password)){
            if(db.isValidPassword(username, password)){

                // SAVE THE CURRENT USER AS THIS USER
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.my_prefs),0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.username_key), username);
                editor.commit();

                Intent intent = new Intent(this,TripFeedActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this, "Password is not valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return !username.equals("");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return !password.equals("");
    }

}

