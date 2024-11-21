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

public class GetLectureListService extends Service {

    private static final String TAG = "GetLectureList";
    ResultReceiver receiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userInput = intent.getStringExtra("url");
        receiver = intent.getParcelableExtra("receiver");
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
            // 서버 URL 설정
            URL url = new URL(urlString);

            // HttpURLConnection 객체 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 방식 설정 (GET, POST 등)
            connection.setRequestMethod("GET");

            // 연결을 시작하고 응답 코드 확인
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);

            // 응답이 성공적인 경우
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 내용을 읽기 위한 스트림 준비
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // 응답을 한 줄씩 읽어서 response에 추가
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    response.append('\n');
                }
                saveDataToStringsXml(response.toString());
                // 응답 내용 로그에 출력
                in.close();
                Log.d(TAG, "Response: " + response.toString());
                // UI 업데이트: 서버에서 받은 응답을 TextView에 표시
            } else {
                Log.e(TAG, "GET request failed");
            }

            // 연결 종료
            connection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error during request", e);
        }
    }

    private void saveDataToStringsXml(String data) {
        // SharedPreferences 사용
        SharedPreferences sharedPreferences = getSharedPreferences("app_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.saved_lecturelist), data);
        editor.apply();
        receiver.send(0, null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}