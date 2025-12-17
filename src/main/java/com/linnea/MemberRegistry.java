package com.linnea;

import com.linnea.entity.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRegistry {

    private ObservableList<Member> membersObservableList = FXCollections.observableArrayList();
    private String fileMemberRegistry;

    public MemberRegistry() {
    }

    public MemberRegistry(String fileMemberRegistry) {
        this.fileMemberRegistry = fileMemberRegistry;
    }

    public ObservableList<Member> getMembersObservableList() {
        return membersObservableList;
    }

    public void setMembersObservableList(ObservableList<Member> membersObservableList) {
        this.membersObservableList = membersObservableList;
    }

    public String getFileMemberRegistry() {
        return fileMemberRegistry;
    }

    public void setFileMemberRegistry(String fileMemberRegistry) {
        this.fileMemberRegistry = fileMemberRegistry;
    }

    public void initialValueMemberRegistry() {

        membersObservableList.add(new Member("851103-7841", "Eja", "Bylund", "Premium"));
        membersObservableList.add(new Member("050119-8765", "Emma", "Hagman", "Student"));
        membersObservableList.add(new Member("750110-7888", "Sara", "Stenlund", "Standard"));
        membersObservableList.add(new Member("020705-7465", "Jennie", "Larsson", "Student"));
        membersObservableList.add(new Member("890305-7458", "Emil", "Gustafsson", "Premium"));
        membersObservableList.add(new Member("670830-1538", "Lars", "Svensson", "Student"));
        membersObservableList.add(new Member("751220-1258", "Gottfrid", "Dalgren", "Standard"));
        membersObservableList.add(new Member("571127-1874", "Berit", "Bengtsson", "Standard"));
        membersObservableList.add(new Member("930508-5482", "Ulla", "Nilsson", "Premium"));
        membersObservableList.add(new Member("990715-6852", "Smilla", "Gran", "Student"));
    }

    public void writeToFileMemberRegistry() {

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileMemberRegistry, false))) {

            objectOutputStream.writeObject(new ArrayList<>(membersObservableList));
        }

        catch (IOException e) {
            System.out.println("Fungerade inte att skapa fil " + e.getMessage());
        }
    }

    public void readFromFileMemberRegistry(String fileMemberRegistry) {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileMemberRegistry))) {

        List<Member> loaded = (List<Member>) objectInputStream.readObject();
        membersObservableList.setAll(loaded);
        }

        catch (IOException | ClassNotFoundException e)  {

            System.out.println("Om fel vid läsning av fil " + e.getMessage());
        }
    }

    public void addMemberToRegistry(Member member) {
        membersObservableList.add(member);
    }

    public void removeMemberFromRegistry(Member member) {
        membersObservableList.remove(member);
    }


}
