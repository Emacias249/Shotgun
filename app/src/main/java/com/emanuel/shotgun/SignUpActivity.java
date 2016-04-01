package com.emanuel.shotgun;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
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
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity {

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void signUp(){
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if(!isUsernameValid(username)) {
            Toast.makeText(SignUpActivity.this, "Username is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isPasswordValid(password)){
            Toast.makeText(SignUpActivity.this, "Password is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, CreateProfile.class);
        intent.putExtra(CreateProfile.USERNAME_KEY, username);
        intent.putExtra(CreateProfile.PASSWORD_KEY, password);
        startActivity(intent);
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

