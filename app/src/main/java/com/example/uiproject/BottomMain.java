package com.example.uiproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

//                if (itemId == R.id.page_1){
//                    startActivity(new Intent(BottomMain.this, calendar.class));
//                    return true;
//                }
                if (itemId == R.id.page_2){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SchedulerMain())
                            .commit();
                    return true;
                }
                if (itemId == R.id.page_3) {
                    SharedPreferences pref = getSharedPreferences("app_data", MODE_PRIVATE);
                    Log.d("WMJ", pref.getString("saved_lecturelist", null));
                    if (pref.getString("saved_lecturelist", null) == null) {
                        Toast.makeText(BottomMain.this, "에브리타임에서 시간표를 먼저 불러와야 사용 가능합니다!", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new TimetableMainFragment())
                                .commit();
                    }
                    return true;
                }
                if (itemId == R.id.page_4){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SubjectMainFragment())
                            .commit();
                    return true;
                }

                return false;
            }
        });

        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                //nothing
            }
        });
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, )
    }
}