package com.example.clicker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    String serverIP = "10.0.2.2";
    String port = "9999";

    TextView tvResult;
    EditText etComment;
    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        etComment = findViewById(R.id.etComment);

        // Get userID passed from LoginActivity
        userID = getIntent().getStringExtra("userID");
        if (userID == null) userID = "guest";

        findViewById(R.id.btnA).setOnClickListener(v -> sendChoice("a"));
        findViewById(R.id.btnB).setOnClickListener(v -> sendChoice("b"));
        findViewById(R.id.btnC).setOnClickListener(v -> sendChoice("c"));
        findViewById(R.id.btnD).setOnClickListener(v -> sendChoice("d"));

        findViewById(R.id.btnComment).setOnClickListener(v -> {
            String comment = etComment.getText().toString().trim();
            if (comment.isEmpty()) {
                tvResult.setText("Please type a comment first!");
                return;
            }
            try {
                String encodedComment = java.net.URLEncoder.encode(comment, "UTF-8");
                String url = "http://" + serverIP + ":" + port
                        + "/clicker/comment?userID=" + userID
                        + "&comment=" + encodedComment;
                new NetworkTask("comment").execute(url);
            } catch (Exception e) {
                tvResult.setText("Error: " + e.getMessage());
            }
        });
    }

    void sendChoice(String choice) {
        String url = "http://" + serverIP + ":" + port
                + "/clicker/select?choice=" + choice;
        new NetworkTask("choice").execute(url);
    }

    class NetworkTask extends AsyncTask<String, Void, String> {
        String type;

        NetworkTask(String type) {
            this.type = type;
        }

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
            if (type.equals("choice")) {
                tvResult.setText("Choice recorded! Thank you.");
            } else if (type.equals("comment")) {
                if (result.trim().equals("success")) {
                    tvResult.setText("Comment submitted! Thank you.");
                    etComment.setText("");
                } else {
                    tvResult.setText(result);
                }
            }
        }
    }
}