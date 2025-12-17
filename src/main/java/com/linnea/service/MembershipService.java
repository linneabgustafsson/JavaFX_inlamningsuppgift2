package com.linnea.service;
import com.linnea.MemberRegistry;
import com.linnea.entity.Member;

public class MembershipService {

    private MemberRegistry memberRegistry;

    public MembershipService()  {
    }

    public MembershipService(MemberRegistry memberRegistry){
        this.memberRegistry = memberRegistry;
    }

    public Member findMember(String userInput)    {

        for (Member member : memberRegistry.getMembersObservableList()) {
            if (member.getPersonalIdNumber().equalsIgnoreCase(userInput) ||
                    member.getFirstName().equalsIgnoreCase(userInput) ||
                    member.getLastName().equalsIgnoreCase(userInput)) {

               return member;
            }
        }

        return null;
    }
}