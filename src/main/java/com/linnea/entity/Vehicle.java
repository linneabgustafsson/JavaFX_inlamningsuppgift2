package com.linnea.entity;

import java.io.Serializable;

public abstract class Vehicle implements Serializable {

    private int price;
    private String brand;
    private String vehicleType;
    private String itemNumber;

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

    public String toString() {
        return "Fordonstyp: " + vehicleType + "\nMärke: " + brand + "\nPris: " + price + " kr/dag\nArtikelnummer: " + itemNumber + "\n";
    }
}


