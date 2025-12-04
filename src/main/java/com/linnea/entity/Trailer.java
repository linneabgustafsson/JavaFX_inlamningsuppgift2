package com.linnea.entity;

public class Trailer extends Vehicle {

    private int length;
    private int width;

    public Trailer()    {
    }

    public Trailer(int price, String brand, String itemNumber, int length, int width) {
        super(price, brand, "släpvagn", itemNumber);
        this.length = length;
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void instructionManual() {
        System.out.println("🚘 MANUAL FÖR SLÄPVAGN 🚘\nSätt fast släpvagnen på bilens dragkrok.");
    }

    @Override
    public String toString() {
        return super.toString() + "Längd: " + length  + " cm\nBredd: " + width + " cm";
    }
}
