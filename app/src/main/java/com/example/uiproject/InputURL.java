package com.example.uiproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uiproject.databinding.InputUrlBinding;

public class InputURL extends AppCompatActivity {
    String userInput;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputUrlBinding binding = InputUrlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LayoutInflater inflater = getLayoutInflater();
        View progressView = inflater.inflate(R.layout.progressbar, null);
        FrameLayout rootView = findViewById(android.R.id.content);
        binding.testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.addView(progressView);
                userInput = binding.editTextInputURL.getText().toString();
                ResultReceiver receiver = new ResultReceiver(new Handler(Looper.getMainLooper())) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        if (resultCode == 0) {
                            rootView.removeView(progressView);
                        }
                    }
                };
                Intent intent = new Intent(InputURL.this, GetLectureListService.class);
                intent.putExtra("url", userInput);
                intent.putExtra("receiver", receiver);
                intent.putExtra("fromEveryTime", true);
                startService(intent);
            }
        });
    }
}