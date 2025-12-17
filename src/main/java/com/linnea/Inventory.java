package com.linnea;

import com.linnea.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private ObservableList<Vehicle> inventoryObservableList = FXCollections.observableArrayList();
    private String inventoryFile;

    public Inventory() {
    }

    public Inventory(String inventoryFile) {
        this.inventoryFile = inventoryFile;
    }

    public ObservableList<Vehicle> getInventoryObservableList() {
        return inventoryObservableList;
    }

    public void setVehicleObservableList(ObservableList<Member> membersObservableList) {
        this.inventoryObservableList = inventoryObservableList;
    }

    public String getInventoryFile() {
        return inventoryFile;
    }

    public void setInventoryFile(String inventoryFile) {
        this.inventoryFile = inventoryFile;
    }

    public void initialValueInventoryList() {

        inventoryObservableList.add(new Trailer(400, "Thule", "145", 325, 152));
        inventoryObservableList.add(new Trailer(350, "Fogelsta", "117", 258, 128));
        inventoryObservableList.add(new Trailer(450, "Tikin", "250", 240, 170));
        inventoryObservableList.add(new Trailer(400, "Brenderup", "314", 200, 110));
        inventoryObservableList.add(new RideOnLawnMower(350, "Husqvarna", "125", 180));
        inventoryObservableList.add(new RideOnLawnMower(250, "Husqvarna", "215", 145));
        inventoryObservableList.add(new RideOnLawnMower(150, "Stihl", "293", 190));
        inventoryObservableList.add(new RideOnLawnMower(330, "Stiga", "274", 210));
        inventoryObservableList.add(new RoboticLawnMower(300, "Husqvarna", "205", 60, 2000));
        inventoryObservableList.add(new RoboticLawnMower(200, "Klippo", "113", 75, 1000));
        inventoryObservableList.add(new RoboticLawnMower(190, "Gardena", "187", 55, 1100));
        inventoryObservableList.add(new RoboticLawnMower(350, "Bosch", "290", 90, 3500));
    }

    public void writeToFileInventory() {

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(inventoryFile, false))) {

            objectOutputStream.writeObject(new ArrayList<>(inventoryObservableList));
        } catch (IOException e) {
            System.out.println("Fungerade inte att skapa fil " + e.getMessage());
        }
    }

    public void readFromFileInventory(String inventoryFile) {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(inventoryFile))) {

            List<Vehicle> loaded = (List<Vehicle>) objectInputStream.readObject();
            inventoryObservableList.setAll(loaded);
        } catch (IOException | ClassNotFoundException e) {

            System.out.println("Om fel vid läsning av fil " + e.getMessage());
        }
    }

    public void addVehicle(Vehicle vehicle) {
        inventoryObservableList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        inventoryObservableList.remove(vehicle);
    }

    public Vehicle findVehicle(String userInput) {

        for (Vehicle vehicle : inventoryObservableList) {
            if (vehicle.getItemNumber().equalsIgnoreCase(userInput) ||
                    vehicle.getBrand().equalsIgnoreCase(userInput) ||
                    vehicle.getVehicleType().equalsIgnoreCase(userInput)) {

                return vehicle;
            }
        }
        return null;
    }

    public Vehicle findVehicleByItemNr(String itemNr) {

        for (Vehicle vehicle : inventoryObservableList) {
            if (vehicle.getItemNumber().equalsIgnoreCase(itemNr)) {
                return vehicle;
            }
        }

        return null;
    }

}




