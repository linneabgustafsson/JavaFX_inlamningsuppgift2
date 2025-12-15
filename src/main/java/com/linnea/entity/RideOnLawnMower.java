package com.linnea.entity;

import java.io.Serializable;

public class RideOnLawnMower extends LawnMower implements Serializable {

    public RideOnLawnMower()    {
    }

    public RideOnLawnMower(int price, String brand, String itemNumber, int weight)    {
        super(price, brand, "åkgräsklippare", itemNumber, weight);
    }

}
