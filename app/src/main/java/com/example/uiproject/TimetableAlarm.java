package com.example.uiproject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uiproject.databinding.TimetableAlarmBinding;
import com.example.uiproject.databinding.TimetableAlarmSelectsongBinding;

import java.util.ArrayList;

public class TimetableAlarm extends AppCompatActivity {
    TimetableAlarmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TimetableAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonAlarmSongPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }
    private void showCustomDialog() {
        Dialog dialog = new Dialog(this);
        ArrayList<String> musicList = loadMusicFiles();
        for(String music : musicList) {
            Log.d("WMJ", music);
        }
        TimetableAlarmSelectsongBinding binding2 = TimetableAlarmSelectsongBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding2.getRoot());
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_songSelector);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemViewCacheSize(30);
        MusicAdapter musicAdapter = new MusicAdapter(musicList);
        recyclerView.setAdapter(musicAdapter);
        dialog.show();
    }

    private ArrayList<String> loadMusicFiles() {
        ArrayList<String> musicList = new ArrayList<>();
        Uri musicUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA // 음악 파일의 경로
        };

        try (Cursor cursor = contentResolver.query(musicUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

                do {
                    String title = cursor.getString(titleColumn);
                    musicList.add(title);
                } while (cursor.moveToNext());
            }
        }
        return musicList;
    }
}