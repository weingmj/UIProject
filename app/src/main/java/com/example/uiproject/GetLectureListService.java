package com.example.uiproject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

public class GetLectureListService extends Service {

    private static final String TAG = "GetLectureList";
    ResultReceiver receiver;
    boolean isFromEverytime = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userInput = intent.getStringExtra("url");
        receiver = intent.getParcelableExtra("receiver");
        isFromEverytime = intent.getBooleanExtra("fromEveryTime", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendHttpRequest("https://timetable-service-261079985553.us-central1.run.app/?url=" + userInput);
            }
        }).start();
        return START_NOT_STICKY;
    }

    private void sendHttpRequest(String urlString) {
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    response.append('\n');
                }
                saveDataToStringsXml(response.toString());
                in.close();
            } else {
                Log.e(TAG, "GET request failed");
            }

            connection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error during request", e);
        }
    }

    private void saveDataToStringsXml(String data) {
        StringTokenizer tokenizer = new StringTokenizer(data, "\n");
        String result = "";
        while (tokenizer.hasMoreTokens()) {
            result = result.concat(tokenizer.nextToken() + " true\n");
        }
        Log.d("WMJ", result);
        SharedPreferences sharedPreferences = getSharedPreferences("app_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.saved_lecturelist), result);
        editor.apply();
        receiver.send(0, null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}