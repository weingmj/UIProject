package com.example.uiproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SampleBNV extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_bnv);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.page_1) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SubjectMainFragment())
                            .commit();
                    return true;
                } else if (itemId == R.id.page_2) {
                    SharedPreferences pref = getSharedPreferences("app_data", MODE_PRIVATE);
                    if (pref.getString("saved_lecturelist", null) == null) {
                        Toast.makeText(SampleBNV.this, "에브리타임에서 시간표를 먼저 불러와야 사용 가능합니다!", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new TimetableMainFragment())
                                .commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }
}