package com.android.familytracking;

/**
 * Created by PC13 on 8/5/2017.
 */

public class Group {
    private String GroupName, GroupId, GroupAdmin;

    public String getGroupAdmin() {
        return GroupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        GroupAdmin = groupAdmin;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }
}
