package com.linnea.service;

import com.linnea.MemberRegistry;
import com.linnea.entity.Member;
import com.linnea.entity.Trailer;
import com.linnea.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MembershipService {

    Scanner scanner = new Scanner(System.in);

    private MemberRegistry memberRegistry;

    public MembershipService()  {
    }

    public MembershipService(MemberRegistry variabelNamnParameterKonstruktor){
        this.memberRegistry = variabelNamnParameterKonstruktor;
    }

    public void printMemberRegistry()   {
//        System.out.println("\nHär är vårt medlemsregister: ");
//
//        for (Member member : memberRegistry.getMemberList()) {
//            System.out.println(member);
//        }

    }

    public List<Member> returnListaMedlemmar_NY() {

        List<Member> memberList = new ArrayList<>();

          for (Member member : memberRegistry.getMemberList()) {
              memberList.add(member);
          }

          return memberList;
//        List<Trailer> allTrailers = new ArrayList<>();
//        for (Vehicle vehicle : inventory.getInventoryList()) {
//
//            if (vehicle instanceof Trailer) {
//                allTrailers.add((Trailer) vehicle);
//            }
//        }
//        return allTrailers;
    }

    public void addMember()    {
        System.out.println("🆕 Här lägger du till medlemmar i vårt register.\n");

        boolean addAnotherMember = true;

        do {
            System.out.println("Skriv medlemmens hela personnummer i en följd (12 siffror), utan bindestreck eller mellanslag:");
            String userInputIDNumber = scanner.nextLine();
            System.out.println("Skriv medlemmens förnamn: ");
            String userInputFirstName = scanner.nextLine();
            System.out.println("Skriv medlemmens efternamn: ");
            String userInputLastName = scanner.nextLine();

            String userInputMembershipLevel;
            boolean wrongInputMembershipLevel = true;

            do {
                System.out.println("Skriv medlemsnivå (standard, student eller premium): ");
                userInputMembershipLevel = scanner.nextLine();

                if (userInputMembershipLevel.equalsIgnoreCase("standard") ||
                        userInputMembershipLevel.equalsIgnoreCase("student") ||
                        userInputMembershipLevel.equalsIgnoreCase("premium")) {

                    wrongInputMembershipLevel = false;

                }

                else {
                    System.out.println("❌ Du angav inte en korrekt medlemsnivå. Prova igen.");
                }
            }
            while (wrongInputMembershipLevel);

            Member addedMember = new Member(userInputIDNumber, userInputFirstName, userInputLastName, userInputMembershipLevel);
            memberRegistry.addMemberToRegistry(addedMember);

            System.out.println("Nu är följande medlem registrerad i vårt register:\n" + addedMember);


        }
        while (addAnotherMember);
    }

    public Member searchForMember()   {

        boolean memberNotFound = true;
        boolean incorrectInput = true;
        Member member = null;

        do {

            do {
                System.out.println("🔎 Skriv om du vill söka medlemmen utifrån hens personnummer, förnamn eller efternamn: ");
                String userInputHowToSearch = scanner.nextLine();

                if (userInputHowToSearch.equalsIgnoreCase("personnummer"))   {
                    System.out.println("Skriv medlemmens hela personnummer i en följd (12 siffror), utan bindestreck eller mellanslag:");
                    incorrectInput = false;
                }

                else if (userInputHowToSearch.equalsIgnoreCase("förnamn")) {
                    System.out.println("Skriv medlemmens förnamn: ");
                    incorrectInput = false;
                }

                else if (userInputHowToSearch.equalsIgnoreCase("efternamn")) {
                    System.out.println("Skriv medlemmens efternamn: ");
                    incorrectInput = false;
                }

                else {
                    System.out.println("❌ Du skrev fel, du måste ange om du vill söka utifrån personnummer, förnamn" +
                            " eller efternamn. Prova igen.\n");
                    incorrectInput = true;
                }

            }
            while (incorrectInput);

            String userInputChoice = scanner.nextLine();
            member = memberRegistry.findMember(userInputChoice);

            if (member == null)    {
                System.out.println("Den medlemmen finns inte i registret. Gör om din sökning.\n ");
                memberNotFound = true;
            }

            else {
                System.out.println("Här kommer uppgifter om den medlemmen:\n" + member);
                memberNotFound = false;
            }
        }
        while (memberNotFound);

        return member;
    }

    public void changeMemberInfo()  {
        System.out.print("✏️ Här kan du ändra uppgifter om en medlem.\nBörja med att söka fram rätt medlem.\n\n");

        Member chosenMember = searchForMember();
        boolean incorrectInput = true;

        do {
            System.out.println("\nSkriv om du vill ändra personnummer, förnamn, efternamn eller medlemsnivå: ");
            String userInputWhatChange = scanner.nextLine();

            if (userInputWhatChange.equalsIgnoreCase("personnummer"))   {
                System.out.println("Skriv det nya personnumret i en följd (12 siffror), utan bindestreck eller mellanslag:");
                String changedPersonalIdNr = scanner.nextLine();
                chosenMember.setPersonalIdNumber(changedPersonalIdNr);
                incorrectInput = false;
            }

            else if (userInputWhatChange.equalsIgnoreCase("förnamn")) {
                System.out.println("Skriv det nya förnamnet: ");
                String changedFirstName = scanner.nextLine();
                chosenMember.setFirstName(changedFirstName);
                incorrectInput = false;
            }

            else if (userInputWhatChange.equalsIgnoreCase("efternamn")) {
                System.out.println("Skriv det nya efternamnet: ");
                String changedLastName = scanner.nextLine();
                chosenMember.setLastName(changedLastName);
                incorrectInput = false;
            }

            else if (userInputWhatChange.equalsIgnoreCase("medlemsnivå")) {
                System.out.println("Skriv den nya medlemsnivån: ");
                String changedMembershipLevel = scanner.nextLine();
                chosenMember.setMembershipLevel(changedMembershipLevel);
                incorrectInput = false;
            }

            else {
                System.out.println("❌ Du skrev fel, du måste ange om du vill ändra personnummer, förnamn, efternamn eller medlemsnivå. Prova igen.");
                incorrectInput = true;
            }

        } while (incorrectInput);

        System.out.println("Här är medlemmens uppgifter efter dina ändringar:\n" + chosenMember);
    }
}