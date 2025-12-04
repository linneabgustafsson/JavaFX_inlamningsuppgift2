package com.linnea.entity;

public class RideOnLawnMower extends LawnMower {

    public RideOnLawnMower()    {
    }

    public RideOnLawnMower(int price, String brand, String itemNumber, int weight)    {
        super(price, brand, "åkgräsklippare", itemNumber, weight);
    }

    @Override
    public void instructionManual() {
        System.out.println("🌿 MANUAL FÖR ÅKGRÄSKLIPPARE 🌿\nSätt dig på åkgräsklipparen och kör iväg.");
    }
}
