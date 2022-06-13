package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private EditText username_edittext;
    private EditText password_edittext;
    private Button login_button;
    private Button signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if logged in, then move to main screen
        if (ParseUser.getCurrentUser() != null) {
            Log.i(TAG, "Logging in previously logged in user");
            GoToMainActivity();
        }

        username_edittext = findViewById(R.id.username_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_edittext.getText().toString();
                String password = password_edittext.getText().toString();

                LoginUser(username, password);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_edittext.getText().toString();
                String password = password_edittext.getText().toString();

                SignupUser(username, password);
            }
        });
    }

    private void GoToMainActivity() {
        Log.i(TAG, "Moving to main activity");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish(); // to prevent moving back
    }

    private void LoginUser(String username, String password) {
        Log.i(TAG, "Logging in: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Unable to log in. Perhaps create new account.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                // go to main activity with correct login
                GoToMainActivity();
            }
        });
    }

    private void SignupUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(e -> {
            if (e == null) {
                // Succeed
                // Show success
                Toast.makeText(LoginActivity.this, "Successfully created new account", Toast.LENGTH_SHORT).show();
                // go to main activity with correct login
                GoToMainActivity();
            } else {
                // Sign up didn't succeed
                // Show exception
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}