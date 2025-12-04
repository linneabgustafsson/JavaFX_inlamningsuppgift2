package com.linnea.entity;

public class RoboticLawnMower extends LawnMower {

    private int lawnSize;

    public RoboticLawnMower()   {
    }

    public RoboticLawnMower(int price, String brand, String itemNumber, int weight, int lawnSize)    {
        super(price, brand, "robotgräsklippare", itemNumber, weight);
        this.lawnSize = lawnSize;
    }

    public int getLawnSize() {
        return lawnSize;
    }

    public void setLawnSize(int lawnSize) {
        this.lawnSize = lawnSize;
    }

    @Override
    public void instructionManual() {
        System.out.println("🍀 MANUAL FÖR ROBOTGRÄSKLIPPARE 🍀\nFixa GPS-inställningarna och se till att klipparen hittar hem till sitt lilla hus.");
    }

    @Override
    public String toString() {
        return super.toString() + "  Kapacitet: " + lawnSize + " m2.\n";
    }
}