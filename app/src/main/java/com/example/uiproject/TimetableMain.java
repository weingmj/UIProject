package com.example.uiproject;

import static android.view.Gravity.TOP;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uiproject.databinding.TimetableMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimetableMain extends AppCompatActivity {
    private TimetableMainBinding binding;
    private final int startTime = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TimetableMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        List<EachLecture> list = new ArrayList<>();
        EachLecture eachLecture = new EachLecture("화", 11, 30, 15, 20, "sample");
        list.add(eachLecture);
        int[][] pos = new int[5][2]; // pos[n][x or y] : n요일의 x, y좌표
        binding.textViewTimetableMon.getLocationOnScreen(pos[0]);
        binding.textViewTimetableTue.getLocationOnScreen(pos[1]);
        binding.textViewTimetableWed.getLocationOnScreen(pos[2]);
        binding.textViewTimetableThr.getLocationOnScreen(pos[3]);
        binding.textViewTimetableFri.getLocationOnScreen(pos[4]);
        int[] eachBlockSize = {binding.timetableEachBlockSample.getWidth(), binding.timetableEachBlockSample.getHeight()};
        AtomicInteger cnt = new AtomicInteger();
        for (EachLecture e : list) {
            int x = 0, y;
            switch (e.getWeekDay()) {
                case ("월") :
                    x = pos[0][0];
                    break;
                case ("화") :
                    x = pos[1][0];
                    break;
                case ("수") :
                    x = pos[2][0];
                    break;
                case ("목") :
                    x = pos[3][0];
                    break;
                case ("금") :
                    x = pos[4][0];
                    break;
            }
            y = e.getStartHour() - startTime;
            y = pos[0][1] + y * eachBlockSize[1] - 8;
            y += eachBlockSize[1] * e.getStartMin() / 60;
            TextView button = new TextView(this);
            button.setText(e.getLectureName());
            button.setMinWidth(30);
            button.setWidth(eachBlockSize[0]);
            button.setOnClickListener(view -> {
                Log.d("WMJ", "hi "+String.valueOf(cnt.getAndIncrement()));
            });
            // 생성된 TextView에 레이아웃 파라미터 적용
            button.setX(x);
            button.setY(y);
            button.setGravity(TOP);
            button.setPadding(4, 4, 0, 0);
            button.setTextSize(12);
            button.setHeight(eachBlockSize[1] * ((e.getEndHour() * 60 + e.getEndMin()) - (e.getStartHour() * 60 + e.getStartMin())) / 60);
            Log.d("WMJ", String.valueOf(eachBlockSize[1] * ((e.getEndHour() * 60 + e.getEndMin()) - (e.getStartHour() * 60 + e.getStartMin())) / 60));
            button.setBackground(getResources().getDrawable(R.color.pink));
            Log.d("WMJ", String.valueOf(eachBlockSize[1]));
            // binding의 부모 레이아웃에 TextView 추가
            binding.getRoot().addView(button);
        }
    }
}

class EachLecture {
    private String weekDay;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private String lectureName;

    public EachLecture(String weekDay, int startHour, int startMin, int endHour, int endMin, String lectureName) {
        this.weekDay = weekDay;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.lectureName = lectureName;
    }

    public String getWeekDay() {
        return weekDay;
    }
    public String getLectureName() {
        return lectureName;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }
}