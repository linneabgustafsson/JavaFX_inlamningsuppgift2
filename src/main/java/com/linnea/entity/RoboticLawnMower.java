package com.linnea.entity;

import java.io.Serializable;

public class RoboticLawnMower extends LawnMower implements Serializable {

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
    public String toString() {
        return super.toString() + "  Kapacitet: " + lawnSize + " m2.\n";
    }
}