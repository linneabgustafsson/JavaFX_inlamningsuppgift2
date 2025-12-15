package com.linnea.service;
import com.linnea.Inventory;
import com.linnea.PricePolicy;
import com.linnea.Rental;
import com.linnea.entity.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RentalService implements PricePolicy {

    private MembershipService membershipService;
    private Inventory inventory;

    private String rentalListFile;
    private List<Rental> rentalList;
    private List<Rental> todaysRentals = new ArrayList<>();
    private ArrayList<Integer> sumupTodaysRentals = new ArrayList<Integer>();

    public RentalService()  {
    }

    public RentalService(MembershipService membershipService, Inventory inventory, String rentalListFile)  {
        this.membershipService = membershipService;
        this.inventory = inventory;
        this.rentalListFile = rentalListFile;
        this.rentalList = new ArrayList<>();
    }

    public List<Rental> getRentalList() {
        return rentalList;
    }

    public void setRentalList(List<Rental> rentalList) {
        this.rentalList = rentalList;
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

        Member member1 = membershipService.searchMemberNY("Emma");
        Vehicle vehicle1 = inventory.findVehicleByItemNr("205");
        Rental rental1 = new Rental(vehicle1, 5, member1);
        rentalList.add(rental1);
        member1.addRentalToHistory(rental1);

        Member member2 = membershipService.searchMemberNY("Emil");
        Vehicle vehicle2 = inventory.findVehicleByItemNr("125");
        Rental rental2 = new Rental(vehicle2, 8, member2);
        rentalList.add(rental2);
        member2.addRentalToHistory(rental2);

        Member member3 = membershipService.searchMemberNY("Smilla");
        Vehicle vehicle3 = inventory.findVehicleByItemNr("145");
        Rental rental3 = new Rental(vehicle3, 14, member3);
        rentalList.add(rental3);
        member3.addRentalToHistory(rental3);

        Member member4 = membershipService.searchMemberNY("Eja");
        Vehicle vehicle4 = inventory.findVehicleByItemNr("117");
        Rental rental4 = new Rental(vehicle4, 14, member4);
        rentalList.add(rental4);
        member4.addRentalToHistory(rental4);
    }

    public void writeToFileRentals() {
        try (ObjectOutputStream skapaFilObjekt = new ObjectOutputStream(new FileOutputStream(rentalListFile, false))) {

            skapaFilObjekt.writeObject(rentalList);
        }

        catch (IOException e) {
            System.out.println("Hamnar här om fel vid läsning av fil" + e.getMessage());
        }
    }

    public void readFromFileRentals(String rentalListFile) {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(rentalListFile))) {

            rentalList = (List<Rental>) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {

            System.out.println("Hamnar här om fel vid läsning av fil" + e.getMessage());
        }
    }

    public List<Trailer> filterItemsEndastTrailersDEN_NYA() {

        List<Trailer> allTrailers = new ArrayList<>();

        for (Vehicle vehicle : inventory.getInventoryList()) {

            if (vehicle instanceof Trailer)  {
                allTrailers.add((Trailer) vehicle);
            }
        }

        return allTrailers;
    }

    public boolean checkIfRentedOut(Vehicle vehicle) {

        for (Rental rental : rentalList) {
            if (vehicle.getItemNumber().equals(rental.getItem().getItemNumber())) {
                return true;
            }
        }

        return false;
    }

    public Rental addBooking(Vehicle chosenVehicle, int chosenNumberOfDays, Member chosenMember)   {

        Rental rental = new Rental(chosenVehicle, chosenNumberOfDays, chosenMember);

        rentalList.add(rental);
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

    public void returnItemNYA(Rental rental) {

        rentalList.remove(rental);
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
    public void returnItem (Rental rental)    {

//        rentalList.remove(rental);
//        todaysRentals.remove(rental);
//        printLisTOngoingRentals();
//
//
//        System.out.println("\nSkriv artikelnummer på det fordon som ska lämnas tillbaka:");
//        String userInputItemNumber = scanner.nextLine();
//
//        Vehicle chosenVehicle = inventory.findVehicle(userInputItemNumber);
//        int findIndex = -1;
//
//        for (int i = 0; i < rentalList.size(); i++) {
//
//            //Rental rental = rentalList.get(i);
//
//            if (rental.getItem().getItemNumber().equals(userInputItemNumber)) {
//                findIndex = i;
//                break;
//            }
//        }
//        Rental currentRental = rentalList.remove(findIndex);
//
//        if (todaysRentals.isEmpty())    {
//        }
//
//        else {
//
//            for (int i = 0; i < todaysRentals.size(); i++)  {
//
//                Rental rental = todaysRentals.get(i);
//
//                if (rental.getItem().getItemNumber().equals(userInputItemNumber));  {
//                    findIndex = i;
//                    break;
//                }
//            }
//            Rental currentRentalToday = todaysRentals.remove(findIndex);
//        }
//        System.out.println("Nu är fordonet återlämnat.");
    }
    public void filterAndSearchItemDEN_GAMLA()   {

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
    public void bookItemDENGAMLA()  {

//        Vehicle chosenVehicle;
//        boolean itemNotAvailable = true;
//
//        do {
//            System.out.println("\nSkriv artikelnummer på det fordon som medlemmen vill hyra:");
//            String userInputItemNumber = scanner.nextLine();
//            chosenVehicle = inventory.findVehicleByItemNr(userInputItemNumber);
//
//            itemNotAvailable = false;
//
//            for (Rental rental : rentalList)    {
//                if (chosenVehicle == rental.getItem()) {
//                    System.out.println("Fordon med artikelnummer " + userInputItemNumber + " är redan uthyrd. Välj något annat.");
//                    itemNotAvailable = true;
//                }
//            }
//            if (chosenVehicle == null) {
//                System.out.println("Det finns inget fordon med det artikelnumret. Skriv in ett korrekt artikelnummer.");
//                itemNotAvailable = true;
//            }
//        }
//        while (itemNotAvailable);
   }

}


