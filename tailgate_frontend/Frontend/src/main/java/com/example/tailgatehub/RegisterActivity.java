package com.example.tailgatehub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText usernameInput, passwordInput, emailInput, passwordConfirmationInput;
    private Button registerButton;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;
    final String REGISTER_URL = "http://10.0.2.2:8080/api/users/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get the input boxes
        usernameInput = findViewById(R.id.registerUsernameText);
        emailInput = findViewById(R.id.registerEmailText);
        passwordInput = findViewById(R.id.registerPasswordText);
        passwordConfirmationInput = findViewById(R.id.confirmPasswordText);

        progressBar = findViewById(R.id.progressBar);

        // create a volley queue
        requestQueue = Volley.newRequestQueue(this);

        // Assign event listeners
        registerButton = findViewById(R.id.completeRegistrationButton);
        registerButton.setOnClickListener(v -> {
            try {
                validateRegistration();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This Method validates password match
     * Validates username is available
     * Sends data to server
     * Returns to login page on success
     */
    void validateRegistration() throws JSONException {
        // Grabs all fields by

        // Saves input to a string
        String username = usernameInput.getEditableText().toString();
        String email = emailInput.getEditableText().toString();
        String password = passwordInput.getEditableText().toString();
        String passwordConfirm = passwordConfirmationInput.getEditableText().toString();


        // log the inputs in logcat
        Log.d("toggle_info", username);
        Log.d("toggle_info", email);
        Log.d("toggle_info", password);
        Log.d("toggle_info", passwordConfirm);


        // validate all fields are complete
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(this, "All Fields must me filled out", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6){
            Toast.makeText(this, "Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // validate that passwords match
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send post request to register user
        registerUser(username, password, email);


    }

    void registerUser(String username, String password, String email) {


        try {
            progressBar.setVisibility(View.VISIBLE);
            registerButton.setEnabled(false);

            // Create JSON object for request body
            JSONObject userJson = new JSONObject();
            userJson.put("username", username);
            userJson.put("email", email);
            userJson.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:8080/api/users/register",
                    userJson, // JSON body containing user data
                    response -> {
                        Log.d("RegisterResponse", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                if (success) {

                                    String message = null;
                                    try {
                                        message = response.getString("message");
                                    } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                    }

                                    // Hide loading spinner
                                    progressBar.setVisibility(View.GONE);
                                    registerButton.setEnabled(true);

                                    // show success message
                                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                                    clearFields();
                                    finish();

                            } else {
                                    try {
                                        String error = response.getString("error");
                                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, 1500); // 1.5-second delay for better UX


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setEnabled(true);


                        if (error.networkResponse != null) {
                            try {
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                Toast.makeText(this, errorJson.getString("error"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(this, "Network error. Try again.", Toast.LENGTH_SHORT).show();

                        }
                    }
            );

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Clears the user input fields
    void clearFields(){
        // Clear input fields
        usernameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        passwordConfirmationInput.setText("");
    }

}
