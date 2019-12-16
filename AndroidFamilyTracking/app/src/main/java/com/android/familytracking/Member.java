package com.android.familytracking;

/**
 * Created by PC13 on 8/4/2017.
 */

public class Member {
    private String MemberId, MemberName, MemberEmail, Group1, Group2;

    public Member() {
        MemberId = "";
        MemberName = "";
        MemberEmail = "";
        Group1 = "";
        Group2 = "";
    }

    public Member(String memberId, String memberName, String memberEmail, String group1, String group2) {
        MemberId = memberId;
        MemberName = memberName;
        MemberEmail = memberEmail;
        Group1 = group1;
        Group2 = group2;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getMemberEmail() {
        return MemberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        MemberEmail = memberEmail;
    }

    public String getGroup1() {
        return Group1;
    }

    public void setGroup1(String group1) {
        Group1 = group1;
    }

    public String getGroup2() {
        return Group2;
    }

    public void setGroup2(String group2) {
        Group2 = group2;
    }
}
