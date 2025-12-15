package com.linnea;

import com.linnea.entity.Vehicle;
import com.linnea.entity.Member;

import java.io.Serializable;

public class Rental implements Serializable {

    private Member member;
    private Vehicle vehicle;
    private int numberOfDays;

    public Rental() {
    }

    public Rental(Vehicle vehicle, int numberOfDays, Member member) {
        this.member = member;
        this.vehicle = vehicle;
        this.numberOfDays = numberOfDays;
    }

    public Member getMember()   {
        return member;
    }

    public Vehicle getItem()   {
        return vehicle;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setItem(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String toString()    {
        return "Medlemmen som bokat: \n" + member + "\n\nDet uthyrda fordonet:\n " + vehicle + "\n\nAntal uthyrningsdagar: " + numberOfDays;
    }
}
