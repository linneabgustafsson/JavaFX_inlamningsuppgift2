package com.linnea.service;
import com.linnea.Inventory;
import com.linnea.PricePolicy;
import com.linnea.Rental;
import com.linnea.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RentalService implements PricePolicy {

    private MembershipService membershipService;
    private Inventory inventory;

    private String rentalListFile;

    private List<Rental> todaysRentals = new ArrayList<>();
    private ArrayList<Integer> sumupTodaysRentals = new ArrayList<Integer>();
    private ObservableList<Rental> rentalsObservableList = FXCollections.observableArrayList();

    public RentalService()  {
    }

    public RentalService(MembershipService membershipService, Inventory inventory, String rentalListFile)  {
        this.membershipService = membershipService;
        this.inventory = inventory;
        this.rentalListFile = rentalListFile;
    }

    public ObservableList<Rental> getRentalsObservableList() {
        return rentalsObservableList;
    }

    public void setRentalsObservableList(ObservableList<Rental> rentalsObservableList) {
        this.rentalsObservableList = rentalsObservableList;
    }

    public String getRentalListFile() {
        return rentalListFile;
    }

    public void setRentalListFile(String rentalListFile) {
        this.rentalListFile = rentalListFile;
    }

    public List<Rental> getTodaysRentals()  {
        return todaysRentals;
    }

    public void setTodaysRentals()  {
        this.todaysRentals = todaysRentals;
    }

    public ArrayList<Integer> getSumupTodaysRentals() {
        return sumupTodaysRentals;
    }

    public void setSumupTodaysRentals()  {
        this.sumupTodaysRentals = sumupTodaysRentals;
    }

    public void initialValueRentalList() {

        Member member1 = membershipService.findMember("Emma");
        Vehicle vehicle1 = inventory.findVehicleByItemNr("205");
        Rental rental1 = new Rental(vehicle1, 5, member1);
        rentalsObservableList.add(rental1);
        member1.addRentalToHistory(rental1);

        Member member2 = membershipService.findMember("Emil");
        Vehicle vehicle2 = inventory.findVehicleByItemNr("125");
        Rental rental2 = new Rental(vehicle2, 8, member2);
        rentalsObservableList.add(rental2);
        member2.addRentalToHistory(rental2);

        Member member3 = membershipService.findMember("Smilla");
        Vehicle vehicle3 = inventory.findVehicleByItemNr("145");
        Rental rental3 = new Rental(vehicle3, 14, member3);
        rentalsObservableList.add(rental3);
        member3.addRentalToHistory(rental3);

        Member member4 = membershipService.findMember("Eja");
        Vehicle vehicle4 = inventory.findVehicleByItemNr("117");
        Rental rental4 = new Rental(vehicle4, 14, member4);
        rentalsObservableList.add(rental4);
        member4.addRentalToHistory(rental4);
    }

    public void writeToFileRentals() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(rentalListFile, false))) {

            out.writeObject(new ArrayList<>(rentalsObservableList));
        }

        catch (IOException e) {
            System.out.println("Fungerade inte att skapa fil " + e.getMessage());
        }
    }

    public void readFromFileRentals(String rentalListFile) {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(rentalListFile))) {

        List<Rental> loaded = (List<Rental>) objectInputStream.readObject();
        rentalsObservableList.setAll(loaded);
        }

        catch (IOException | ClassNotFoundException e)  {

            System.out.println("Om fel vid läsning av fil " + e.getMessage());
        }
    }

    public boolean checkIfRentedOut(Vehicle vehicle) {

        for (Rental rental : rentalsObservableList) {
            if (vehicle.getItemNumber().equals(rental.getItem().getItemNumber())) {
                return true;
            }
        }

        return false;
    }

    public Rental addBooking(Vehicle chosenVehicle, int chosenNumberOfDays, Member chosenMember)   {

        Rental rental = new Rental(chosenVehicle, chosenNumberOfDays, chosenMember);

        rentalsObservableList.add(rental);
        todaysRentals.add(rental);
        chosenMember.addRentalToHistory(rental);
        return rental;
    }

    public int calculatePrice(Rental rental)  {

        int finalPrice;
        int priceAfterDiscount;

        if (rental.getMember().getMembershipLevel().equalsIgnoreCase("student")) {

            int standardPrice = rental.getItem().getPrice();

            priceAfterDiscount = studentDiscount(standardPrice);
            finalPrice = priceAfterDiscount * rental.getNumberOfDays();
            sumupTodaysRentals.add(finalPrice);
            return finalPrice;
        }

        if (rental.getMember().getMembershipLevel().equalsIgnoreCase("premium")) {

            priceAfterDiscount = premiumDiscount(rental.getItem().getPrice());
            finalPrice = priceAfterDiscount * rental.getNumberOfDays();
            sumupTodaysRentals.add(finalPrice);
            return finalPrice;
        }

        finalPrice = rental.getItem().getPrice() * rental.getNumberOfDays();
        sumupTodaysRentals.add(finalPrice);
        return finalPrice;
    }

    public int calculateTodaysRevenue() {

        int totalRevenue = 0;

        if (todaysRentals.isEmpty())    {
            return 0;
        }

        for (int i = 0; i < sumupTodaysRentals.size(); i++)   {
            totalRevenue = totalRevenue + sumupTodaysRentals.get(i);
        }

        return totalRevenue;
    }

    public void returnItem(Rental rental) {
        rentalsObservableList.remove(rental);
        todaysRentals.remove(rental);
    }

    @Override
        public int studentDiscount(int price) {
        int newPriceDiscount = (int) (price * 0.8);
        return newPriceDiscount;
    }

    @Override
    public int premiumDiscount(int price) {
        int newPriceDiscount = (int) (price * 0.9);
        return newPriceDiscount;
    }
}


