package com.linnea;

import com.linnea.entity.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Vehicle> inventoryList;
    private String inventoryFile;

    public Inventory() {
    }

    public Inventory(String inventoryFile) {
        this.inventoryFile = inventoryFile;
        this.inventoryList = new ArrayList<>();
    }

    public List<Vehicle> getInventoryList()  {
        return inventoryList;
    }

    public void setInventoryList(List<Vehicle> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public String getInventoryFile() {
        return inventoryFile;
    }

    public void setInventoryFile(String inventoryFile) {
        this.inventoryFile = inventoryFile;
    }

    public void initialValueInventoryList() {

        inventoryList.add(new Trailer(400, "Thule", "145", 325, 152));
        inventoryList.add(new Trailer(350, "Fogelsta", "117", 258, 128));
        inventoryList.add(new Trailer(450, "Tikin", "250", 240, 170));
        inventoryList.add(new Trailer(400, "Brenderup", "314", 200, 110));
        inventoryList.add(new RideOnLawnMower(350, "Husqvarna", "125", 180));
        inventoryList.add(new RideOnLawnMower(250, "Husqvarna", "215", 145));
        inventoryList.add(new RoboticLawnMower(300, "Husqvarna", "205", 60, 2000));
        inventoryList.add(new RoboticLawnMower(200, "Klippo", "113", 75, 1000));
    }

    public void writeToFileInventory() {
        try (ObjectOutputStream skapaFilObjekt = new ObjectOutputStream(new FileOutputStream(inventoryFile, false))) {

            skapaFilObjekt.writeObject(inventoryList);
        }

        catch (IOException e) {
            System.out.println("Hamnar här om fel vid läsning av fil" + e.getMessage());
        }
    }

    public void readFromFileMemberRegistry(String fileMemberRegistry) {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileMemberRegistry))) {

            inventoryList = (List<Vehicle>) objectInputStream.readObject();
        }

        catch (IOException | ClassNotFoundException e) {

            System.out.println("Hamnar här om fel vid läsning av fil" + e.getMessage());
        }
    }

    public void addVehicle(Vehicle vehicle) {
        inventoryList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        inventoryList.remove(vehicle);
    }

    //Ska den här metoden vara här?
    public Vehicle findVehicleByItemNr(String itemNr) {
        for (Vehicle vehicle : inventoryList) {
            if (vehicle.getItemNumber().equalsIgnoreCase(itemNr)) {
                return vehicle;
            }
        }

        return null;
    }

}




