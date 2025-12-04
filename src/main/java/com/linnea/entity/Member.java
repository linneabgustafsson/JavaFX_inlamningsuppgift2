package com.linnea.entity;

import com.linnea.Rental;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {

    private String personalIdNumber;
    private String firstName;
    private String lastName;
    private String membershipLevel;
    private List<Rental> orderHistory = new ArrayList<>();

//    //!!!!!!!!!!!!!!!!!!
//    private final BooleanProperty NAMN = new SimpleBooleanProperty(false);

    public Member(String personalIdNumber, String firstName, String lastName, String membershipLevel) {
        this.personalIdNumber = personalIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membershipLevel = membershipLevel;
    }

    public Member() {
    }

//    //!!!!!!!!!!!!!!!!!!
//    public boolean isActive() {
//        return NAMN.get();
//    }
//    public BooleanProperty activeProperty() {
//        return NAMN;
//    }
//    public void setActive(boolean value) {
//        NAMN.set(value);
//    }


    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMembershipLevel()  {
        return  membershipLevel;
    }

    public List getOrderHistory()   {
        return  orderHistory;
    }

    public void setPersonalIdNumber(String personalIdNumber) {
        this.personalIdNumber = personalIdNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public void setOrderHistory(List<Rental> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String toString()    {
        return personalIdNumber + "\n" + firstName + "\n" + lastName + "\n" + membershipLevel + "";
    }

    public String toStringOrderHistory()    {
        return "Orderhistorik; " + orderHistory;
    }
}