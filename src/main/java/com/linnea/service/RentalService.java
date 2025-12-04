package com.linnea.service;
import com.linnea.Inventory;
import com.linnea.PricePolicy;
import com.linnea.Rental;
import com.linnea.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RentalService implements PricePolicy {

    Scanner scanner = new Scanner(System.in);

    private List<Rental> rentalList;
    private MembershipService membershipService;
    private Inventory inventory;
    private List<Rental> todaysRentals = new ArrayList<>();

    public RentalService()  {
    }

    public RentalService(MembershipService membershipService, Inventory inventory, List<Rental> rentalList)  {
        this.membershipService = membershipService;
        this.inventory = inventory;
        this.rentalList = rentalList;
    }

    public List<Rental> getRentalList() {
        return rentalList;
    }

    public void setMemberList(List<Rental> rentalList) {
        this.rentalList = rentalList;
    }

    public List<Rental> getTodaysRentals()  {
        return todaysRentals;
    }

    public void setTodaysRentals()  {
        this.todaysRentals = todaysRentals;
    }

    public void printListAllItems()    {

        for (Vehicle vehicle : inventory.getInventoryList())  {
            System.out.println(vehicle);
        }
    }

    //Strömmar...
    public List<Trailer> filterItemsEndastTrailersDEN_NYA() {

        List<Trailer> allTrailers = new ArrayList<>();

        for (Vehicle vehicle : inventory.getInventoryList()) {

            if (vehicle instanceof Trailer)  {
                allTrailers.add((Trailer) vehicle);
            }
        }

        return allTrailers;
    }

    public void filterAndSearchItemDEN_GAMLA()   {
//
//        if (userInputProductType.equalsIgnoreCase("släpvagn")) {
//            System.out.println("Vi har följande släpvagnar:\n");
//
//            for (Item item : inventory.getInventoryList())    {
//                if (item instanceof Trailer)    {
//                    System.out.println("🔸Märke: " + item.getBrand() + ". Pris: "+ item.getPrice() + " kr/dag. Artikelnummer: "
//                            + item.getItemNumber() + ". Längd: " + ((Trailer) item).getLength() + " cm. Bredd: " +
//                            ((Trailer) item).getWidth() + " cm.");
//                }
//            }
//        }
//
//        else if (userInputProductType.equalsIgnoreCase("gräsklippare")) {
//            System.out.println("Vi har både åkgräsklippare och robotgräsklippare.\n");
//
//            System.out.println("Här är våra åkgräsklippare: ");
//            for (Item item : inventory.getInventoryList()) {
//                if (item instanceof RideOnLawnMower) {
//
//                    System.out.println("🔸 Märke: " + item.getBrand() + "" +
//                            ". Pris: " + item.getPrice() + " kr/dag. Artikelnummer: "
//                            + item.getItemNumber() + ". Vikt: " + ((LawnMower) item).getWeight() + " kg.");
//                }
//            }
//
//            System.out.println("\nHär är våra robotgräsklippare: ");
//            for (Item item : inventory.getInventoryList())    {
//                    if (item instanceof RoboticLawnMower) {
//
//                        System.out.println("🔸 Märke: " + item.getBrand() + "" +
//                                ". Pris: " + item.getPrice() + " kr/dag. Artikelnummer: "
//                                + item.getItemNumber() + ". Vikt: " + ((LawnMower) item).getWeight() + " kg. Kapacitet: "
//                                + ((RoboticLawnMower) item).getLawnSize() + " m2.");
//                    }
//            }
//        }
    }

    public void bookItemDEN_NYA(Vehicle chosenVehicle, int chosenNumberOfDays, Member chosenMember)   {
        rentalList.add(new Rental(chosenVehicle, chosenNumberOfDays, chosenMember));
        todaysRentals.add(new Rental(chosenVehicle, chosenNumberOfDays, chosenMember));

        chosenMember.getOrderHistory().add(new Rental(chosenVehicle, chosenNumberOfDays, chosenMember));

    }

    public void bookItemDENGAMLA()  {
        System.out.println("📝 Här registrerar du medlemmens bokning. Börja med att söka fram rätt medlem.\n");
        Member chosenMember = membershipService.searchForMember();

        System.out.println("\nNu ska du välja vilket fordon medlemmen ska hyra genom att göra en sökning.\n");
        //filterAndSearchItemDEN_GAMLA();

        Vehicle chosenVehicle;
        boolean itemNotAvailable = true;

        do {
            System.out.println("\nSkriv artikelnummer på det fordon som medlemmen vill hyra:");
            String userInputItemNumber = scanner.nextLine();
            chosenVehicle = inventory.findItem(userInputItemNumber);

            itemNotAvailable = false;

            for (Rental rental : rentalList)    {
                if (chosenVehicle == rental.getItem()) {
                    System.out.println("Fordon med artikelnummer " + userInputItemNumber + " är redan uthyrd. Välj något annat.");
                    itemNotAvailable = true;
                }
            }

            if (chosenVehicle == null) {
                System.out.println("Det finns inget fordon med det artikelnumret. Skriv in ett korrekt artikelnummer.");
                itemNotAvailable = true;
            }

        }
        while (itemNotAvailable);

        System.out.println("Skriv hur många dagar du vill hyra: ");
        int numberOfDays = scanner.nextInt();
        scanner.nextLine();

        rentalList.add(new Rental(chosenVehicle, numberOfDays, chosenMember));
        todaysRentals.add(new Rental(chosenVehicle, numberOfDays, chosenMember));

        chosenMember.getOrderHistory().add(new Rental(chosenVehicle, numberOfDays, chosenMember));

        System.out.println("Här kommer en sammanställning av bokningen:");

        System.out.println("🔸" + chosenMember.getFirstName() + " (personnummer " +
                chosenMember.getPersonalIdNumber() + ")" + " ska hyra en " +
                chosenVehicle.getVehicleType() + " (artikelnummer " +
                chosenVehicle.getItemNumber() + ") under " + numberOfDays + " dagar.\n");



   }

    public void printListOngoingRentals()    {

        if (rentalList.isEmpty())   {
            System.out.println("Vi har inga fordon uthyrda just nu.");
        }

        else {
            System.out.println("\nHär är en sammanställning av alla pågående uthyrningar: ");

            for (Rental rental : rentalList) {
                System.out.println("🔸" + rental.getMember().getFirstName() + " (personnummer " +
                        rental.getMember().getPersonalIdNumber() + ")" + " hyr en " +
                        rental.getItem().getVehicleType() + " (artikelnummer " +
                        rental.getItem().getItemNumber() + ") under " + rental.getNumberOfDays() + " dagar.");
            }
        }
   }

   public void printListTodaysRentals()    {

        if (todaysRentals.isEmpty())    {
            System.out.println("Det har inte registrerats någon uthyrning idag.");
        }

        else {
            System.out.println("\nDe uthyrningar som registrerats idag är: ");

            for (Rental rental : todaysRentals) {
                System.out.println("🔸" + rental.getMember().getFirstName() + " (personnummer " +
                        rental.getMember().getPersonalIdNumber() + ")" + " hyr en " +
                        rental.getItem().getVehicleType() + " (artikelnummer " +
                        rental.getItem().getItemNumber() + ") under " + rental.getNumberOfDays() + " dagar.");
            }
        }
   }

    public void returnItem ()    {
       System.out.println("🔄 Du har valt alternativet avsluta uthyrning och lämna tillbaka fordon.");

        printListOngoingRentals();

        if (rentalList.isEmpty())   {
            System.out.println("Du kan därför inte avsluta någon uthyrning.");
            return;
        }

        System.out.println("\nSkriv artikelnummer på det fordon som ska lämnas tillbaka:");
        String userInputItemNumber = scanner.nextLine();

        Vehicle chosenVehicle = inventory.findItem(userInputItemNumber);

        int findIndex = -1;

        for (int i = 0; i < rentalList.size(); i++) {

            Rental rental = rentalList.get(i);

            if (rental.getItem().getItemNumber().equals(userInputItemNumber)) {
                findIndex = i;
                break;
            }
        }

        Rental currentRental = rentalList.remove(findIndex);

        if (todaysRentals.isEmpty())    {
        }

        else {

            for (int i = 0; i < todaysRentals.size(); i++)  {

                Rental rental = todaysRentals.get(i);

                if (rental.getItem().getItemNumber().equals(userInputItemNumber));  {
                    findIndex = i;
                    break;
                }
            }

            Rental currentRentalToday = todaysRentals.remove(findIndex);
        }

        System.out.println("Nu är fordonet återlämnat.");
    }

    public void sumRevenue() {
        System.out.println("\n💰 Här kommer en summering av dagens intäkter.");

        if (todaysRentals.isEmpty())    {
            System.out.println("Det har inte registrerats någon uthyrning idag och intäkterna kan därför inte summeras.");
            return;
        }

        printListTodaysRentals();
        System.out.println("");

        int priceAfterDiscount;
        int finalPrice;
        ArrayList<Integer> sumupTodaysRentals = new ArrayList<Integer>();

        for (Rental rental : todaysRentals)  {

            if (rental.getMember().getMembershipLevel().equalsIgnoreCase("standard"))   {

                finalPrice = rental.getItem().getPrice() * rental.getNumberOfDays();
                sumupTodaysRentals.add(finalPrice);
            }

            else if (rental.getMember().getMembershipLevel().equalsIgnoreCase("student"))   {

                priceAfterDiscount = studentDiscount(rental.getItem().getPrice());
                finalPrice = priceAfterDiscount * rental.getNumberOfDays();
                sumupTodaysRentals.add(finalPrice);
            }

             else if (rental.getMember().getMembershipLevel().equalsIgnoreCase("premium"))   {

                 priceAfterDiscount = premiumDiscount(rental.getItem().getPrice());
                 finalPrice = priceAfterDiscount * rental.getNumberOfDays();
                 sumupTodaysRentals.add(finalPrice);
            }
        }

        int totalRevenue = 0;

        for (int i = 0; i < sumupTodaysRentals.size(); i++)   {
            totalRevenue = totalRevenue + sumupTodaysRentals.get(i);
        }

        System.out.println("De totala intäkterna för dagens uthyrningar är " + totalRevenue + " kr.");
    }

    @Override
    public int studentDiscount(int price) {
        //(int) är en casting, en konvertering så att funkar med procent vilket ju är decimaltal egentligen, en int.
        int newPriceDiscount = (int) (price * 0.8);
        return newPriceDiscount;
    }

    @Override
    public int premiumDiscount(int price) {
        int newPriceDiscount = (int) (price * 0.9);
        return newPriceDiscount;
    }
}
