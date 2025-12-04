package com.linnea;

import com.linnea.entity.Vehicle;
import java.util.List;

public class Inventory {

    private List<Vehicle> inventoryList;

    public Inventory() {
    }

    public Inventory(List<Vehicle> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<Vehicle> getInventoryList()  {
        return inventoryList;
    }

    public void setInventoryList(List<Vehicle> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public Vehicle findItem(String userInputItem) {
        for (Vehicle vehicle : inventoryList)    {
            if (vehicle.getItemNumber().equalsIgnoreCase(userInputItem))   {
                return vehicle;
            }
        }

        return null;
    }
}
