package com.android.familytracking;

/**
 * Created by PC13 on 8/6/2017.
 */

public class MemberGroup {
    private String MemberId;

    public MemberGroup() {
        MemberId = "";
    }

    public MemberGroup(String memberId) {
        MemberId = memberId;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }
}
