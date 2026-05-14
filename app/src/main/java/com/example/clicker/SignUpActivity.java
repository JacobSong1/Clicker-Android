package com.example.clicker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.net.*;

public class SignUpActivity extends AppCompatActivity {

    String serverIP = "10.0.2.2";
    String port = "9999";

    EditText etPhone, etUserID, etPassword, etConfirmPassword;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etPhone = findViewById(R.id.etPhone);
        etUserID = findViewById(R.id.etUserID);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String userID = etUserID.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validate fields
            if (phone.isEmpty() || userID.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                tvResult.setText("Please fill in all fields!");
                return;
            }

            // Validate phone number is 8 digits
            if (phone.length() != 8) {
                tvResult.setText("Phone number must be 8 digits!");
                return;
            }

            // Validate passwords match
            if (!password.equals(confirmPassword)) {
                tvResult.setText("Passwords do not match!");
                return;
            }

            String url = "http://" + serverIP + ":" + port
                    + "/clicker/register?userID=" + userID
                    + "&password=" + password
                    + "&phone=" + phone;
            new RegisterTask().execute(url);
        });
    }

    class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                return sb.toString();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.trim().equals("success")) {
                tvResult.setText("Registration successful! Please login.");
                // Go back to login page after 2 seconds
                new android.os.Handler().postDelayed(() -> {
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }, 2000);
            } else {
                tvResult.setText(result);
            }
        }
    }
}