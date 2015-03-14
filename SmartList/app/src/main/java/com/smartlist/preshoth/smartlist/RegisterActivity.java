package com.smartlist.preshoth.smartlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {

    private EditText _usernameField;
    private EditText _passwordField;
    private TextView _errorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _usernameField = (EditText) findViewById(R.id.register_username);
        _passwordField = (EditText) findViewById(R.id.register_password);
        _errorField = (TextView) findViewById(R.id.error_messages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    public void register(final View v){
        if(_usernameField.getText().length() == 0 || _passwordField.getText().length() == 0)
            return;

        v.setEnabled(false);
        ParseUser user = new ParseUser();
        user.setUsername(_usernameField.getText().toString());
        user.setPassword(_passwordField.getText().toString());
        _errorField.setText("");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(RegisterActivity.this, TodoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    switch(e.getCode()){
                        case ParseException.USERNAME_TAKEN:
                            _errorField.setText("Username has already been taken.");
                            break;
                        case ParseException.USERNAME_MISSING:
                            _errorField.setText("Please supply a username");
                            break;
                        case ParseException.PASSWORD_MISSING:
                            _errorField.setText("Please supply a password");
                            break;
                        default:
                            _errorField.setText(e.getLocalizedMessage());
                    }
                    v.setEnabled(true);
                }
            }
        });
    }

    public void showLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
