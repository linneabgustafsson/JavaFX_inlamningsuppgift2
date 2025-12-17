package com.linnea.entity;

import java.io.Serializable;

public abstract class LawnMower extends Vehicle implements Serializable {

    private int weight;

    public LawnMower()  {
    }

    public LawnMower(int price, String brand, String itemType, String itemNumber, int weight)   {
        super(price, brand, itemType, itemNumber);
        this.weight = weight;
    }

    public int getWeight()  {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return super.toString() + "Vikt: " + weight + " kg\n";
    }
}
