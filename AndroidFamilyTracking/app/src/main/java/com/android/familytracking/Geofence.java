package com.android.familytracking;

/**
 * Created by PC13 on 10/2/2017.
 */

public class Geofence {

    private String Id, ScheduleId, Radius;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(String scheduleId) {
        ScheduleId = scheduleId;
    }

    public String getRadius() {
        return Radius;
    }

    public void setRadius(String radius) {
        Radius = radius;
    }
}
