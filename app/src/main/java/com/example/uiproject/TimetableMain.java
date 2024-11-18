package com.example.uiproject;

import static android.os.SystemClock.sleep;
import static android.view.Gravity.TOP;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.uiproject.databinding.TimetableDialogAddplanBinding;
import com.example.uiproject.databinding.TimetableMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;


public class TimetableMain extends AppCompatActivity {
    private TimetableMainBinding binding;
    private final int startTime = 9;
    private final int endTime = 21;
    static int [] colorList = new int[15];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String userInput;
        super.onCreate(savedInstanceState);
        binding = TimetableMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setColorList();
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 19 + 1; i <= endTime; i++) {
            TableRow tableRow = (TableRow) inflater.inflate(R.layout.timetable_main_eachrow, null);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, // 너비
                    0,                                     // 높이
                    1                                      // Weight
            );
            tableRow.setLayoutParams(params);
            binding.timetableTable.addView(tableRow);
            TextView t = (TextView) tableRow.getChildAt(0);
            t.setText(String.valueOf(i));
            userInput = "https://everytime.kr/@X5z3rQ2ToUIvLmfo34fb";
            Intent intent = new Intent(this, GetLectureListService.class);
            intent.putExtra("url", userInput);
            startService(intent);
            binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    initializeTimetable();
                }
            });
        }
        binding.timetableImageButtonAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean [] dayClicked = {false, false, false, false, false};
                LayoutInflater layoutInflater = getLayoutInflater();
                TimetableDialogAddplanBinding timetableDialogAddplanBinding = TimetableDialogAddplanBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(TimetableMain.this);
                dialog.setContentView(timetableDialogAddplanBinding.getRoot());
                timetableDialogAddplanBinding.buttonAddplanMon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[0]) {
                            setFalse(dayClicked);
                            dayClicked[0] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[0] = false;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanTue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[1]) {
                            setFalse(dayClicked);
                            dayClicked[1] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[1] = false;
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanWed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[2]) {
                            setFalse(dayClicked);
                            dayClicked[2] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[2] = false;
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanThu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[3]) {
                            setFalse(dayClicked);
                            dayClicked[3] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[3] = false;
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.buttonAddplanFri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dayClicked[4]) {
                            setFalse(dayClicked);
                            dayClicked[4] = true;
                            timetableDialogAddplanBinding.buttonAddplanMon.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanTue.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanWed.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanThu.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.red));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.black));
                        } else {
                            dayClicked[4] = false;
                            timetableDialogAddplanBinding.buttonAddplanFri.setTextColor(getColor(R.color.black));
                            timetableDialogAddplanBinding.buttonAddplanAdd.setTextColor(getColor(R.color.gray));
                        }
                    }
                });
                timetableDialogAddplanBinding.numberPickerAddplanStartHour.setMinValue(startTime);
                timetableDialogAddplanBinding.numberPickerAddplanStartHour.setMaxValue(endTime);
                timetableDialogAddplanBinding.numberPickerAddplanStartMin.setMinValue(0);
                timetableDialogAddplanBinding.numberPickerAddplanStartMin.setMaxValue(59);
                timetableDialogAddplanBinding.numberPickerAddplanEndHour.setMinValue(startTime);
                timetableDialogAddplanBinding.numberPickerAddplanEndHour.setMaxValue(endTime);
                timetableDialogAddplanBinding.numberPickerAddplanEndMin.setMinValue(0);
                timetableDialogAddplanBinding.numberPickerAddplanEndMin.setMaxValue(59);
                timetableDialogAddplanBinding.buttonAddplanAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //dayClicked[0]~[4]까지 true있으면 해당 일정이 가능한지 검사, 아무것도 true가 아니면 날짜선택해달라 하면 됨
                    }
                });
                dialog.show();
            }
        });
    }
    protected void setFalse(boolean[] arr) {
        int i = 0;
        for (boolean b : arr) {
            arr[i] = false;
            i++;
        }
    }
    protected void setColorList() {
        colorList[0] = R.color.medium_slate_blue;
        colorList[1] = R.color.cold;
        colorList[2] = R.color.pink;
        colorList[3] = R.color.coral;
        colorList[4] = R.color.dark_sea_green;
        colorList[5] = R.color.dark_olive_green;
        colorList[6] = R.color.light_sea_green;
        colorList[7] = R.color._light_green;
        colorList[8] = R.color.aquamarine;
        colorList[9] = R.color.dark_orange;
        colorList[10] = R.color.dodger_blue;
        colorList[11] = R.color.light_coral;
        colorList[12] = R.color.medium_orchid;
        colorList[13] = R.color.light_sky_blue;
        colorList[14] = R.color.rosy_brown;
    }

    private void initializeTimetable() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_data", MODE_PRIVATE);
        String lectureString = sharedPreferences.getString(getString(R.string.saved_lecturelist), "No data found");
        StringTokenizer tokenizer = new StringTokenizer(lectureString, "\n");
        List<EachLecture> list = new ArrayList<>();
        int k = 0;
        while (tokenizer.hasMoreTokens()) {
            String temp = tokenizer.nextToken();
            if (temp.isBlank()) break;
            list.add(new EachLecture(temp));
        }

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
            y += ((binding.timetableTable.getHeight() * 0.05) / 100);
            Log.d("WMJ", String.valueOf(binding.timetableTable.getHeight()));
            TextView button = new TextView(this);
            button.setText(e.getLectureName());
            button.setMinWidth(30);
            button.setWidth(eachBlockSize[0]);
            button.setOnClickListener(view -> {
                Log.d("WMJ", "hi "+ cnt.getAndIncrement());
            });
            button.setX(x);
            button.setY(y);
            button.setGravity(TOP);
            button.setPadding(4, 4, 0, 0);
            button.setTextSize(12);
            button.setHeight(eachBlockSize[1] * ((e.getEndHour() * 60 + e.getEndMin()) - (e.getStartHour() * 60 + e.getStartMin())) / 60);
            button.setBackground(ContextCompat.getDrawable(this, e.getColor()));
            // binding의 부모 레이아웃에 TextView 추가
            binding.getRoot().addView(button);
        }
    }
}

class EachLecture {
    String [] lectureInfo; // 강의명 / 요일 / 시작 시간/분 / 끝 시간/분 순서로 저장
    int color;
    public EachLecture(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, " ");
        lectureInfo = new String[6];
        int i = 0;
        for (String info : lectureInfo) {
            info = tokenizer.nextToken();
            lectureInfo[i++] = info;
        }
        Random random = new Random();
        color = TimetableMain.colorList[random.nextInt(15)];
    }

    @NonNull
    @Override
    public String toString() {
        return lectureInfo[0] + " " + lectureInfo[1];
    }

    String getLectureName() {
        return lectureInfo[0];
    }
    String getWeekDay() {
        return lectureInfo[1];
    }
    int getStartHour() {
        return Integer.parseInt(lectureInfo[2]);
    }
    int getStartMin() {
        return Integer.parseInt(lectureInfo[3]);
    }
    int getEndHour() {
        return Integer.parseInt(lectureInfo[4]);
    }
    int getEndMin() {
        return Integer.parseInt(lectureInfo[5]);
    }
    int getColor() {
        return color;
    }
}