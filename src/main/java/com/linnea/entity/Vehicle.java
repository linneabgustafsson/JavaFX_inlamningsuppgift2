package com.linnea.entity;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;

public abstract class Vehicle implements Serializable {

    private int price;
    private String brand;
    private String vehicleType;
    private String itemNumber;

    //!!!!!!!!!!!!!!!!!!VAD ÄR DETTA
    private final BooleanProperty NAMN = new SimpleBooleanProperty(false);

        //!!!!!!!!!!!!!!!!!!
    public boolean isActive() {
        return NAMN.get();
    }
    public BooleanProperty activeProperty() {
        return NAMN;
    }
    public void setActive(boolean value) {
        NAMN.set(value);
    }
    //------------------------------------


    public Vehicle() {
    }

    public Vehicle(int price, String brand, String vehicleType, String itemNumber) {
        this.price = price;
        this.brand = brand;
        this.vehicleType = vehicleType;
        this.itemNumber = itemNumber;
    }

    public int getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public abstract void instructionManual();

    public String toString() {
        return "Fordonstyp: " + vehicleType + "\nMärke: " + brand + "\nPris: " + price + " kr/dag\nArtikelnummer: " + itemNumber + "\n";
    }
}


