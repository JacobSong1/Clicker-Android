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

public class LoginActivity extends AppCompatActivity {

    String serverIP = "10.0.2.2";
    String port = "9999";

    EditText etUserID, etPassword;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserID = findViewById(R.id.etUserID);
        etPassword = findViewById(R.id.etPassword);
        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String userID = etUserID.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (userID.isEmpty() || password.isEmpty()) {
                tvResult.setText("Please fill in all fields!");
                return;
            }

            String url = "http://" + serverIP + ":" + port
                    + "/clicker/login?userID=" + userID
                    + "&password=" + password;
            new LoginTask().execute(url);
        });

        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    class LoginTask extends AsyncTask<String, Void, String> {
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
                // Go to voting page
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userID", etUserID.getText().toString().trim());
                startActivity(intent);
                finish();
            } else {
                tvResult.setText(result);
            }
        }
    }
}