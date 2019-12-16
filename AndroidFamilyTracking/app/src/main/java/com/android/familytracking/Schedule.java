package com.android.familytracking;

/**
 * Created by PC13 on 9/9/2017.
 */

public class Schedule {

    private String scheduleId, schedule, scheduleDay,scheduleDate,scheduleBegin, scheduleFinish, scheduleLongitude, scheduleLatitude, scheduleNotification;

    public Schedule(){

    }

    public String getScheduleNotification() {
        return scheduleNotification;
    }

    public void setScheduleNotification(String scheduleNotification) {
        this.scheduleNotification = scheduleNotification;
    }

    public String getScheduleLongitude() {
        return scheduleLongitude;
    }

    public void setScheduleLongitude(String scheduleLongitude) {
        this.scheduleLongitude = scheduleLongitude;
    }

    public String getScheduleLatitude() {
        return scheduleLatitude;
    }

    public void setScheduleLatitude(String scheduleLatitude) {
        this.scheduleLatitude = scheduleLatitude;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(String scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleBegin() {
        return scheduleBegin;
    }

    public void setScheduleBegin(String scheduleBegin) {
        this.scheduleBegin = scheduleBegin;
    }

    public String getScheduleFinish() {
        return scheduleFinish;
    }

    public void setScheduleFinish(String scheduleFinish) {
        this.scheduleFinish = scheduleFinish;
    }
}
