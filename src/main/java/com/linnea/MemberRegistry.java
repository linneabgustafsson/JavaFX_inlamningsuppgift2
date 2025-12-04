package com.linnea;

import com.linnea.entity.Member;

import java.util.List;

public class MemberRegistry {

    private List<Member> memberList;

    public MemberRegistry() {
    }

    public MemberRegistry(List<Member> memberList) {
        this.memberList = memberList;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public Member findMember(String userInputSearch)    {

        for (Member member : memberList)    {
            if (member.getPersonalIdNumber().equalsIgnoreCase(userInputSearch) ||
                member.getFirstName().equalsIgnoreCase(userInputSearch) ||
                member.getLastName().equalsIgnoreCase(userInputSearch))  {
                return member;
            }
        }

        return null;
    }

    public void addMemberToRegistry(Member member) {
        memberList.add(member);
    }
}
