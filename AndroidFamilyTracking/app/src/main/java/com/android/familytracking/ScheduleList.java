package com.android.familytracking;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC13 on 9/10/2017.
 */

public class ScheduleList extends ArrayAdapter<Schedule> {
    private Activity context;
    private List<Schedule> ScheduleList;

    public ScheduleList(Activity context, List<Schedule> ScheduleList){
        super(context, R.layout.schedule_layout, ScheduleList);
        this.context = context;
        this.ScheduleList = ScheduleList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.schedule_layout, null, true);

        TextView Hari = (TextView)listViewItem.findViewById(R.id.Hari);
        TextView Jam = (TextView)listViewItem.findViewById(R.id.Jam);
        TextView Kegiatan = (TextView)listViewItem.findViewById(R.id.Kegiatan);

        Schedule schedule = ScheduleList.get(position);

        // Set their text
        Hari.setText(schedule.getScheduleDay() + ", " + schedule.getScheduleDate());
        Jam.setText(schedule.getScheduleBegin() + " - " + schedule.getScheduleFinish());

        // Format the date before showing it
        Kegiatan.setText(schedule.getSchedule());

        return listViewItem;
    }
}
