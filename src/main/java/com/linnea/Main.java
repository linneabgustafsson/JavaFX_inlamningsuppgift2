package com.linnea;

import com.linnea.entity.*;
import com.linnea.service.MembershipService;
import com.linnea.service.RentalService;
import com.linnea.util.UIStyler;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Main extends Application {

    UIStyler uiStyler = new UIStyler();

    MemberRegistry memberRegistry;
    MembershipService membershipService;
    Inventory inventory;
    RentalService rentalService;
    Rental rental;

    String memberRegistryFile;
    String inventoryListFile;
    String rentalListFile;

    int numberOfDays;
    Vehicle vehicle;
    Member member;

    Label memberRemovedLabel = new Label();
    TextFlow memberInMemberRegistryTextFlow = new TextFlow();

    boolean openMemberRegistryFromBookWindow = false;
    boolean openMemberFormFromAddMemberWindow = false;

    boolean openVehicleRegistryFromBookWindow = false;
    boolean openVehicleFormFromAddVehicleWindow = false;

    LocalDate fromDate;
    LocalDate toDate;

    int finalPrice;

    Scene previousMemberScene;
    String previousMemberHeadline;
    Scene previousVehicleScene;
    String previousVehicleHeadLine;

    ObservableList<Member> membersObservableList;
    TableView<Member> memberTableView;
    Member chosenMemberInRegistryHighlight = null;
    
    ObservableList<Vehicle> inventoryObservableList;
    Vehicle chosenVehicleInRegistryHighlight = null;

    ObservableList<Rental> rentalsObservableList;
    Rental chosenRentalInRegistryHighlight = null;

    @Override
    public void start(Stage stage) throws Exception {
        readFiles();
        mainMenuWindow(stage);
    }

    public void readFiles()    {

        memberRegistryFile = "filMemberRegistry.ser";
        memberRegistry = new MemberRegistry(memberRegistryFile);
        memberRegistry.readFromFileMemberRegistry(memberRegistryFile);

        if (memberRegistry.getMembersObservableList().isEmpty()) {
            memberRegistry.initialValueMemberRegistry();
            memberRegistry.writeToFileMemberRegistry();
        }

        membershipService = new MembershipService(memberRegistry);

        inventoryListFile = "inventoryListFile.ser";
        inventory = new Inventory(inventoryListFile);
        inventory.readFromFileInventory(inventoryListFile);

        if (inventory.getInventoryObservableList().isEmpty()) {
            inventory.initialValueInventoryList();
            inventory.writeToFileInventory();
        }

        rentalListFile = "rentalListFile.ser";
        rentalService = new RentalService(membershipService, inventory, rentalListFile);
        rentalService.readFromFileRentals(rentalListFile);

        if (rentalService.getRentalsObservableList().isEmpty()) {
            rentalService.initialValueRentalList();
            rentalService.writeToFileRentals();
        }

        //************ORDERHISTORIK*******************
        Member member1 = membershipService.findMember("Lars");
        Vehicle vehicle1 = inventory.findVehicleByItemNr("125");
        Rental rental1 = new Rental(vehicle1, 15, member1);
        member1.getOrderHistory().add(rental1);

        Member member2 = membershipService.findMember("Eja");
        Vehicle vehicle2 = inventory.findVehicleByItemNr("250");
        Rental rental2 = new Rental(vehicle2, 20, member2);
        member2.getOrderHistory().add(rental2);

        Member member3 = membershipService.findMember("Ulla");
        Vehicle vehicle3 = inventory.findVehicleByItemNr("113");
        Rental rental3 = new Rental(vehicle3, 5, member3);
        member3.getOrderHistory().add(rental3);
    }

    public void mainMenuWindow(Stage stage) {
        stage.setTitle("HUVUDMENY");

        Label headingLabel = new Label("SHARED VEHICLES");
        headingLabel.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 30));
        headingLabel.setStyle("-fx-text-fill: #3A5A50");

        Label headingLabel2 = new Label("uthyrning av fordon");
        headingLabel2.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 22));
        headingLabel2.setStyle("-fx-text-fill: #3A5A50");

        Label topLineLabel = new Label("─────────────────────────────────");
        topLineLabel.setStyle("-fx-text-fill: #3A5A50");

        Label memberHandlingLabel = new Label("Hantera medlem");
        uiStyler.styleMainMenuLabels(memberHandlingLabel);

        Button addMemberButton = new Button();
        addMemberButton.setText("Lägg till medlem");
        uiStyler.styleDropdownMenuButton(addMemberButton);
        addMemberButton.setOnAction(e -> {
            openMemberFormFromAddMemberWindow = false;
            addOrChangeMemberWindow(stage);
        });

        Button memberRegistryButton = new Button();
        memberRegistryButton.setText("Sök, ändra eller ta bort medlem");
        uiStyler.styleDropdownMenuButton(memberRegistryButton);
        memberRegistryButton.setOnAction(e -> memberRegistryChangeDelete());

        ComboBox <Button> memberComboBox = new ComboBox();
        memberComboBox.getItems().addAll(addMemberButton, memberRegistryButton);

        Label vehicleHandlingLabel = new Label("Hantera fordon");
        uiStyler.styleMainMenuLabels(vehicleHandlingLabel);

        Button addVehicleButton = new Button();
        addVehicleButton.setText("Lägg till fordon");
        uiStyler.styleDropdownMenuButton(addVehicleButton);
        addVehicleButton.setOnAction(e -> {
                openVehicleFormFromAddVehicleWindow = true;
                addOrChangeVehicleWindow(stage);
        });

        Button vehicleRegistryButton = new Button();
        vehicleRegistryButton.setText("Sök, ändra eller ta bort fordon");
        uiStyler.styleDropdownMenuButton(vehicleRegistryButton);
        vehicleRegistryButton.setOnAction(e -> {
            openVehicleRegistryFromBookWindow = true;
            vehicleRegistryFromMenu(stage);
        });

        ComboBox <Button> vehicleComboBox = new ComboBox<>();
        vehicleComboBox.getItems().addAll(addVehicleButton, vehicleRegistryButton);

        Label rentalHandlingLabel = new Label("Hantera uthyrning");
        uiStyler.styleMainMenuLabels(rentalHandlingLabel);

        Button bookRentalButton = new Button();
        bookRentalButton.setText("Boka uthyrning");
        uiStyler.styleDropdownMenuButton(bookRentalButton);
        bookRentalButton.setOnAction(e -> bookRentalsWindow(stage));

        Button ongoingRentalsButton = new Button();
        ongoingRentalsButton.setText("Se uthyrningar/återlämna fordon");
        uiStyler.styleDropdownMenuButton(ongoingRentalsButton);
        ongoingRentalsButton.setOnAction(e -> ongoingAndReturnRentalWindow(stage));

        ComboBox<Button> rentalComboBox = new ComboBox<>();
        rentalComboBox.getItems().addAll(bookRentalButton, ongoingRentalsButton);

        Label economyLabel = new Label("Hantera ekonomi");
        uiStyler.styleMainMenuLabels(economyLabel);

        Button sumRevenueButton = new Button();
        sumRevenueButton.setText("Summera intäkter");
        uiStyler.styleDropdownMenuButton(sumRevenueButton);
        sumRevenueButton.setOnAction(e -> sumRevenueWindow(stage));

        ComboBox <Button> economyComboBox = new ComboBox<>();
        economyComboBox.getItems().addAll(sumRevenueButton);

        Button quitButton = new Button("Avsluta");
        uiStyler.styleOrdinaryButton(quitButton);
        quitButton.setOnAction(e -> System.exit(0));

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-padding: 30");
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.add(headingLabel, 0, 0);
        gridPane.add(headingLabel2, 0, 1);
        gridPane.add(topLineLabel, 0, 3);
        gridPane.add(memberHandlingLabel, 0, 5);
        gridPane.add(memberComboBox, 0, 6);
        gridPane.add(vehicleHandlingLabel, 0, 9);
        gridPane.add(vehicleComboBox, 0, 10);
        gridPane.add(rentalHandlingLabel, 0, 13);
        gridPane.add(rentalComboBox, 0, 14);
        gridPane.add(economyLabel, 0, 17);
        gridPane.add(economyComboBox, 0, 18);

        Label wigellLabel = new Label("Skapat av Wigellkoncernen");
        wigellLabel.setFont(Font.font("Nanum Gothic", FontWeight.BOLD, 10));

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(240);
        bottomHBox.getChildren().addAll(wigellLabel, quitButton);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setRight(bottomHBox);

        BorderPane borderPaneMain = new BorderPane();
        borderPaneMain.setCenter(gridPane);
        borderPaneMain.setBottom(bottomPane);

        Scene scene = uiStyler.styleWindow(bottomHBox, borderPaneMain);
        stage.setScene(scene);
        stage.show();
    }

    //***********MEMBER*********
    public void addOrChangeMemberWindow(Stage stage) {

        stage.setTitle("MEDLEMSFORMULÄR");
        Label headingLabel;

        if (openMemberFormFromAddMemberWindow == false) {
            headingLabel = new Label("Lägg till medlem");
        }
        else {
           headingLabel = new Label("Ändra uppgifter om medlem");
        }

        uiStyler.styleHeadingLabel(headingLabel);

        Label personalIdNrLabel = new Label("Personnummer (ÅÅMMDD-XXXX)");
        uiStyler.styleLabelinForm(personalIdNrLabel);
        Label firstNameLabel = new Label("Förnamn ");
        uiStyler.styleLabelinForm(firstNameLabel);
        Label lastNameLabel = new Label("Efternamn ");
        uiStyler.styleLabelinForm(lastNameLabel);
        Label membershipLevelLabel = new Label("Medlemsnivå (student, standard, premium) ");
        uiStyler.styleLabelinForm(membershipLevelLabel);

        TextField personalIdNrField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField membershipLevelField = new TextField();

        if (openMemberFormFromAddMemberWindow == false) {
        }
        else {
            personalIdNrField.setText(member.getPersonalIdNumber());
            firstNameField.setText(member.getFirstName());
            lastNameField.setText(member.getLastName());
            membershipLevelField.setText(member.getMembershipLevel());
        }

        Label savedStatusLabel = new Label();
        savedStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextFlow infoAboutMemberTextFlow = new TextFlow();

        Button saveButton = new Button("Spara");
        uiStyler.styleOrdinaryButton(saveButton);
        saveButton.setOnAction(e -> {

            infoAboutMemberTextFlow.getChildren().clear();

            if (personalIdNrField.getText().isEmpty()) {

                savedStatusLabel.setText("Du har inte fyllt i alla fält!");
                savedStatusLabel.setStyle("-fx-text-fill: #C0392B");
                personalIdNrField.setStyle("-fx-border-color: #C0392B; -fx-border-width: 2;");
                return;
            }
            personalIdNrField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");

            if (firstNameField.getText().isEmpty()) {

                savedStatusLabel.setText("Du har inte fyllt i alla fält!");
                savedStatusLabel.setStyle("-fx-text-fill: #C0392B");
                firstNameField.setStyle("-fx-border-color: #C0392B; -fx-border-width: 2;");
                return;
            }
            firstNameField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");

            if (lastNameField.getText().isEmpty()) {

                savedStatusLabel.setText("Du har inte fyllt i alla fält!");
                savedStatusLabel.setStyle("-fx-text-fill: #C0392B");
                lastNameField.setStyle("-fx-border-color: #C0392B; -fx-border-width: 2;");
                return;
            }
            lastNameField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");

            if (membershipLevelField.getText().isEmpty()) {

                savedStatusLabel.setText("Du har inte fyllt i alla fält!");
                savedStatusLabel.setStyle("-fx-text-fill: #C0392B");
                membershipLevelField.setStyle("-fx-border-color: #C0392B; ");
                return;
            }
            membershipLevelField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");

            //Ej klar, ses endast i konsolen
            if (membershipLevelField.getText().equalsIgnoreCase("student") ||
                membershipLevelField.getText().equalsIgnoreCase("premium")  ||
                membershipLevelField.getText().equals("standard")) {

                System.out.println("Om rätt medlemsnivå");
            }

            else {
               savedStatusLabel.setText("Kan endast vara student, premium, standard");
               savedStatusLabel.setStyle("-fx-text-fill: red");

               System.out.println("Om fel medlemsnivå");
            }

           //Ej klar, ses endast i konsolen
            String characters = personalIdNrField.getText();
            characters.length();
            int countCharacters = characters.length();
            System.out.println(countCharacters);

            if (countCharacters < 11 || countCharacters > 11) {
                System.out.println("För få eller för många tecken");
            }

            if (openMemberFormFromAddMemberWindow == false) {
                Member member = new Member(
                        personalIdNrField.getText(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        membershipLevelField.getText());

                memberRegistry.addMemberToRegistry(member);
                printMemberInfo(member, infoAboutMemberTextFlow);
            }

            else {
                member.setPersonalIdNumber(personalIdNrField.getText());
                member.setFirstName(firstNameField.getText());
                member.setLastName(lastNameField.getText());
                member.setMembershipLevel(membershipLevelField.getText());
                printMemberInfo(member, infoAboutMemberTextFlow);
            }

            savedStatusLabel.setStyle("-fx-text-fill: #4F6F66");
            savedStatusLabel.setText("Följande uppgifter är sparade");

            personalIdNrField.clear();
            firstNameField.clear();
            lastNameField.clear();
            membershipLevelField.clear();
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(15);
        gridPane.setStyle("-fx-padding: 30");
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.add(headingLabel, 0, 0);
        gridPane.add(personalIdNrLabel, 0, 2);
        gridPane.add(personalIdNrField, 0, 3);
        gridPane.add(firstNameLabel, 0, 4);
        gridPane.add(firstNameField, 0, 5);
        gridPane.add(lastNameLabel, 0, 6);
        gridPane.add(lastNameField, 0, 7);
        gridPane.add(membershipLevelLabel, 0, 8);
        gridPane.add(membershipLevelField, 0, 9);
        gridPane.add(saveButton, 0, 12);
        gridPane.add(savedStatusLabel, 0, 14);
        gridPane.add(infoAboutMemberTextFlow, 0, 15);

        HBox hBox = new HBox();

        if (openMemberFormFromAddMemberWindow == false) {

            Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
            returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

            hBox.getChildren().addAll(returnToMenuButton);
        }

        else {

            Button backToMemberRegistry = new Button("Klar");
            uiStyler.styleOrdinaryButton(backToMemberRegistry);
            backToMemberRegistry.setOnAction(e -> {
                stage.setScene(previousMemberScene);
                stage.setTitle(previousMemberHeadline);
            });

            hBox.getChildren().addAll(backToMemberRegistry);
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(hBox);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public HBox memberRegistryTableView(Stage stage)   {

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #96ACA6;");

        Label headingLabel = new Label("Alla medlemmar");
        uiStyler.styleHeadingLabel(headingLabel);

        memberTableView = new TableView<>();
        memberTableView.setEditable(true);
        memberTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        memberTableView.setId("customTable");
        memberTableView.getStylesheets().add("style.css");

        memberTableView.setRowFactory(tv -> new TableRow<Member>() {
            @Override
            protected void updateItem(Member item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                }
                else if (item == chosenMemberInRegistryHighlight) {
                    setStyle("-fx-background-color: #82a9a1;");
                }
                else {
                    setStyle("");
                }
            }
        });

        TableColumn<Member, String> firstNameColumn = new TableColumn<>("Förnamn");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Member, String> lastNameColumn = new TableColumn<>("Efternamn");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Member, String> personalIdNumberColumn = new TableColumn<>("Personnummer");
        personalIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("personalIdNumber"));

        TableColumn<Member, String> membershipLevelColumn = new TableColumn<>("Medlemsnivå");
        membershipLevelColumn.setCellValueFactory(new PropertyValueFactory<>("membershipLevel"));

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Sök medlem");

        Label infoLabel = new Label("Du kan söka på förnamn, efternamn och personnummer");

        Button searchButton = new Button("Sök");
        searchButton.setPrefSize(100, 25);
        searchButton.setStyle("-fx-background-radius: 5;");
        searchButton.setOnAction(e -> {

            String userInput = searchTextField.getText();
            Member foundMember = membershipService.findMember(userInput);

            if (searchTextField.getText().equals("")) {
                searchTextField.setStyle("-fx-border-color: #C0392B; -fx-border-width: 2;");
                infoLabel.setStyle("-fx-text-fill: #C0392B");
                infoLabel.setText("Du måste ange värde i sökfältet");
            }

            else if (foundMember == null) {
                searchTextField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");
                infoLabel.setStyle("-fx-text-fill: black");
                infoLabel.setText("Den medlemmen finns inte");
            }

            else {
                searchTextField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");

                int index = memberTableView.getItems().indexOf(foundMember);
                memberTableView.scrollTo(index);

                chosenMemberInRegistryHighlight = foundMember;
                memberTableView.refresh();
            }
        });

        HBox onTopHBox = new HBox(searchTextField, searchButton, infoLabel);
        onTopHBox.setSpacing(20);

        VBox onTopVBox = new VBox();
        onTopVBox.setSpacing(30);
        onTopVBox.setAlignment(Pos.CENTER);
        onTopVBox.setPadding(new Insets(20, 10, 30, 10));
        onTopVBox.getChildren().addAll(headingLabel, onTopHBox);

        TableColumn<Member, Void> orderHistoryColumn = new TableColumn<>("Orderhistorik");
        orderHistoryColumn.setCellFactory(col -> new TableCell<Member, Void>() {

            Button orderHistoryButton = new Button("Klicka");
            HBox orderHistoryHBox = new HBox(5);

            {
                orderHistoryHBox.setAlignment(Pos.CENTER);
                orderHistoryHBox.getChildren().addAll(orderHistoryButton);
                orderHistoryButton.setOnAction(e -> orderHistoryWindow(stage));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }

                else {
                    setGraphic(orderHistoryHBox);
                }
            }
        });

        Text headLineChosenMember = new Text("Ingen medlem vald");
        headLineChosenMember.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headLineChosenMember.setFill(Color.web("#4F6F66"));

        memberInMemberRegistryTextFlow.getChildren().clear();
        memberInMemberRegistryTextFlow.getChildren().add(headLineChosenMember);

        TableColumn<Member, Void> selectMemberColumn = new TableColumn<>("Välj medlem");
        selectMemberColumn.setCellFactory(col -> new TableCell<Member, Void>() {

            Button selectMemberButton = new Button("Välj");
            HBox selectMemberHBox = new HBox(5);

            {
                selectMemberHBox.setAlignment(Pos.CENTER);
                selectMemberHBox.getChildren().addAll(selectMemberButton);
                selectMemberButton.setOnAction(e -> {

                    memberRemovedLabel.setText("");

                    Member selectedMember = getTableView().getItems().get(getIndex());
                    member = selectedMember;
                    chosenMemberInRegistryHighlight = selectedMember;
                    memberTableView.refresh();

                    headLineChosenMember.setText("Vald medlem\n");
                    memberInMemberRegistryTextFlow.getChildren().setAll(headLineChosenMember);

                   printMemberInfo(selectedMember, memberInMemberRegistryTextFlow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }

                else {
                    setGraphic(selectMemberHBox);
                }
            }
        });

        memberTableView.getColumns().addAll(firstNameColumn, lastNameColumn,
                personalIdNumberColumn, membershipLevelColumn, orderHistoryColumn, selectMemberColumn);

        memberTableView.setStyle("-fx-border-color: black;" + "-fx-border-width: 1;");
        membersObservableList = memberRegistry.getMembersObservableList();
        memberTableView.setItems(membersObservableList);

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(60);
        bottomHBox.setStyle("-fx-padding: 20");
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomHBox.setPrefHeight(200);
        bottomHBox.setMinHeight(Region.USE_PREF_SIZE);
        bottomHBox.setMaxHeight(Region.USE_PREF_SIZE);
        bottomHBox.getChildren().addAll(memberInMemberRegistryTextFlow);

        borderPane.setTop(onTopVBox);
        BorderPane.setMargin(onTopHBox, new Insets(10));
        borderPane.setCenter(memberTableView);
        borderPane.setBottom(bottomHBox);

        Scene scene = new Scene(borderPane, 800, 750);
        stage.setScene(scene);

        return bottomHBox;
    }

    public void bottomMemberRegistry(Stage stage, HBox bottomHBox) {

        memberRemovedLabel.setStyle("-fx-text-fill: #4F6F66;");
        memberRemovedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().add(memberInMemberRegistryTextFlow);

        if (openMemberRegistryFromBookWindow) {

            Button chosenMemberReadyButton = new Button("Klar");
            uiStyler.styleOrdinaryButton(chosenMemberReadyButton);
            chosenMemberReadyButton.setOnAction(e -> stage.close());

            bottomHBox.setSpacing(450);
            bottomHBox.getChildren().addAll(chosenMemberReadyButton);
        }

        else {

            Button deleteButton = new Button("Ta bort");
            uiStyler.styleOrdinaryButton(deleteButton);
            deleteButton.setOnAction(e -> {

                if (member == null) {
                }

                else {

                    String title = "BORTTAG AV MEDLEM";
                    String header = "Är du säker på att du vill ta bort medlemmen?";
                    boolean removeMember = confirmRemovalAlertWindow(title, header);

                    if (removeMember == true) {

                        memberRegistry.removeMemberFromRegistry(member);
                        memberInMemberRegistryTextFlow.getChildren().clear();
                        memberRemovedLabel.setText(member.getFirstName() + " " +
                                member.getLastName() + "\när borttagen\nfrån registret");
                    }
                }
            });

            Button changeButton = new Button("Ändra");
            uiStyler.styleOrdinaryButton(changeButton);
            changeButton.setOnAction(e -> {

                if (member == null) {
                }

                else {
                    memberTableView.refresh();
                    previousMemberScene = stage.getScene();
                    previousMemberHeadline = stage.getTitle();
                    openMemberFormFromAddMemberWindow = true;
                    addOrChangeMemberWindow(stage);
                }
            });

            Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
            returnToMenuButton.setOnAction(e -> {
                memberRemovedLabel.setText("");
                mainMenuWindow(stage);
                stage.close();
            });

            VBox vBox1 = new VBox();
            vBox1.getChildren().add(memberRemovedLabel);

            VBox vBox2 = new VBox();
            vBox2.setAlignment(Pos.CENTER);
            vBox2.setSpacing(20);
            vBox2.getChildren().addAll(deleteButton, changeButton);

            VBox vBox3 = new VBox();
            vBox3.setAlignment(Pos.BOTTOM_RIGHT);
            vBox3.getChildren().addAll(returnToMenuButton);

            bottomHBox.setSpacing(110);
            bottomHBox.getChildren().addAll(vBox1, vBox2, vBox3);
        }
    }

    public void memberRegistryChangeDelete()  {

        openMemberRegistryFromBookWindow = false;

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("MEDLEMSREGISTER");

        HBox hBox = memberRegistryTableView(stage);
        hBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        bottomMemberRegistry(stage, hBox);
        stage.showAndWait();
    }

    public void orderHistoryWindow(Stage stage) {
        stage.setTitle("ORDERHISTORIK");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #96ACA6;");

        Label headingLabel = new Label("Orderhistorik");
        uiStyler.styleHeadingLabel(headingLabel);

        Label headingLabelName = new Label();
        headingLabelName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headingLabelName.setStyle("-fx-text-fill: #4F6F66");

        VBox topVBox = new VBox();
        topVBox.setSpacing(30);
        topVBox.setAlignment(Pos.CENTER);
        topVBox.setPadding(new Insets(20, 10, 30, 10));

        if (member == null) {
            topVBox.getChildren().addAll(headingLabel, headingLabelName);

            Label infoLabel = new Label("Ingen medlem vald");
            infoLabel.setAlignment(Pos.CENTER);
            borderPane.setTop(topVBox);
            borderPane.setCenter(infoLabel);
        }

        else {
            headingLabelName.setText(member.getFirstName() +
                    " " + member.getLastName());

            topVBox.getChildren().addAll(headingLabel, headingLabelName);
            borderPane.setTop(topVBox);

            List<Rental> orderHistory = member.getOrderHistory();

            TableView<Rental> orderHistoryTableView = new TableView<>();
            orderHistoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            orderHistoryTableView.setId("customTable");
            orderHistoryTableView.getStylesheets().add("style.css");

            TableColumn<Rental, String> vehicleTypeColumn = new TableColumn<>("Fordonstyp");
            vehicleTypeColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getItem().getVehicleType()));

            TableColumn<Rental, String> vehiclePriceColumn = new TableColumn<>("Pris (kr/dag)");
            vehiclePriceColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(String.valueOf(data.getValue().getItem().getPrice())));

            TableColumn<Rental, String> vehicleBrandColumn = new TableColumn<>("Märke");
            vehicleBrandColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getItem().getBrand()));

            TableColumn<Rental, String> vehicleItemNrColumn = new TableColumn<>("Artikelnummer");
            vehicleItemNrColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getItem().getItemNumber()));

            TableColumn<Rental, String> daysColumn = new TableColumn<>("Antal dagar");
            daysColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));

            orderHistoryTableView.getColumns().addAll(vehicleTypeColumn, vehiclePriceColumn, vehicleBrandColumn,
                    vehicleItemNrColumn, daysColumn);

            ObservableList<Rental> orderHistoryObservableList = FXCollections.observableArrayList(orderHistory);
            orderHistoryTableView.setItems(orderHistoryObservableList);

            borderPane.setCenter(orderHistoryTableView);
        }

        Button closeWindowButton = new Button("Stäng");
        closeWindowButton.getStylesheets().add("style.css");
        uiStyler.styleOrdinaryButton(closeWindowButton);
        closeWindowButton.setOnAction(e -> {
            stage.setTitle("MEDLEMSREGISTER");

            HBox bottomHBox = memberRegistryTableView(stage);
            bottomHBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            bottomMemberRegistry(stage, bottomHBox);
        });

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(60);
        bottomHBox.setStyle("-fx-padding: 20");
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomHBox.setPrefHeight(200);
        bottomHBox.setMinHeight(Region.USE_PREF_SIZE);
        bottomHBox.setMaxHeight(Region.USE_PREF_SIZE);
        bottomHBox.getChildren().addAll(closeWindowButton);

        borderPane.setBottom(bottomHBox);

        Scene scene = new Scene(borderPane, 800, 750);
        stage.setScene(scene);
    }

    public void memberRegistryBook()  {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("MEDLEMSREGISTER");

        openMemberRegistryFromBookWindow = true;

        HBox bookHBox = memberRegistryTableView(stage);
        bookHBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        bottomMemberRegistry(stage, bookHBox);
        stage.showAndWait();
    }

    //********VEHICLE**************
    public void addOrChangeVehicleWindow(Stage stage) {

        stage.setTitle("FORDONSFORMULÄR");

        Label headingLabel = new Label("Lägg till fordon");
        uiStyler.styleHeadingLabel(headingLabel);


        if (openVehicleRegistryFromBookWindow == false) {
            headingLabel.setText("Lägg till fordon");
        }

        else {
            headingLabel.setText("Ändra fordon");
        }

        Label priceLabel = new Label("Pris (kr/dag)");
        uiStyler.styleLabelinForm(priceLabel);
        TextField priceTextField = new TextField();
        uiStyler.styleTextFieldinForm(priceTextField);

        Label brandLabel = new Label("Märke");
        uiStyler.styleLabelinForm(brandLabel);
        TextField brandTextField = new TextField();
        uiStyler.styleTextFieldinForm(brandTextField);

        Label itemNumberLabel = new Label("Artikelnummer");
        uiStyler.styleLabelinForm(itemNumberLabel);
        TextField itemNumberTextField = new TextField();
        uiStyler.styleTextFieldinForm(itemNumberTextField);

        Label choseVehicleTypeLabel = new Label("Välj fordonstyp");
        uiStyler.styleLabelinForm(choseVehicleTypeLabel);

        Button trailerButton = new Button("Släpvagn");
        uiStyler.styledropdownMenuButtoninForm(trailerButton);
        Button rideOnLawnMowerButton = new Button("Åkgräsklippare");
        uiStyler.styledropdownMenuButtoninForm(rideOnLawnMowerButton);
        Button roboticLawnMowerButton = new Button("Robotgräsklippare");
        uiStyler.styledropdownMenuButtoninForm(roboticLawnMowerButton);

        ComboBox <Button> choseVehicleTypeComboBox = new ComboBox<>();
        choseVehicleTypeComboBox.getItems().addAll(trailerButton, rideOnLawnMowerButton, roboticLawnMowerButton);

        Label chosenVehicleTypeinDropDown = new Label("");
        chosenVehicleTypeinDropDown.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 14));
        chosenVehicleTypeinDropDown.setStyle("-fx-background-color: #508f83");

        Label wrongInputMessage = new Label("");
        wrongInputMessage.setStyle("-fx-text-fill: #C0392B");
        wrongInputMessage.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        VBox sharedTopVBox = new VBox();
        sharedTopVBox.setSpacing(10);
        VBox.setMargin(headingLabel, new Insets(30, 0, 0, 0));
        VBox.setMargin(priceLabel, new Insets(30, 0, 0, 0));
        sharedTopVBox.setAlignment(Pos.TOP_CENTER);
        sharedTopVBox.getChildren().addAll(headingLabel, priceLabel, priceTextField, brandLabel, brandTextField,
                itemNumberLabel, itemNumberTextField, choseVehicleTypeLabel, choseVehicleTypeComboBox,chosenVehicleTypeinDropDown);

        Text vehicleIsSavedText = new Text("");
        vehicleIsSavedText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        vehicleIsSavedText.setFill(Color.web("#4F6F66"));

        TextFlow printInfoSavedVehicleTextFlow = new TextFlow();
        printInfoSavedVehicleTextFlow.setTextAlignment(TextAlignment.CENTER);
        printInfoSavedVehicleTextFlow.getChildren().addAll(vehicleIsSavedText);

        Button saveButton = new Button("Spara");
        uiStyler.styleOrdinaryButton(saveButton);

        VBox middleVBox = new VBox();
        middleVBox.setSpacing(10);
        middleVBox.setAlignment(Pos.TOP_CENTER);
        middleVBox.getChildren().addAll(saveButton);

        Label lengthLabel = new Label("");
        uiStyler.styleLabelinForm(lengthLabel);
        TextField lengthTextField = new TextField();
        uiStyler.styleTextFieldinForm(lengthTextField);

        Label widthLabel = new Label("");
        uiStyler.styleLabelinForm(widthLabel);
        TextField widthTextField = new TextField();
        uiStyler.styleTextFieldinForm(widthTextField);

        trailerButton.setOnAction(e -> {

            middleVBox.getChildren().clear();

            chosenVehicleTypeinDropDown.setText("släpvagn");
            lengthLabel.setText("Längd (i cm)");
            widthLabel.setText("Bredd (i cm)");

            VBox.setMargin(chosenVehicleTypeinDropDown, new Insets(10, 0, 5, 0));
            middleVBox.getChildren().addAll(chosenVehicleTypeinDropDown, lengthLabel,
                    lengthTextField, widthLabel, widthTextField, wrongInputMessage);
        });

        Label weightLabel = new Label("");
        uiStyler.styleLabelinForm(weightLabel);
        TextField weightTextField = new TextField();
        uiStyler.styleTextFieldinForm(weightTextField);

        rideOnLawnMowerButton.setOnAction(e -> {
            middleVBox.getChildren().clear();
            chosenVehicleTypeinDropDown.setText("åkgräsklippare");
            weightLabel.setText("vikt (i kg)");

            VBox.setMargin(chosenVehicleTypeinDropDown, new Insets(10, 0, 5, 0));
            middleVBox.getChildren().addAll(chosenVehicleTypeinDropDown, weightLabel, weightTextField, wrongInputMessage);
        });

        Label lawnSizeLabel = new Label("");
        uiStyler.styleLabelinForm(lawnSizeLabel);
        TextField lawnSizeTextField = new TextField();
        uiStyler.styleTextFieldinForm(lawnSizeTextField);

        roboticLawnMowerButton.setOnAction(e -> {
            middleVBox.getChildren().clear();
            chosenVehicleTypeinDropDown.setText("robotgräsklippare");
            weightLabel.setText("vikt (i kg)");
            lawnSizeLabel.setText("kapacitet (i m2)");

           VBox.setMargin(chosenVehicleTypeinDropDown, new Insets(10, 0, 5, 0));
           middleVBox.getChildren().addAll(chosenVehicleTypeinDropDown, weightLabel, weightTextField, lawnSizeLabel,
                   lawnSizeTextField, wrongInputMessage);
        });

          if (openVehicleRegistryFromBookWindow == true){
                String priceString = String.valueOf(vehicle.getPrice());
                priceTextField.setText(priceString);
                brandTextField.setText(vehicle.getBrand());
                itemNumberTextField.setText(vehicle.getItemNumber());
            }

        saveButton.setOnAction(e-> {

            String vehicleTypeString = chosenVehicleTypeinDropDown.getText();
            boolean vehicleInputIsCorrect = validateAndSaveVehicle(priceTextField, brandTextField, vehicleTypeString,
                    itemNumberTextField, lengthTextField, widthTextField, weightTextField,
                    lawnSizeTextField, wrongInputMessage);

            if (vehicleInputIsCorrect==true) {
                vehicleIsSavedText.setText("\nSparade uppgifter\n");
                printVehicleInfo(vehicle, printInfoSavedVehicleTextFlow);

                priceTextField.clear();
                brandTextField.clear();
                itemNumberTextField.clear();
                lengthTextField.clear();
                widthTextField.clear();
                weightTextField.clear();
                lawnSizeTextField.clear();
                chosenVehicleTypeinDropDown.setText("");
                wrongInputMessage.setText("");
            }
        });

        VBox bottomVBox = new VBox();
        bottomVBox.setSpacing(0);
        bottomVBox.setAlignment(Pos.TOP_CENTER);
        bottomVBox.getChildren().addAll(saveButton, printInfoSavedVehicleTextFlow);

        HBox bottomHBox = new HBox();

        if (openVehicleFormFromAddVehicleWindow == true)   {
             Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
             bottomHBox.getChildren().addAll(returnToMenuButton);
             returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));
        }

        else    {
            Button chosenVehicleReadyButton = new Button("Klar");
            uiStyler.styleOrdinaryButton(chosenVehicleReadyButton);
            bottomHBox.getChildren().addAll(chosenVehicleReadyButton);

            chosenVehicleReadyButton.setOnAction(e -> {
                stage.setScene(previousVehicleScene);
                stage.setTitle(previousVehicleHeadLine);

            });
        }

        BorderPane bottomBorderPane = new BorderPane();
        bottomBorderPane.setCenter(middleVBox);
        bottomBorderPane.setBottom(bottomVBox);

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setTop(sharedTopVBox);
        mainBorderPane.setCenter(bottomBorderPane);
        mainBorderPane.setBottom(bottomHBox);

        Scene scene = uiStyler.styleWindow(bottomHBox, mainBorderPane);
        stage.setScene(scene);
        stage.show();
    }

    public boolean validateAndSaveVehicle(TextField priceTextField, TextField brandTextField,
                                          String vehicleType, TextField itemNumberTextField, TextField lengthTextField,
                                          TextField widthTextField, TextField weightTextField, TextField lawnSizeTextField,
                                          Label wrongInputMessage)    {

        try {

            int price = Integer.parseInt(priceTextField.getText().trim());

            if (vehicleType.equals("släpvagn")) {

                int length = Integer.parseInt(lengthTextField.getText().trim());
                int width = Integer.parseInt(widthTextField.getText().trim());

                if (openVehicleFormFromAddVehicleWindow == true) {

                    vehicle.setPrice(price);
                    vehicle.setBrand(brandTextField.getText());
                    vehicle.setVehicleType(vehicleType);
                    vehicle.setItemNumber(itemNumberTextField.getText());
                }
                else    {
                    vehicle = new Trailer(price, brandTextField.getText().trim(),
                            itemNumberTextField.getText().trim(), length, width);
                    inventory.addVehicle(vehicle);
                }
            }

            else if (vehicleType.equals("åkgräsklippare")) {

                int weight = Integer.parseInt(weightTextField.getText().trim());

                if (openVehicleFormFromAddVehicleWindow == true) {
                    vehicle.setPrice(price);
                    vehicle.setBrand(brandTextField.getText());
                    vehicle.setVehicleType(vehicleType);
                    vehicle.setItemNumber(itemNumberTextField.getText());
                }
                else {
                    vehicle = new RideOnLawnMower(price, brandTextField.getText().trim(),
                            itemNumberTextField.getText().trim(), weight);
                    inventory.addVehicle(vehicle);
                }
            }

            else if (vehicleType.equals("robotgräsklippare")) {

                int weight = Integer.parseInt(weightTextField.getText().trim());
                int lawnSize = Integer.parseInt(lawnSizeTextField.getText().trim());

                if (openVehicleFormFromAddVehicleWindow == true) {
                    vehicle.setPrice(price);
                    vehicle.setBrand(brandTextField.getText());
                    vehicle.setVehicleType(vehicleType);
                    vehicle.setItemNumber(itemNumberTextField.getText());
                }
                else {
                    vehicle = new RoboticLawnMower(price, brandTextField.getText().trim(),
                            itemNumberTextField.getText().trim(), weight, lawnSize);
                        inventory.addVehicle(vehicle);
                }
            }

            return true;
        }

        catch (NumberFormatException e) {
            wrongInputMessage.setText("\nPris, längd, bredd och m2 ska anges i heltal");
            return false;
        }
    }

    public void vehicleRegistryFromMenu(Stage stage) {
        openVehicleRegistryFromBookWindow = true;
        vehicleRegistryTableView(stage);
        stage.show();
    }

     public void vehicleRegistryTableView(Stage stage) {

        stage.setTitle("FORDONSREGISTER");

        Label headingLabel = new Label("Alla fordon");
        uiStyler.styleHeadingLabel(headingLabel);

        Text headLineChosenVehicle = new Text("Inget fordon valt");
        headLineChosenVehicle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headLineChosenVehicle.setFill(Color.web("#4F6F66"));

        TextFlow printVehicleInfoTextFlow = new TextFlow();
        printVehicleInfoTextFlow.getChildren().add(headLineChosenVehicle);

        inventoryObservableList = inventory.getInventoryObservableList();
        ObservableList<Vehicle> trailers = inventoryObservableList.filtered(v -> v instanceof Trailer);
        ObservableList<Vehicle> roboticLawnMowers = inventoryObservableList.filtered(v -> v instanceof RoboticLawnMower);
        ObservableList<Vehicle> rideOnLawnMowers = inventoryObservableList.filtered(v -> v instanceof RideOnLawnMower);

        TableView<Vehicle> allVehicleTableView = new TableView<>();
        allVehicleTableView.setEditable(true);
        allVehicleTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        allVehicleTableView.setId("customTable");

        TableColumn<Vehicle, String> vehicleTypeColumn1 = new TableColumn<>("Fordonstyp");
        vehicleTypeColumn1.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        TableColumn<Vehicle, String> brandColumn1 = new TableColumn<>("Märke");
        brandColumn1.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Vehicle, Integer> priceColumn1 = new TableColumn<>("Pris (kr/dag)");
        priceColumn1.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Vehicle, String> itemColumn1 = new TableColumn<>("Artikelnr");
        itemColumn1.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));

        TableColumn<Vehicle, Void> selectColumn1 = new TableColumn<>("Välj");
        selectColumn1.setCellFactory(col -> new TableCell<Vehicle, Void>() {
            Button selectButton = new Button("Välj");
            HBox selectHBox = new HBox(selectButton);

            {
                selectHBox.setAlignment(Pos.CENTER);
                selectButton.setOnAction(e -> {
                    Vehicle selectedVehicle = getTableView().getItems().get(getIndex());
                    vehicle = selectedVehicle;
                    headLineChosenVehicle.setText("Valt fordon\n");
                    printVehicleInfoTextFlow.getChildren().setAll(headLineChosenVehicle);
                    printVehicleInfo(selectedVehicle, printVehicleInfoTextFlow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : selectHBox);
            }
        });

        allVehicleTableView.getColumns().addAll(vehicleTypeColumn1, brandColumn1, priceColumn1, itemColumn1, selectColumn1);
        allVehicleTableView.setItems(inventory.getInventoryObservableList());

        TableView<Vehicle> trailersTableView = new TableView<>();
        trailersTableView.setEditable(true);
        trailersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        trailersTableView.setId("customTable");

        TableColumn<Vehicle, String> vehicleType2 = new TableColumn<>("Fordonstyp");
        vehicleType2.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        TableColumn<Vehicle, String> brandColumn2 = new TableColumn<>("Märke");
        brandColumn2.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Vehicle, Integer> priceColumn2 = new TableColumn<>("Pris (kr/dag)");
        priceColumn2.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Vehicle, String> itemColumn2 = new TableColumn<>("Artikelnr");
        itemColumn2.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));
        TableColumn<Vehicle, Integer> lengthColumn = new TableColumn<>("Längd (i cm)");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        TableColumn<Vehicle, Integer> widthColumn = new TableColumn<>("Bredd (i cm)");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Vehicle, Void> selectColumn2 = new TableColumn<>("Välj");
        selectColumn2.setCellFactory(col -> new TableCell<Vehicle, Void>() {
            Button selectButtonTrailer = new Button("Välj");
            HBox trailerHBox = new HBox(selectButtonTrailer);

            {
                trailerHBox.setAlignment(Pos.CENTER);
                selectButtonTrailer.setOnAction(e -> {
                    Vehicle selectedTrailer = getTableView().getItems().get(getIndex());
                    vehicle = selectedTrailer;
                    headLineChosenVehicle.setText("Valt fordon\n");
                    printVehicleInfoTextFlow.getChildren().setAll(headLineChosenVehicle);
                    printVehicleInfo(selectedTrailer, printVehicleInfoTextFlow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : trailerHBox);
            }
        });

        trailersTableView.getColumns().addAll(vehicleType2, brandColumn2, priceColumn2, itemColumn2, lengthColumn, widthColumn, selectColumn2);
        trailersTableView.setItems(trailers);

        TableView<Vehicle> roboticLawnMowersTableView = new TableView<>();
        roboticLawnMowersTableView.setEditable(true);
        roboticLawnMowersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        roboticLawnMowersTableView.setId("customTable");

        TableColumn<Vehicle, String> vehicleTypeColumn3 = new TableColumn<>("Fordonstyp");
        vehicleTypeColumn3.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        TableColumn<Vehicle, String> brandColumn3 = new TableColumn<>("Märke");
        brandColumn3.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Vehicle, Integer> priceColumn3 = new TableColumn<>("Pris (kr/dag)");
        priceColumn3.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Vehicle, String> itemColumn3 = new TableColumn<>("Artikelnr");
        itemColumn3.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));
        TableColumn<Vehicle, Integer> weightColumn1 = new TableColumn<>("Vikt (kg)");
        weightColumn1.setCellValueFactory(new PropertyValueFactory<>("weight"));
        TableColumn<Vehicle, Integer> lawnSize = new TableColumn<>("Kapacitet (m2)");
        lawnSize.setCellValueFactory(new PropertyValueFactory<>("lawnSize"));

        TableColumn<Vehicle, Void> selectColumn3 = new TableColumn<>("Välj");
        selectColumn3.setCellFactory(col -> new TableCell<Vehicle, Void>() {
            Button choseButtonRoboticLawnMower = new Button("Välj");
            HBox roboticMowerHBox = new HBox(choseButtonRoboticLawnMower);

            {
                roboticMowerHBox.setAlignment(Pos.CENTER);
                choseButtonRoboticLawnMower.setOnAction(e -> {
                    Vehicle selectedRoboticLawnMower = getTableView().getItems().get(getIndex());
                    vehicle = selectedRoboticLawnMower;
                    headLineChosenVehicle.setText("Valt fordon\n");
                    printVehicleInfoTextFlow.getChildren().setAll(headLineChosenVehicle);
                    printVehicleInfo(selectedRoboticLawnMower, printVehicleInfoTextFlow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : roboticMowerHBox);
            }
        });

        roboticLawnMowersTableView.getColumns().addAll(vehicleTypeColumn3, brandColumn3, priceColumn3,
                itemColumn3, weightColumn1, lawnSize, selectColumn3);
        roboticLawnMowersTableView.setItems(roboticLawnMowers);

        TableView<Vehicle> rideOnLawnMowersTableView = new TableView<>();
        rideOnLawnMowersTableView.setEditable(true);
        rideOnLawnMowersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rideOnLawnMowersTableView.setId("customTable");

        TableColumn<Vehicle, String> vehicleType4 = new TableColumn<>("Fordonstyp");
        vehicleType4.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        TableColumn<Vehicle, String> brandColumn4 = new TableColumn<>("Märke");
        brandColumn4.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Vehicle, Integer> priceColumn4 = new TableColumn<>("Pris (kr/dag)");
        priceColumn4.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Vehicle, String> itemNr4 = new TableColumn<>("Artikelnr");
        itemNr4.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));
        TableColumn<Vehicle, Integer> weightColumn2 = new TableColumn<>("Vikt (kg)");
        weightColumn2.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<Vehicle, Void> selectColumn4 = new TableColumn<>("Välj");
        selectColumn4.setCellFactory(col -> new TableCell<Vehicle, Void>() {
            Button rideOnChoseButton = new Button("Välj");
            HBox rideOnHBox = new HBox(rideOnChoseButton);

            {
                rideOnHBox.setAlignment(Pos.CENTER);
                rideOnChoseButton.setOnAction(e -> {
                    Vehicle selectedRideOn = getTableView().getItems().get(getIndex());
                    vehicle = selectedRideOn;
                    headLineChosenVehicle.setText("Valt fordon\n");
                    printVehicleInfoTextFlow.getChildren().setAll(headLineChosenVehicle);
                    printVehicleInfo(selectedRideOn, printVehicleInfoTextFlow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : rideOnHBox);
            }
        });

        rideOnLawnMowersTableView.getColumns().addAll(vehicleType4, brandColumn4, priceColumn4, itemNr4,
                weightColumn2, selectColumn4);
        rideOnLawnMowersTableView.setItems(rideOnLawnMowers);

        TabPane tabPane = new TabPane();

        Label allVehicleLabel = new Label("Alla fordon");
        Tab allVehicleTab = new Tab("", allVehicleTableView);
        uiStyler.styleTabs(allVehicleLabel, allVehicleTab);

        Label trailersLabel = new Label("Släpvagnar");
        Tab trailersTab = new Tab("", trailersTableView);
        uiStyler.styleTabs(trailersLabel, trailersTab);

        Label roboticLawnMowersLabel = new Label("Robotgräsklippare");
        Tab roboticLawnMowersTab = new Tab("", roboticLawnMowersTableView);
        uiStyler.styleTabs(roboticLawnMowersLabel, roboticLawnMowersTab);

        Label rideOnLawnMowersLabel = new Label("Åkgräsklippare");
        Tab rideOnLawnMowersTab = new Tab("", rideOnLawnMowersTableView);
        uiStyler.styleTabs(rideOnLawnMowersLabel, rideOnLawnMowersTab);

        tabPane.getTabs().addAll(allVehicleTab, trailersTab, roboticLawnMowersTab, rideOnLawnMowersTab);

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Sök fordon");

        Label infoAboutSearchLabel = new Label("Du kan söka på fordonstyp, märke och artikelnummer");
        Button searchButton = new Button("Sök");
        searchButton.setPrefSize(100, 25);
        searchButton.setStyle("-fx-background-radius: 5;");
        searchButton.setOnAction(e -> {

            String userInput = searchTextField.getText();
            Vehicle foundVehicle = inventory.findVehicle(userInput);

            if (searchTextField.getText().equals("")) {
                searchTextField.setStyle("-fx-border-color: #C0392B; -fx-border-width: 2;");
                infoAboutSearchLabel.setStyle("-fx-text-fill: #C0392B");
                infoAboutSearchLabel.setText("Du måste ange värde i sökfältet");
            }
            else if (foundVehicle == null) {
                searchTextField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");
                infoAboutSearchLabel.setStyle("-fx-text-fill: black");
                infoAboutSearchLabel.setText("Det fordonet finns inte");
            }
            else {
                searchTextField.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");

                tabPane.getSelectionModel().select(allVehicleTab);

                int index = allVehicleTableView.getItems().indexOf(foundVehicle);
                if (index >= 0) {
                    allVehicleTableView.scrollTo(index);
                    allVehicleTableView.getSelectionModel().clearAndSelect(index);
                    allVehicleTableView.requestFocus();
                }
            }
        });

        VBox vBox2;
        VBox vBox3;
        Label alreadyRentedLabel = new Label("");
        alreadyRentedLabel.setStyle("-fx-text-fill: #C0392B");
        alreadyRentedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        if (openVehicleRegistryFromBookWindow == true) {

            Button changeVehicleButton = new Button("Ändra");
            uiStyler.styleOrdinaryButton(changeVehicleButton);
            changeVehicleButton.setOnAction(e -> {

                if (vehicle == null) {
                }

                else {
                    previousVehicleScene = stage.getScene();
                    previousVehicleHeadLine = stage.getTitle();
                    addOrChangeVehicleWindow(stage);
                }

            });

            Button removeVehicleButton = new Button("Ta bort");
            uiStyler.styleOrdinaryButton(removeVehicleButton);
            removeVehicleButton.setOnAction(e -> {

                if (vehicle == null) {
                }
                else {
                        String title = "BORTTAG AV FORDON";
                        String header = "Är du säker på att du vill ta bort fordonet?";
                        boolean removeVehicle = confirmRemovalAlertWindow(title, header);

                        if (removeVehicle == true) {

                            inventory.removeVehicle(vehicle);
                            printVehicleInfoTextFlow.getChildren().clear();
                        }
                }
            });

            vBox2 = new VBox();
            vBox2.setAlignment(Pos.CENTER);
            vBox2.setSpacing(20);
            vBox2.setPadding(new Insets(0, 90, 0,0 ));
            vBox2.getChildren().addAll(removeVehicleButton, changeVehicleButton);

            Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
            returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

            vBox3 = new VBox();
            vBox3.setAlignment(Pos.BOTTOM_RIGHT);
            vBox3.getChildren().addAll(returnToMenuButton);
        }

        else {

            Button valtFordonKlarButton = new Button("Klar");
            uiStyler.styleOrdinaryButton(valtFordonKlarButton);
            valtFordonKlarButton.setOnAction(e -> {

                if (rentalService.checkIfRentedOut(vehicle)) {
                    alreadyRentedLabel.setText("Fordonet är redan uthyrt\n\n   Välj ett annat");
                }

                else {
                    ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
                }
            });

            vBox2 = new VBox();
            vBox2.setAlignment(Pos.CENTER);
            vBox2.setPadding(new Insets(0, 40, 0, 0));
            vBox2.getChildren().addAll(alreadyRentedLabel);

            vBox3 = new VBox();
            vBox3.setAlignment(Pos.BOTTOM_RIGHT);
            vBox3.getChildren().addAll(valtFordonKlarButton);
        }

        HBox onTopHBox = new HBox();
        onTopHBox.setSpacing(20);
        onTopHBox.getChildren().addAll(searchTextField, searchButton, infoAboutSearchLabel);

        VBox onTopVBox = new VBox();
        onTopVBox.setSpacing(30);
        onTopVBox.setAlignment(Pos.CENTER);
        onTopVBox.setPadding(new Insets(20, 10, 30, 10));
        onTopVBox.getChildren().addAll(headingLabel, onTopHBox);

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10, 0, 0, 20));
        vBox1.getChildren().addAll(headLineChosenVehicle, printVehicleInfoTextFlow);

        BorderPane bottomBorderPane = new BorderPane();
        bottomBorderPane.setPadding(new Insets(20));
        bottomBorderPane.setPrefHeight(235);
        bottomBorderPane.setMinHeight(Region.USE_PREF_SIZE);
        bottomBorderPane.setMaxHeight(Region.USE_PREF_SIZE);
        bottomBorderPane.setLeft(vBox1);
        bottomBorderPane.setCenter(vBox2);
        bottomBorderPane.setRight(vBox3);
        BorderPane.setAlignment(vBox1, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(vBox2, Pos.CENTER);
        BorderPane.setAlignment(vBox3, Pos.BOTTOM_RIGHT);

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setStyle("-fx-background-color: #96ACA6;");
        mainBorderPane.setTop(onTopVBox);
        mainBorderPane.setCenter(tabPane);
        mainBorderPane.setBottom(bottomBorderPane);

        Scene scene = new Scene(mainBorderPane, 800, 750);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
    }

    //********RENTAL**************
    public void bookRentalsWindow(Stage stage) {
        stage.setTitle("BOKA FORDON");

        Label headingLabel = new Label("Bokningssida");
        uiStyler.styleHeadingLabel(headingLabel);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);
        BorderPane.setAlignment(headingLabel, Pos.CENTER);
        BorderPane.setMargin(headingLabel, new Insets(20));

        //***********FORDON***********
        Button nextButtonVehicle = new Button("Nästa");
        uiStyler.styleOrdinaryButton(nextButtonVehicle);
        nextButtonVehicle.setDisable(true);

        Label choseVehicleLabel = new Label("Välj fordon");
        uiStyler.styleHeadingLabel(choseVehicleLabel);

        Text headLineChosenVehicle = new Text("");
        headLineChosenVehicle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headLineChosenVehicle.setFill(Color.web("#4F6F66"));

        TextFlow vehicleSummaryTextFlow = new TextFlow();
        vehicleSummaryTextFlow.getChildren().addAll(headLineChosenVehicle);

        Button openVehicleRegisterButton = new Button("Till fordonsregistet (öppnas i nytt fönster) ");
        uiStyler.styleLongerButton(openVehicleRegisterButton);

        openVehicleRegisterButton.setOnAction(e -> {

            openVehicleRegistryFromBookWindow = false;
            Stage popupStage = new Stage();
            popupStage.initOwner(stage);
            popupStage.initModality(Modality.WINDOW_MODAL);
            vehicleRegistryTableView(popupStage);
            popupStage.showAndWait();

            if (vehicle == null) {
                return;
            }

            headLineChosenVehicle.setText("Valt fordon\n");
            vehicleSummaryTextFlow.getChildren().setAll(headLineChosenVehicle);
            printVehicleInfo(vehicle, vehicleSummaryTextFlow);
            nextButtonVehicle.setDisable(false);
        });

        HBox nextButtonVehicleHBox = new HBox();
        uiStyler.styleHBoxBooking(nextButtonVehicleHBox);
        nextButtonVehicleHBox.getChildren().addAll(nextButtonVehicle);

        Region vehicleSpacer = new Region();
        VBox.setVgrow(vehicleSpacer, Priority.ALWAYS);

        VBox vehicleVBox = new VBox();
        uiStyler.styleVBoxBooking(vehicleVBox);
        VBox.setMargin(choseVehicleLabel, new Insets(20, 0, 20, 0));
        VBox.setMargin(openVehicleRegisterButton, new Insets(0, 0, 30, 0));
        VBox.setMargin(vehicleSummaryTextFlow, new Insets(0, 0, 60, 160));
        vehicleVBox.getChildren().addAll(choseVehicleLabel, openVehicleRegisterButton,
                vehicleSummaryTextFlow, vehicleSpacer, nextButtonVehicleHBox);

        //***********DATUM***********
        Button nextButtonDate = new Button("Nästa");
        uiStyler.styleOrdinaryButton(nextButtonDate);
        nextButtonDate.setDisable(true);
        Button goBackButtonDate = new Button("Tillbaka");
        uiStyler.styleOrdinaryButton(goBackButtonDate);

        Label choseDateLabel = new Label("Välj datum");
        uiStyler.styleHeadingLabel(choseDateLabel);
        Label fromDateLabel = new Label("Startdatum");
        fromDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        DatePicker fromDatePicker = new DatePicker();
        Label toDateLabel = new Label("Slutdatum");
        toDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        DatePicker toDatePicker = new DatePicker();

        Button selectFromDateButton = new Button("Välj");
        selectFromDateButton.setPrefSize(60, 20);
        selectFromDateButton.setStyle("-fx-background-radius: 5;");

        Label chosenDateOrNoDateLabel = new Label("");
        chosenDateOrNoDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextFlow printDateTextFlow = new TextFlow();

        Button selectToDateButton = new Button("Välj");
        selectToDateButton.setPrefSize(60, 20);
        selectToDateButton.setStyle("-fx-background-radius: 5;");
        selectToDateButton.setOnAction(e -> {
               fromDate = fromDatePicker.getValue();
               toDate = toDatePicker.getValue();

               numberOfDays = (int) ChronoUnit.DAYS.between(fromDate, toDate);

                if (numberOfDays < 0)   {
                    toDatePicker.setStyle("-fx-border-color: #C0392B; -fx-border-width: 2;");
                    chosenDateOrNoDateLabel.setStyle("-fx-text-fill: #C0392B");
                    chosenDateOrNoDateLabel.setText("Slutdatum kan inte vara före startdatum");
                }

                else    {
                   toDatePicker.setStyle("-fx-border-color: #000000; -fx-border-width: 0;");

                   chosenDateOrNoDateLabel.setStyle("-fx-text-fill: #4F6F66;");
                   chosenDateOrNoDateLabel.setText("Valda datum");

                   printDateInfo(numberOfDays, printDateTextFlow);
                   nextButtonDate.setDisable(false);
                }
        });

        HBox nextBackButtonsDateHBox = new HBox();
        uiStyler.styleHBoxBooking(nextBackButtonsDateHBox);
        nextBackButtonsDateHBox.getChildren().addAll(goBackButtonDate, nextButtonDate);

        Region dateSpacer = new Region();
        VBox.setVgrow(dateSpacer, Priority.ALWAYS);

        VBox dateVBox = new VBox();
        uiStyler.styleVBoxBooking(dateVBox);
        VBox.setMargin(choseDateLabel, new Insets(20, 0, 10, 0));
        VBox.setMargin(fromDateLabel, new Insets(0, 0, 0, 0));
        VBox.setMargin(fromDatePicker, new Insets(0, 0, 0, 0));
        VBox.setMargin(selectFromDateButton, new Insets(0, 0, 20, 0));
        VBox.setMargin(toDateLabel, new Insets(0, 0, 0, 0));
        VBox.setMargin(toDatePicker, new Insets(0, 0, 0, 0));
        VBox.setMargin(selectToDateButton, new Insets(0, 0, 50, 0));
        VBox.setMargin(printDateTextFlow, new Insets(0, 0, 0, 185));

        dateVBox.getChildren().addAll(choseDateLabel, fromDateLabel, fromDatePicker, selectFromDateButton,
                toDateLabel, toDatePicker, selectToDateButton, chosenDateOrNoDateLabel, printDateTextFlow,
                dateSpacer, nextBackButtonsDateHBox);

        //***************MEDLEMSSIDA************************************
        Button finishBookingButton = new Button("Boka");
        uiStyler.styleOrdinaryButton(finishBookingButton);
        finishBookingButton.setDisable(true);

        Label choseMemberLabel = new Label("Välj medlem");
        uiStyler.styleHeadingLabel(choseMemberLabel);

        Text headLineChosenMember = new Text("");
        headLineChosenMember.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headLineChosenMember.setFill(Color.web("#4F6F66"));

        TextFlow memberSummaryTextFlow = new TextFlow();
        memberSummaryTextFlow.getChildren().addAll(headLineChosenMember);

        Button openMemberRegisterButton = new Button("Till medlemsregistret (öppnas i nytt fönster)");
        uiStyler.styleLongerButton(openMemberRegisterButton);
        openMemberRegisterButton.setOnAction(e -> {
                memberRegistryBook();
                printMemberInfo(member, memberSummaryTextFlow);
                headLineChosenMember.setText("Vald medlem\n");
                finishBookingButton.setDisable(false);
        });

        Button goBackButtonMember = new Button("Tillbaka");
        uiStyler.styleOrdinaryButton(goBackButtonMember);

        HBox finishBackButtonsMemberHBox = new HBox();
        uiStyler.styleHBoxBooking(finishBackButtonsMemberHBox);
        finishBackButtonsMemberHBox.getChildren().addAll(goBackButtonMember, finishBookingButton);

        Region memberSpacer = new Region();
        VBox.setVgrow(memberSpacer, Priority.ALWAYS);

        VBox memberVBox = new VBox();
        uiStyler.styleVBoxBooking(memberVBox);
        VBox.setMargin(choseMemberLabel, new Insets(20,0, 20, 0));
        VBox.setMargin(openMemberRegisterButton, new Insets(0, 0, 30, 0));
        VBox.setMargin(memberSummaryTextFlow, new Insets(0, 0, 60, 160));
        memberVBox.getChildren().addAll(choseMemberLabel, openMemberRegisterButton,
                memberSummaryTextFlow, memberSpacer, finishBackButtonsMemberHBox);

        //***************BOKNINGSBEKRÄFTELSE************************************
        Label confirmationLabel = new Label("Bokningsbekräftelse");
        uiStyler.styleHeadingLabel(confirmationLabel);

        TextFlow confirmationPrintTextFlow = new TextFlow();

        Button printReceiptButton = new Button("Skriv ut kvitto");
        uiStyler.styleLongerButton(printReceiptButton);
        printReceiptButton.setOnAction(e -> receipt());

        VBox confirmationVBox = new VBox();
        uiStyler.styleVBoxBooking(confirmationVBox);
        VBox.setMargin(confirmationLabel, new Insets(20, 0, 15, 0));
        VBox.setMargin(confirmationPrintTextFlow, new Insets(0, 0, 0, 160));
        VBox.setMargin(printReceiptButton, new Insets(15, 0, 0, 0));
        confirmationVBox.getChildren().addAll(confirmationLabel, confirmationPrintTextFlow, printReceiptButton);

        borderPane.setCenter(vehicleVBox);
        nextButtonVehicle.setOnAction(e -> borderPane.setCenter(dateVBox));
        goBackButtonDate.setOnAction(e -> borderPane.setCenter(vehicleVBox));
        nextButtonDate.setOnAction(e -> borderPane.setCenter(memberVBox));
        goBackButtonMember.setOnAction(e -> borderPane.setCenter(dateVBox));
        finishBookingButton.setOnAction(e -> {

            borderPane.setCenter(confirmationVBox);

            rental = rentalService.addBooking(vehicle, numberOfDays, member);
            finalPrice = rentalService.calculatePrice(rental);
            printBookingConfirmation(numberOfDays, confirmationPrintTextFlow);
        });

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void receipt() {

        Stage receiptStage = new Stage();
        receiptStage.initModality(Modality.APPLICATION_MODAL);
        receiptStage.setTitle("KVITTO");

        Label headingLabel = new Label("SHARED VEHICLES");
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label receiptLabel = new Label("KVITTO");
        receiptLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 16));
        Label starsLabel1 = new Label("********************************************");
        starsLabel1.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));

        Label vehicleTypeBrandLabel = new Label(vehicle.getVehicleType() + " " + vehicle.getBrand());
        vehicleTypeBrandLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label daysAndPriceLabel = new Label();
        daysAndPriceLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label amountLabel = new Label("       " + rental.getNumberOfDays() + " dag * " +
                vehicle.getPrice() + " kr     " + finalPrice + " kr");
        amountLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));

        Label starsLabel2 = new Label("********************************************");
        starsLabel2.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label totalAmountLabel = new Label("          Totalt      " + finalPrice + " kr");
        totalAmountLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        Label starsLabel3 = new Label("********************************************");
        starsLabel3.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label saveReceiptLabel = new Label("          SPARA KVITTOT");
        saveReceiptLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 18));
        Label welcomeBackLabel = new Label("           Välkommen åter!");
        welcomeBackLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 16));

        Button okButton = new Button("OK");
        okButton.setPrefSize(80, 30);
        okButton.setStyle("-fx-background-radius: 5;");
        okButton.setOnAction(e -> receiptStage.close());

        VBox topVBox = new VBox();
        topVBox.setAlignment(Pos.CENTER);
        topVBox.setSpacing(10);
        topVBox.setPadding(new Insets(10));
        topVBox.getChildren().addAll(headingLabel, receiptLabel, starsLabel1);

        VBox centerVBox = new VBox();
        centerVBox.setSpacing(10);
        centerVBox.setPadding(new Insets(0, 0, 0, 10));
               centerVBox.getChildren().addAll(vehicleTypeBrandLabel, daysAndPriceLabel, amountLabel, starsLabel2,
                       totalAmountLabel, starsLabel3, saveReceiptLabel, welcomeBackLabel);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topVBox);
        borderPane.setCenter(centerVBox);
        borderPane.setBottom(okButton);
        BorderPane.setAlignment(okButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(okButton, new Insets(10));

        Scene scene = new Scene(borderPane, 400, 400);
        receiptStage.setScene(scene);
        receiptStage.showAndWait();
    }

    public void ongoingAndReturnRentalWindow(Stage stage) {
        stage.setTitle("PÅGÅENDE UTHYRNINGAR");

        Label headingLabel = new Label("Uthyrda fordon just nu");
        uiStyler.styleHeadingLabel(headingLabel);

        TableView <Rental> ongoingRentalsTableView = new TableView<>();
        ongoingRentalsTableView.setEditable(true);
        ongoingRentalsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ongoingRentalsTableView.setId("customTable");
        ongoingRentalsTableView.setSelectionModel(null);

        ongoingRentalsTableView.setRowFactory(tv -> new TableRow<Rental>() {
            @Override
            protected void updateItem(Rental item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                }
                else if (item == chosenRentalInRegistryHighlight) {
                    setStyle("-fx-background-color: #82a9a1;");
                }
                else {
                    setStyle("");
                }
            }
        });

        TableColumn<Rental, String> memberColumn = new TableColumn<>("Medlem");
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("member"));
        memberColumn.setCellValueFactory(cellData ->
        new ReadOnlyStringWrapper(cellData.getValue().getMember().getFirstName() + " " +
                cellData.getValue().getMember().getLastName() + "\n(" +
                cellData.getValue().getMember().getPersonalIdNumber() + " )"));

        TableColumn<Rental, String> vehicleColumn = new TableColumn<>("Fordon");
        vehicleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getItem().getVehicleType() +
                        " (" + cellData.getValue().getItem().getBrand() + ")\n" +
                        cellData.getValue().getItem().getPrice() +
                        " kr/dag\nArtikelnr: " + cellData.getValue().getItem().getItemNumber()));

        TableColumn<Rental, String> numberOfDaysColumn = new TableColumn<>("Antal dagar");
        numberOfDaysColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));

        Text headLineChosenRental = new Text("Ingen uthyrning vald");
        headLineChosenRental.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headLineChosenRental.setFill(Color.web("#4F6F66"));

        TextFlow ongoingRentalTextFlow = new TextFlow();
        ongoingRentalTextFlow.getChildren().add(headLineChosenRental);

        Label rentalReturnedLabel = new Label();

        TableColumn<Rental, Void> selectRentalColumn = new TableColumn<>("Välj");
        selectRentalColumn.setCellFactory(col -> new TableCell<Rental, Void>() {

            Button selectRentalButton = new Button("Välj");
            HBox selectRentalHBox = new HBox(5);

            {
                selectRentalHBox.setAlignment(Pos.CENTER);
                selectRentalHBox.getChildren().addAll(selectRentalButton);
                selectRentalButton.setOnAction(e -> {

                    rentalReturnedLabel.setText("");

                    Rental selectedRental = getTableView().getItems().get(getIndex());
                    rental = selectedRental;

                    chosenRentalInRegistryHighlight = selectedRental;
                    ongoingRentalsTableView.refresh();

                    headLineChosenRental.setText("Vald uthyrning\n");
                    ongoingRentalTextFlow.getChildren().setAll(headLineChosenRental);
                    printRentalInfo(selectedRental, ongoingRentalTextFlow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }

                else {
                    setGraphic(selectRentalHBox);
                }
            }
        });

        ongoingRentalsTableView.getColumns().addAll(memberColumn, vehicleColumn, numberOfDaysColumn,  selectRentalColumn);
        ongoingRentalsTableView.setStyle("-fx-border-color: black;" + "-fx-border-width: 1;");

        rentalsObservableList = rentalService.getRentalsObservableList();
        ongoingRentalsTableView.setItems(rentalsObservableList);

        rentalReturnedLabel.setStyle("-fx-text-fill: #4F6F66;");
        rentalReturnedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Button returnRentalButton = new Button("Återlämna\n   fordon");
        returnRentalButton.getStyleClass().add("button-ordinary");
        returnRentalButton.setPrefSize(125, 60);
        returnRentalButton.setOnAction(e -> {

                    if (rental == null) {
                    }

                    else {
                        rentalService.returnItem(rental);
                        ongoingRentalTextFlow.getChildren().clear();
                        rentalReturnedLabel.setText("Fordonet är återlämnat");
                    }
        });

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox topHBox = new HBox();
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(new Insets(30, 0, 50, 0));
        topHBox.getChildren().addAll(headingLabel);

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10, 0, 0, 15));
        vBox1.getChildren().addAll(headLineChosenRental, ongoingRentalTextFlow, rentalReturnedLabel);

        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.CENTER);
        vBox2.setPadding(new Insets(0, 90, 0, 40));
        vBox2.getChildren().addAll(returnRentalButton);

        VBox vBox3 = new VBox();
        vBox3.setAlignment(Pos.BOTTOM_RIGHT);
        vBox3.getChildren().addAll(returnToMenuButton);

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(90);
        bottomHBox.setStyle("-fx-padding: 20");
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomHBox.setPrefHeight(200);
        bottomHBox.setMinHeight(Region.USE_PREF_SIZE);
        bottomHBox.setMaxHeight(Region.USE_PREF_SIZE);
        bottomHBox.getChildren().addAll(vBox1, vBox2, vBox3);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #96ACA6;");
        borderPane.setTop(topHBox);
        borderPane.setCenter(ongoingRentalsTableView);
        borderPane.setBottom(bottomHBox);

        Scene scene = new Scene(borderPane, 800, 750);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    //********ECONOMY************
    public void sumRevenueWindow(Stage stage) {
        stage.setTitle("SUMMERA INTÄKTER");

        Label headingLabel = new Label("Summering av dagens intäkter");
        uiStyler.styleHeadingLabel(headingLabel);

        Label noRentalsTodayLabel = new  Label("");
        noRentalsTodayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        noRentalsTodayLabel.setStyle("-fx-text-fill: #3f4240");

        Label howManyRentalsLabel = new Label("");
        howManyRentalsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        howManyRentalsLabel.setStyle("-fx-text-fill: #3f4240");

        Label todaysRevenueLabel = new Label("");
        todaysRevenueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        todaysRevenueLabel.setStyle("-fx-text-fill: #4F6F66");

        int totalRevenue = rentalService.calculateTodaysRevenue();

        if (totalRevenue == 0) {
            noRentalsTodayLabel.setText("Det har inte registrerats någon uthyrning idag");
        }

        if (totalRevenue > 0) {
            howManyRentalsLabel.setText("Idag har " + rentalService.getTodaysRentals().size() + " uthyrningar registrerats");

            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("sv", "SE"));
            todaysRevenueLabel.setText("Totala intäkter: " + numberFormat.format(totalRevenue) + " kr");
        }

        VBox vBox = new VBox();
        uiStyler.styleVBoxBooking(vBox);
        vBox.setPadding(new Insets(60, 10, 10, 10));
        vBox.setSpacing(20);
        vBox.getChildren().addAll(noRentalsTodayLabel, howManyRentalsLabel, todaysRevenueLabel);

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);
        borderPane.setAlignment(headingLabel, Pos.CENTER);
        BorderPane.setMargin(headingLabel, new Insets(25));
        borderPane.setCenter(vBox);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    //******ALERT**************
    public Boolean confirmRemovalAlertWindow(String title, String header)   {

        Alert confirmationAlert = new Alert(Alert.AlertType.WARNING);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(header);
        confirmationAlert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        confirmationAlert.getDialogPane().getStyleClass().add("remove-member-alert");
        confirmationAlert.getButtonTypes().clear();

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> userChoice = confirmationAlert.showAndWait();

        if (userChoice.isPresent() && userChoice.get() == okButton) {
                return true;
        }

        return false;
    }

    //********PRINT************
    public void printMemberInfo(Member member, TextFlow textFlow)    {

        textFlow.setLineSpacing(6);

        Text topLine = new Text("──────────────────\n");
        topLine.setFill(Color.web("#6F8F86"));

        Text name = new Text(member.getFirstName() + " " + member.getLastName() + "\n");
        name.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        name.setFill(Color.web("#1F2A26"));

        Text membershipLevel = new Text(member.getMembershipLevel().toUpperCase() + "\n\n");
        membershipLevel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        membershipLevel.setFill(Color.web("#4F6F66"));

        Text personalIdNr = new Text(member.getPersonalIdNumber());
        personalIdNr.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        personalIdNr.setFill(Color.web("#2E2E2E"));

        Text bottomLine = new Text("\n──────────────────");
        bottomLine.setFill(Color.web("#6F8F86"));

        textFlow.getChildren().addAll(topLine, name, membershipLevel, personalIdNr, bottomLine);
    }

    public void printVehicleInfo(Vehicle vehicle, TextFlow textFlow)    {

        textFlow.setLineSpacing(6);

        Text topLine = new Text("───────────────────────\n");
        topLine.setFill(Color.web("#6F8F86"));

        Text typeAndBrand = new Text(vehicle.getVehicleType() + " (" + vehicle.getBrand() + ")\n");
        typeAndBrand.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        typeAndBrand.setFill(Color.web("#1F2A26"));

        Text price = new Text(vehicle.getPrice() + " kr/dag\n\n");
        price.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        price.setFill(Color.web("#4F6F66"));

        Text itemNr = new Text("Artikelnr: " + vehicle.getItemNumber() + "\n");
        itemNr.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        itemNr.setFill(Color.web("#2E2E2E"));

        Text bottomLine = new Text("───────────────────────");
        bottomLine.setFill(Color.web("#6F8F86"));

        textFlow.getChildren().addAll(topLine, typeAndBrand, price, itemNr, bottomLine);
    }

    public void printDateInfo(int numberOfDays, TextFlow textFlow)    {

        textFlow.setLineSpacing(8);

        Text topLine = new Text("────────────────────────\n");
        topLine.setFill(Color.web("#6F8F86"));

        Text fromToDate = new Text(fromDate + " - " + toDate + "\n");
        fromToDate.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        fromToDate.setFill(Color.web("#1F2A26"));

        Text numberOfDaysText = new Text("                   " + numberOfDays + " dagar");
        numberOfDaysText.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        numberOfDaysText.setFill(Color.web("#4F6F66"));

        Text bottomLine = new Text("\n────────────────────────");
        bottomLine.setFill(Color.web("#6F8F86"));

        textFlow.getChildren().addAll(topLine, fromToDate, numberOfDaysText, bottomLine);
    }

    public void printBookingConfirmation(int numberOfDays, TextFlow textFlow)    {

        textFlow.setLineSpacing(10);

        Text topLine = new Text("──────────────────────────────────\n");
        topLine.setStyle("-fx-text-fill: #6F8F86;");

        Text bookedVehicleHeadLine = new Text();
        bookedVehicleHeadLine.setFont(Font.font("Apple Color Emoji", FontWeight.BOLD, 20));
        bookedVehicleHeadLine.setText("  🚘 Fordon\n");
        Text bookedVehicleText = new Text();
        bookedVehicleText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        bookedVehicleText.setText("      " + vehicle.getVehicleType() + " (" + vehicle.getBrand() + ")");

        Text bookedDateHeadLine = new Text();
        bookedDateHeadLine.setFont(Font.font("Apple Color Emoji", FontWeight.BOLD, 20));
        bookedDateHeadLine.setText("\n\n  🗓️ Period\n");
        Text bookedDateText = new Text();
        bookedDateText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        bookedDateText.setText("    " + fromDate + " - " + toDate +
                    " (" + numberOfDays + " dagar)");

        Text bookedMemberHeadLine = new Text();
        bookedMemberHeadLine.setFont(Font.font("Apple Color Emoji", FontWeight.BOLD, 20));
        bookedMemberHeadLine.setText("\n\n  👤 Medlem\n");

        Text bookedMemberText = new Text();
        bookedMemberText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        bookedMemberText.setText("   " + member.getFirstName() + " " +
                    member.getLastName() + " (" + member.getPersonalIdNumber() + ")");

        Text priceHeadLine = new Text();
        priceHeadLine.setFont(Font.font("Apple Color Emoji", FontWeight.BOLD, 20));
        priceHeadLine.setText("\n\n  💵 Totalpris\n");

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("sv", "SE"));
        Text priceText = new Text();
        priceText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        priceText.setText("                   " + numberFormat.format(finalPrice) + " kr");

        Text bottomLine = new Text("\n──────────────────────────────────");
        bottomLine.setStyle("-fx-text-fill: #6F8F86;");

        textFlow.getChildren().addAll(topLine, bookedVehicleHeadLine, bookedVehicleText,
                bookedDateHeadLine, bookedDateText, bookedMemberHeadLine,
                bookedMemberText, priceHeadLine, priceText, bottomLine);
    }

    public void printRentalInfo(Rental rental, TextFlow textFlow)   {

        textFlow.setLineSpacing(6);

        Text topLine = new Text("──────────────────\n");
        topLine.setFill(Color.web("#6F8F86"));

        Text memberName = new Text(rental.getMember().getFirstName() + " " + rental.getMember().getLastName() + "\n");
        memberName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        memberName.setFill(Color.web("#1F2A26"));

        Text vehicleTypeAndBrand = new Text(rental.getItem().getVehicleType() + " " + rental.getItem().getBrand() + "\n");
        vehicleTypeAndBrand.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        vehicleTypeAndBrand.setFill(Color.web("#4F6F66"));

        Text numberOfDays = new Text(rental.getNumberOfDays() + " dagar");
        numberOfDays.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        numberOfDays.setFill(Color.web("#2E2E2E"));

        Text bottomLine = new Text("\n──────────────────");
        bottomLine.setFill(Color.web("#6F8F86"));

        textFlow.getChildren().addAll(topLine, memberName, vehicleTypeAndBrand, numberOfDays, bottomLine);
    }

    public static void main(String[] args) {
         launch(args);
    }
}


