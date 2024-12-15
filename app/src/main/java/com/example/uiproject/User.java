package com.example.uiproject;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class User {
    private String nickname;
    private String univname;
    private List<Boolean> schedule;
    private List<Boolean> sharedSchedule;
    @ServerTimestamp
    private Timestamp timestamp; // server timestamp

    public User() {}

    public User(String nickname, String univname, List<Boolean> schedule, List<Boolean> sharedSchedule) {
        this.nickname = nickname;
        this.univname = univname;
        this.schedule = schedule;
        this.sharedSchedule = sharedSchedule;
    }

    public List<Boolean> getSharedSchedule() {
        return sharedSchedule;
    }

    public void setSharedSchedule(List<Boolean> sharedSchedule) {
        this.sharedSchedule = sharedSchedule;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUnivname() {
        return univname;
    }

    public void setUnivname(String univname) {
        this.univname = univname;
    }

    public List<Boolean> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Boolean> schedule) {
        this.schedule = schedule;
    }
}
