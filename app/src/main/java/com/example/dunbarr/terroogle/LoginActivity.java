package com.example.dunbarr.terroogle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class LoginActivity extends AppCompatActivity {

    public static final String APPLICATION_ID = "D2E5F9E6-9223-39C2-FF93-DE7AC2162300";
    public static final String SECRET_KEY = "528276A7-1BAA-C39F-FF49-839811194800";
    public static final String TAG = "LoginActivity";
    private EditText emailET, passwordET, nameET;
    private TextView cancel, signup;
    private Button login, submit;
    private CheckBox stayLoggedInCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Backendless.initApp(this, APPLICATION_ID, SECRET_KEY);

        String loggedInUser = Backendless.UserService.loggedInUser();

        if (!loggedInUser.isEmpty()) {

            Backendless.UserService.findById(loggedInUser, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser response) {
                    Backendless.UserService.setCurrentUser(response);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.e("LoginActivity", fault.toString());
                }
            });


        }

        Log.d("LoginActivity", "Logged in user: " + loggedInUser);

        emailET = findViewById(R.id.emailet);
        passwordET = findViewById(R.id.passet);
        nameET = findViewById(R.id.nameet);
        cancel = findViewById(R.id.canceltv);
        signup = findViewById(R.id.signuptv);
        login = findViewById(R.id.loginbt);
        submit = findViewById(R.id.signmeupbt);
        stayLoggedInCheckBox = findViewById(R.id.cb_stayLogged);

        logIn();

        //You can set onClickListener onto a textview
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String name = nameET.getText().toString();

                Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("LoginActivity", fault.toString());
                    }
                }, stayLoggedInCheckBox.isChecked());

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String name = nameET.getText().toString();

                /*
                    Could do other checks as well.

                 */

                if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                    BackendlessUser user = new BackendlessUser();
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setProperty("name", name);

                    Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Toast.makeText(LoginActivity.this, response.getEmail() + " was registered", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("LoginActivity", fault.toString());
                        }


                    });
                }

                emailET.setText("");
                passwordET.setText("");
                nameET.setText("");

            }
        });
    }

    public void logIn() {
        nameET.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);

        login.setVisibility(View.VISIBLE);
        signup.setVisibility(View.VISIBLE);
        stayLoggedInCheckBox.setVisibility(View.VISIBLE);
    }

    public void signUp() {
        nameET.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);

        login.setVisibility(View.GONE);
        signup.setVisibility(View.GONE);
        stayLoggedInCheckBox.setVisibility(View.GONE);
    }
}