package com.example.tailgatehub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

/*
* This is the main activity screen, shown when the app is launched
*
* */
public class MainActivity extends AppCompatActivity {

    private boolean isValid = false; // flag to indicate if user is valid for login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button registerButton = findViewById(R.id.mainRegisterButton);
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

//        registerButton.setOnClickListener(this::register); // this is another way to write above step 1:

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this::login);

    }

//    public void register(View view){ // step 2
//        // this should open up a page to register a user
//        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//        startActivity(intent);
//    }

    /**
     * Validates if the user has an account
     * so if username = erik and password = password123
     * */
    public boolean validateUser(String username, String password){
        return username.equals("erik") && password.equals("password123");
    }

    /**
     * If user is logged in, on swipe from the left
     * display a collapsable menu or something
     */
    public boolean userLoggedIn(){

        return true;
    }


    /*
    * Handles the login functionality of the user
    * */
    public void login(View view){

        //Grab the attributes form page
        TextView message = findViewById(R.id.textView);
        TextInputEditText usernameInput = findViewById(R.id.usernameText);
        TextInputEditText passwordInput = findViewById(R.id.passwordText);

        // Save the input into strings
        String username = usernameInput.getEditableText().toString();
        String password = passwordInput.getEditableText().toString();


        // Log for debugging
        Log.d("toggle_info", username);
        Log.d("toggle_info", password);



        // if login fields are not empty
        if(!username.isEmpty() && !password.isEmpty() && validateUser(username, password)){
            //method to validate username and password on the backend
            validateUser(username, password);

            //wipe input fields
            usernameInput.setText("");
            passwordInput.setText("");

            // set welcome message
            String welcomeMessage = "Welcome "+ username + "!";
            message.setText(welcomeMessage);

            // indicate valid user
            isValid = true;


        } else {

            // message to ensure both fields are filled out
            Toast.makeText(this, "All Fields must me filled out", Toast.LENGTH_SHORT).show();

            // if password field is left empty
            if (password.isEmpty()){
                Log.d("toggle_info", "password is empty");
            }
            // is userName field is left empty
            if (username.isEmpty()){
                Log.d("toggle_info", "username is empty");
            } 

        }

        // if the username and password are valid, open up the feed
        if(isValid){
            // change to feed screen
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
        }
    }
}