package com.android.familytracking;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC13 on 10/2/2017.
 */

public class MemberGroupList extends ArrayAdapter<MemberGroup> {
    private Activity context;
    private List<MemberGroup> MemberList;

    public MemberGroupList(Activity context, List<MemberGroup> MemberList) {
        super(context, R.layout.member_group_layout, MemberList);
        this.context = context;
        this.MemberList = MemberList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.member_group_layout, null, true);
        TextView MemberName = (TextView) listViewItem.findViewById(R.id.MemberName);
        MemberGroup member = MemberList.get(position);
        MemberName.setText(member.getMemberId());
        return listViewItem;
    }
}