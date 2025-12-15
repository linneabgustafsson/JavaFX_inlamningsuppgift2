package com.linnea.service;

import com.linnea.MemberRegistry;
import com.linnea.Rental;
import com.linnea.entity.Member;
import java.util.Scanner;

public class MembershipService {

    Scanner scanner = new Scanner(System.in);

    private MemberRegistry memberRegistry;


    public MembershipService()  {
    }

    public MembershipService(MemberRegistry memberRegistry){
        this.memberRegistry = memberRegistry;
    }



    public void printMemberRegistry()   {
//        System.out.println("\nHär är vårt medlemsregister: ");
//
//        for (Member member : memberRegistry.getMemberList()) {
//            System.out.println(member);
//        }

    }

    public void printOrderHistory()  {

        for (Member m : memberRegistry.getMembersObservableList()) {
            System.out.println("\nOrderhistorik för " + m.getFirstName() + " " + m.getLastName() + ":");

            if (m.getOrderHistory().isEmpty()) {
                System.out.println("(Ingen orderhistorik)");
                continue;
            }

            for (Rental rental : m.getOrderHistory()) {
                System.out.println("  " + rental);
            }
        }
    }

    public Member searchMemberNY(String userInputSearch)    {

        for (Member member : memberRegistry.getMembersObservableList()) {
            if (member.getPersonalIdNumber().equalsIgnoreCase(userInputSearch) ||
                    member.getFirstName().equalsIgnoreCase(userInputSearch) ||
                    member.getLastName().equalsIgnoreCase(userInputSearch)) {

               return member;
            }
        }

        return null;
    }

    public void changeMemberInfo()  {
        System.out.print("✏️ Här kan du ändra uppgifter om en medlem.\nBörja med att söka fram rätt medlem.\n\n");

        //Member chosenMember = searchForMember();
        boolean incorrectInput = true;

        do {
            System.out.println("\nSkriv om du vill ändra personnummer, förnamn, efternamn eller medlemsnivå: ");
            String userInputWhatChange = scanner.nextLine();

            if (userInputWhatChange.equalsIgnoreCase("personnummer"))   {
                System.out.println("Skriv det nya personnumret i en följd (12 siffror), utan bindestreck eller mellanslag:");
                String changedPersonalIdNr = scanner.nextLine();
               // chosenMember.setPersonalIdNumber(changedPersonalIdNr);
                incorrectInput = false;
            }

            else if (userInputWhatChange.equalsIgnoreCase("förnamn")) {
                System.out.println("Skriv det nya förnamnet: ");
                String changedFirstName = scanner.nextLine();
               // chosenMember.setFirstName(changedFirstName);
                incorrectInput = false;
            }

            else if (userInputWhatChange.equalsIgnoreCase("efternamn")) {
                System.out.println("Skriv det nya efternamnet: ");
                String changedLastName = scanner.nextLine();
               // chosenMember.setLastName(changedLastName);
                incorrectInput = false;
            }

            else if (userInputWhatChange.equalsIgnoreCase("medlemsnivå")) {
                System.out.println("Skriv den nya medlemsnivån: ");
                String changedMembershipLevel = scanner.nextLine();
               // chosenMember.setMembershipLevel(changedMembershipLevel);
                incorrectInput = false;
            }

            else {
                System.out.println("❌ Du skrev fel, du måste ange om du vill ändra personnummer, förnamn, efternamn eller medlemsnivå. Prova igen.");
                incorrectInput = true;
            }

        } while (incorrectInput);

        //System.out.println("Här är medlemmens uppgifter efter dina ändringar:\n" + chosenMember);
    }

}