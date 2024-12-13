package com.example.uiproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.provider.AlarmClock;

import androidx.appcompat.app.AppCompatActivity;
import com.example.uiproject.databinding.TimetableAlarmBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class TimetableAlarmMain extends AppCompatActivity {
    TimetableAlarmBinding binding;
    Uri selectedSong;
    String chosenRingtone;
    boolean[][][] timetableManager;
    int[][] lectureStartTimes = new int[5][2];

    private void setLectureStartTimes() {
        ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultData != null) {
                    timetableManager = (boolean[][][]) resultData.getSerializable("timetableManager");
                    for (int day = 0; day < 5; day++) {
                        boolean breaker = false;
                        for (int i = 0; i < 12; i++) {
                            for (int j = 0; j < 60; j++) {
                                if (timetableManager[day][i][j]) {
                                    lectureStartTimes[day][0] = i + 9;
                                    lectureStartTimes[day][1] = j;
                                    breaker = true;
                                    break;
                                }
                            }
                            if (breaker) break;
                        }
                        if (!breaker) {
                            lectureStartTimes[day][0] = -1;
                            lectureStartTimes[day][1] = -1;
                        }
                    }
                }
            }
        };
        Intent serviceIntent = new Intent(this, ManageLectureList.class);
        serviceIntent.putExtra("receiver", resultReceiver);
        startService(serviceIntent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TimetableAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setLectureStartTimes();
        binding.numberPickerAlarmHour.setMinValue(0);
        binding.numberPickerAlarmHour.setMaxValue(5);
        binding.numberPickerAlarmMinute.setMinValue(0);
        binding.numberPickerAlarmMinute.setMaxValue(59);

        binding.buttonAlarmCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "MonAlarm");
                PendingIntent pendingIntent = PendingIntent.getActivity(TimetableAlarmMain.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                SystemClock.sleep(1000);
                createAlarm(binding.numberPickerAlarmHour.getValue(), binding.numberPickerAlarmMinute.getValue());
            }
        });
        SharedPreferences pref = getSharedPreferences("alarm", MODE_PRIVATE);
        if (pref.getInt("MonHour", 0) == -1) {
            binding.timetableAlarmMonTime.setText("공강");
        } else if (pref.getInt("MonHour", 0) == 0) {
            binding.timetableAlarmMonTime.setText("미설정");
        } else {
            String zeroHour = "";
            String zeroMin = "";
            if (pref.getInt("MonHour", -1) < 10) zeroHour = "0";
            if (pref.getInt("MonMin", -1) < 10) zeroMin = "0";
            String temp = zeroHour + pref.getInt("MonHour", -1) + " : " + zeroMin + pref.getInt("MonMin", -1);
            binding.timetableAlarmMonTime.setText(temp);
        }

        if (pref.getInt("TueHour", 0) == -1) {
            binding.timetableAlarmTueTime.setText("공강");
        } else if (pref.getInt("TueHour", 0) == 0) {
            binding.timetableAlarmTueTime.setText("미설정");
        } else {
            String zeroHour = "";
            String zeroMin = "";
            if (pref.getInt("TueHour", -1) < 10) zeroHour = "0";
            if (pref.getInt("TueMin", -1) < 10) zeroMin = "0";
            String temp = zeroHour + pref.getInt("TueHour", -1) + " : " + zeroMin + pref.getInt("TueMin", -1);
            binding.timetableAlarmTueTime.setText(temp);
        }

        if (pref.getInt("WedHour", 0) == -1) {
            binding.timetableAlarmWedTime.setText("공강");
        } else if (pref.getInt("WedHour", 0) == 0) {
            binding.timetableAlarmWedTime.setText("미설정");
        } else {
            String zeroHour = "";
            String zeroMin = "";
            if (pref.getInt("WedHour", -1) < 10) zeroHour = "0";
            if (pref.getInt("WedMin", -1) < 10) zeroMin = "0";
            String temp = zeroHour + pref.getInt("WedHour", -1) + " : " + zeroMin + pref.getInt("WedMin", -1);
            binding.timetableAlarmWedTime.setText(temp);
        }
        Log.d("WMJ", String.valueOf(pref.getInt("ThuHour", 0)));
        if (pref.getInt("ThuHour", 0) == -1) {
            binding.timetableAlarmThuTime.setText("공강");
        } else if (pref.getInt("ThuHour", 0) == 0) {
            binding.timetableAlarmThuTime.setText("미설정");
        } else {
            String zeroHour = "";
            String zeroMin = "";
            if (pref.getInt("ThuHour", -1) < 10) zeroHour = "0";
            if (pref.getInt("ThuMin", -1) < 10) zeroMin = "0";
            String temp = zeroHour + pref.getInt("ThuHour", -1) + " : " + zeroMin + pref.getInt("ThuMin", -1);
            binding.timetableAlarmThuTime.setText(temp);
        }

        if (pref.getInt("FriHour", 0) == -1) {
            binding.timetableAlarmFriTime.setText("공강");
        } else if (pref.getInt("FriHour", 0) == 0) {
            binding.timetableAlarmFriTime.setText("미설정");
        } else {
            String zeroHour = "";
            String zeroMin = "";
            if (pref.getInt("FriHour", -1) < 10) zeroHour = "0";
            if (pref.getInt("FriMin", -1) < 10) zeroMin = "0";
            String temp = zeroHour + pref.getInt("FriHour", -1) + " : " + zeroMin + pref.getInt("FriMin", -1);
            binding.timetableAlarmFriTime.setText(temp);
        }
    }

    private void createAlarm(int hoursBefore, int minutesBefore) {
        for (int dayOfWeek = Calendar.MONDAY; dayOfWeek <= Calendar.FRIDAY; dayOfWeek++) {
            int alarmMinute;
            boolean isBigBefore;
            if (minutesBefore <= lectureStartTimes[dayOfWeek - Calendar.MONDAY][1]) {
                alarmMinute = lectureStartTimes[dayOfWeek - Calendar.MONDAY][1] - minutesBefore;
                isBigBefore = false;
            } else {
                alarmMinute = 60 + lectureStartTimes[dayOfWeek - Calendar.MONDAY][1] - minutesBefore;
                isBigBefore = true;
            }
            int alarmHour;
            if (isBigBefore) {
                alarmHour = lectureStartTimes[dayOfWeek - Calendar.MONDAY][0] - hoursBefore - 1;
            } else {
                alarmHour = lectureStartTimes[dayOfWeek - Calendar.MONDAY][0] - hoursBefore;
            }

            if (lectureStartTimes[dayOfWeek - Calendar.MONDAY][0] == -1) {
                alarmHour = -1;
                alarmMinute = -1;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
            calendar.set(Calendar.MINUTE, alarmMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

            SharedPreferences pref = getSharedPreferences("alarm", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            switch (dayOfWeek) {
                case Calendar.MONDAY:
                    editor.putInt("MonHour", alarmHour);
                    editor.putInt("MonMin", alarmMinute);
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, "MonAlarm");
                    break;
                case Calendar.TUESDAY:
                    editor.putInt("TueHour", alarmHour);
                    editor.putInt("TueMin", alarmMinute);
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, "TueAlarm");
                    break;
                case Calendar.WEDNESDAY:
                    editor.putInt("WedHour", alarmHour);
                    editor.putInt("WedMin", alarmMinute);
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, "WedAlarm");
                    break;
                case Calendar.THURSDAY:
                    editor.putInt("ThuHour", alarmHour);
                    editor.putInt("ThuMin", alarmMinute);
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, "ThuAlarm");
                    break;
                case Calendar.FRIDAY:
                    editor.putInt("FriHour", alarmHour);
                    editor.putInt("FriMin", alarmMinute);
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, "FriAlarm");
                    break;
            }


            intent.putExtra(AlarmClock.EXTRA_HOUR, alarmHour);
            intent.putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute);
            intent.putExtra(AlarmClock.EXTRA_RINGTONE, selectedSong);

            ArrayList<Integer> days = new ArrayList<>();
            days.add(dayOfWeek);
            intent.putExtra(AlarmClock.EXTRA_DAYS, days);
            intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            startActivity(intent);

            editor.apply();
            SystemClock.sleep(1000);
        }
        recreate();
    }
}


