package com.linnea;

import com.linnea.entity.*;
import com.linnea.service.MembershipService;
import com.linnea.service.RentalService;
import com.linnea.util.UIStyler;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

    String memberRegistryFile;
    String inventoryListFile;
    String rentalListFile;

    int numberOfDays;
    Vehicle vehicleInstansvariabel;
    Member memberInstansvariabel;

    Label memberRemovedLabel = new Label();
    TextFlow memberInfoIMedlemsregisterTextFlow = new TextFlow();

    boolean openMemberRegistryFromBookWindow = false;
    boolean openMemberFormFromAddMemberWindow = false;

    LocalDate fromDateInstansvariabel;
    LocalDate toDateInstansvariabel;

    int finalPriceInstansvariabel;

    public Rental rentalInstansvariabel;

    Scene previousScene;
    String previousHeadline;

    ObservableList<Member> membersObservableList;
    TableView<Member> memberTableView;

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
        inventory.readFromFileMemberRegistry(inventoryListFile);

        if (inventory.getInventoryList().isEmpty()) {
            inventory.initialValueInventoryList();
            inventory.writeToFileInventory();
        }

        rentalListFile = "rentalListFile.ser";
        rentalService = new RentalService(membershipService, inventory, rentalListFile);
        rentalService.readFromFileRentals(rentalListFile);

        if (rentalService.getRentalList().isEmpty()) {
            rentalService.initialValueRentalList();
            rentalService.writeToFileRentals();
        }

        //************ORDERHISTORIK*******************
        Member member1 = membershipService.searchMemberNY("Lars");
        Vehicle vehicle1 = inventory.findVehicleByItemNr("125");
        Rental rental1 = new Rental(vehicle1, 15, member1);
        member1.getOrderHistory().add(rental1);

        Member member2 = membershipService.searchMemberNY("Eja");
        Vehicle vehicle2 = inventory.findVehicleByItemNr("250");
        Rental rental2 = new Rental(vehicle2, 20, member2);
        member2.getOrderHistory().add(rental2);

        Member member3 = membershipService.searchMemberNY("Ulla");
        Vehicle vehicle3 = inventory.findVehicleByItemNr("113");
        Rental rental3 = new Rental(vehicle3, 5, member3);
        member3.getOrderHistory().add(rental3);
    }

    public void mainMenuWindow(Stage stage) {
        stage.setTitle("HUVUDMENY");

        Label headingLabel = new Label("SHARED VEHICLES");
        headingLabel.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 26));
        headingLabel.setStyle("-fx-text-fill: #3A5A50");

        Label headingLabel2 = new Label("medlemsklubb med uthyrning av fordon");
        headingLabel2.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 18));
        headingLabel2.setStyle("-fx-text-fill: #3A5A50");

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
        addVehicleButton.setOnAction(e -> addVehicleWindow(stage));

        Button vehicleRegistryButton = new Button();
        vehicleRegistryButton.setText("Sök, ändra eller ta bort fordon");
        uiStyler.styleDropdownMenuButton(vehicleRegistryButton);
        //vehicleRegistryButton.setOnAction(e -> vehicleRegistryWindow(stage));     //Ska gå till den gemensamma

        ComboBox <Button> vehicleComboBox = new ComboBox<>();
        vehicleComboBox.getItems().addAll(addVehicleButton, vehicleRegistryButton);

        Label rentalHandlingLabel = new Label("Hantera uthyrning");
        uiStyler.styleMainMenuLabels(rentalHandlingLabel);

        Button bookRentalButton = new Button();
        bookRentalButton.setText("Boka uthyrning");
        uiStyler.styleDropdownMenuButton(bookRentalButton);
        bookRentalButton.setOnAction(e -> bookRentalsWindow(stage));

        Button ongoingRentalsButton = new Button();
        ongoingRentalsButton.setText("Pågående uthyrningar");
        uiStyler.styleDropdownMenuButton(ongoingRentalsButton);
        ongoingRentalsButton.setOnAction(e -> ongoingRentalWindow(stage));

        Button returnVehicleButton = new Button();
        returnVehicleButton.setText("Återlämna fordon");
        uiStyler.styleDropdownMenuButton(returnVehicleButton);
        returnVehicleButton.setOnAction(e -> returnVehicleWindow(stage));

        ComboBox<Button> rentalComboBox = new ComboBox<>();
        rentalComboBox.getItems().addAll(bookRentalButton, ongoingRentalsButton, returnVehicleButton);

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

    //***********MEDLEM*********
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

        Label personalIdNrLabel = new Label("Personnummer (ÅÅMMDD-XXXX) ");
        personalIdNrLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label firstNameLabel = new Label("Förnamn ");
        firstNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label lastNameLabel = new Label("Efternamn ");
        lastNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label membershipLevelLabel = new Label("Medlemsnivå (student, standard, premium) ");
        membershipLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField personalIdNrField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField membershipLevelField = new TextField();

        if (openMemberFormFromAddMemberWindow == false) {
        }

        else {
            personalIdNrField.setText(memberInstansvariabel.getPersonalIdNumber());
            firstNameField.setText(memberInstansvariabel.getFirstName());
            lastNameField.setText(memberInstansvariabel.getLastName());
            membershipLevelField.setText(memberInstansvariabel.getMembershipLevel());
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

            //**************EJ KLAR - MEN FUNKAR I KONSOLEN
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

            //**************FELHANTERING FÖR OM PNR-INPUT ÄR FEL******************
           //Säkerställer inte rätt format men iaf rätt antal tecken...
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
                memberInstansvariabel.setPersonalIdNumber(personalIdNrField.getText());
                memberInstansvariabel.setFirstName(firstNameField.getText());
                memberInstansvariabel.setLastName(lastNameField.getText());
                memberInstansvariabel.setMembershipLevel(membershipLevelField.getText());
                printMemberInfo(memberInstansvariabel, infoAboutMemberTextFlow);
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
                stage.setScene(previousScene);
                stage.setTitle(previousHeadline);
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

    public HBox memberRegistryGEMENSAM(Stage memberRegistryStage)   {

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #96ACA6;");

        Label headingLabel = new Label("Alla medlemmar");
        uiStyler.styleHeadingLabel(headingLabel);

        memberTableView = new TableView<>();
        memberTableView.setEditable(true);                                              //???????????
        memberTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);     //Eventuellt
        memberTableView.setId("customTable");
        memberTableView.getStylesheets().add("style.css");

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
            Member foundMember = membershipService.searchMemberNY(userInput);

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
                memberTableView.getSelectionModel().select(index);
                memberTableView.scrollTo(index);
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

                //Öppna orderhistorik i SAMMA stage
                orderHistoryButton.setOnAction(e -> orderHistoryWindow(memberRegistryStage));
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

        memberInfoIMedlemsregisterTextFlow.getChildren().clear();
        memberInfoIMedlemsregisterTextFlow.getChildren().add(headLineChosenMember);

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
                    memberInstansvariabel = selectedMember;

                    headLineChosenMember.setText("Vald medlem\n");
                    memberInfoIMedlemsregisterTextFlow.getChildren().setAll(headLineChosenMember);

                   printMemberInfo(selectedMember, memberInfoIMedlemsregisterTextFlow);
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

        memberTableView.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;"
        );

        membersObservableList = memberRegistry.getMembersObservableList();
        memberTableView.setItems(membersObservableList);

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(60);
        bottomHBox.setStyle("-fx-padding: 20");
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomHBox.setPrefHeight(200);
        bottomHBox.setMinHeight(Region.USE_PREF_SIZE);
        bottomHBox.setMaxHeight(Region.USE_PREF_SIZE);
        bottomHBox.getChildren().addAll(memberInfoIMedlemsregisterTextFlow);

        borderPane.setTop(onTopVBox);
        BorderPane.setMargin(onTopHBox, new Insets(10));
        borderPane.setCenter(memberTableView);
        borderPane.setBottom(bottomHBox);

        Scene scene = new Scene(borderPane, 800, 750);
        memberRegistryStage.setScene(scene);

        return bottomHBox;
    }

    public void bottomMemberRegistry(Stage stage, HBox bottomHBox) {

        memberRemovedLabel.setStyle("-fx-text-fill: #4F6F66;");
        memberRemovedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().add(memberInfoIMedlemsregisterTextFlow);

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

                if (memberInstansvariabel == null) {
                }

                else {

                    Alert confirmationAlert = new Alert(Alert.AlertType.WARNING);
                    confirmationAlert.setTitle("BORTTAG AV MEDLEM");
                    confirmationAlert.setHeaderText("Är du säker på att du vill ta bort medlemmen?");
                    confirmationAlert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                    confirmationAlert.getDialogPane().getStyleClass().add("remove-member-alert");
                    confirmationAlert.getButtonTypes().clear();

                    ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButton = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
                    confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

                    Optional<ButtonType> userChoice = confirmationAlert.showAndWait();

                    if (userChoice.isPresent() && userChoice.get() == okButton) {

                        memberRegistry.removeMemberFromRegistry(memberInstansvariabel);
                        memberInfoIMedlemsregisterTextFlow.getChildren().clear();

                        memberRemovedLabel.setText(
                                memberInstansvariabel.getFirstName() + " " +
                                        memberInstansvariabel.getLastName() +
                                        "\när borttagen\nfrån registret"
                        );
                    }
                }
            });

            Button changeButton = new Button("Ändra");
            uiStyler.styleOrdinaryButton(changeButton);
            changeButton.setOnAction(e -> {

                if (memberInstansvariabel == null) {
                }

                else {
                    memberTableView.refresh();
                    previousScene = stage.getScene();
                    previousHeadline = stage.getTitle();
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

        HBox hBox = memberRegistryGEMENSAM(stage);
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

        if (memberInstansvariabel == null) {
            //headingLabelName.setText("Du måste klicka på knappen Välj först");
            topVBox.getChildren().addAll(headingLabel, headingLabelName);

            borderPane.setTop(topVBox);

            Label infoLabel = new Label("Ingen medlem vald");
            infoLabel.setAlignment(Pos.CENTER);
            borderPane.setCenter(infoLabel);
        }

        else {

            headingLabelName.setText(memberInstansvariabel.getFirstName() +
                    " " + memberInstansvariabel.getLastName());

            topVBox.getChildren().addAll(headingLabel, headingLabelName);
            borderPane.setTop(topVBox);

            List<Rental> orderHistory = memberInstansvariabel.getOrderHistory();

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

//            TableColumn<Rental, String> startDateColumn = new TableColumn<>("Startdatum");
//            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
//
//            TableColumn<Rental, String> endDateColumn = new TableColumn<>("Slutdatum");
//            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

            orderHistoryTableView.getColumns().addAll(
                    vehicleTypeColumn, vehiclePriceColumn, vehicleBrandColumn,
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

            HBox bottomHBox = memberRegistryGEMENSAM(stage);
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
        stage.setScene(scene);   //samma Stage, ny vy
    }

    public void memberRegistryBook()  {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("MEDLEMSREGISTER");

        openMemberRegistryFromBookWindow = true;

        HBox bookHBox = memberRegistryGEMENSAM(stage);
        bookHBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

//        Button valdMedlemKlarButton = new Button("Klar");
//        createStuff.createButtonVanlig(valdMedlemKlarButton);
//        valdMedlemKlarButton.setOnAction(e -> stage.close());
            //*******INUTI DENNA ACTIONEVENT*********
            //   - Felhantering, "Du har inte valt medlem"
           //valdMedlemKlarButton.setDisable(true);
           //valdMedlemKlarButton.setDisable(false);

        bottomMemberRegistry(stage, bookHBox);
        stage.showAndWait();
    }

    //********FORDON**************
    public void addVehicleWindow(Stage stage) {
        stage.setTitle("LÄGG TILL FORDON ");

        Label headingLabel = new Label("Lägga till fordon");
        uiStyler.styleHeadingLabel(headingLabel);

        //Om felhantering.
        //- Inte kunna använda ta samma artikelnr igen.
        //Jämför mot vilka artikelnr som finns, loopa igenom
        //Om userInputItemNr == itemNumber "Finns redan, välj nytt."

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void vehicleRegistryGEMENSAM(Stage stage) {

        Stage vehicleRegistryStage = new Stage();
        vehicleRegistryStage.initModality(Modality.APPLICATION_MODAL);      //Detta hindrar klick i huvudfönstret
        vehicleRegistryStage.setTitle("FORDONSREGISTER ");

        Label headingLabel = new Label("Alla fordon");
        uiStyler.styleHeadingLabel(headingLabel);

        //************TRAILERS************
        TableView<Trailer> trailersTableView = new TableView();      //Metod som fixar?
        trailersTableView.setEditable(false);                         //VAD GÖR DENNA
        trailersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vBoxTrailer = new VBox();
        vBoxTrailer.getChildren().addAll(trailersTableView);
        vBoxTrailer.setPadding(new Insets(10, 10, 0, 10));

        TableColumn<Trailer, String> brandColumn = new TableColumn("Märke");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Trailer, Number> priceColumn = new TableColumn("Pris (kr/dag)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Trailer, Number> lengthColumn = new TableColumn("Längd (cm)");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<Trailer, Number> widthColumn = new TableColumn("Bredd (cm)");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Trailer, Number> itemNumberColumn = new TableColumn("Artikelnummer");
        itemNumberColumn.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));

        Text headLineChosenVehicle = new Text("");
        headLineChosenVehicle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headLineChosenVehicle.setFill(Color.web("#2E2E2E"));

        //VARIABELNAMN
        TextFlow textFlowNU = new TextFlow();
        textFlowNU.getChildren().add(headLineChosenVehicle);

        TableColumn<Trailer, Void> selectButtonColumn = new TableColumn<>("Välj");
        selectButtonColumn.setCellFactory(col -> new TableCell<Trailer, Void>() {
            Button selectButton = new Button("Välj");
            HBox buttons = new HBox(5);
            {
                buttons.setAlignment(Pos.CENTER);
                buttons.getChildren().addAll(selectButton);
                selectButton.setOnAction(e -> {

                    Trailer selectedTrailer = getTableView().getItems().get(getIndex());
                    vehicleInstansvariabel = selectedTrailer;
                    headLineChosenVehicle.setText("Valt fordon");
                    printVehicleInfo(vehicleInstansvariabel, textFlowNU);


                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    setGraphic(buttons);
                }
            }
        });

        trailersTableView.getColumns().addAll(brandColumn, priceColumn, lengthColumn, widthColumn,
                itemNumberColumn, selectButtonColumn);
        trailersTableView.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;"
        );

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("spaced-tabs");
        tabPane.setStyle("-fx-padding: 10");

        Tab trailersTab = new Tab();
        Label trailersLabel = new Label("Släpvagnar");
        uiStyler.styleTabs(trailersLabel, trailersTab);
        trailersTab.setGraphic(trailersLabel);
        trailersTab.setContent(vBoxTrailer);

        List<Trailer> trailerList = rentalService.filterItemsEndastTrailersDEN_NYA();
        ObservableList<Trailer> trailers = FXCollections.observableArrayList(trailerList);
        trailersTableView.setItems(trailers);

        //************LAWNMOWERS************
        TableView<LawnMower> lawnMowersTableView = new TableView();      //Metod som fixar?
        lawnMowersTableView.setEditable(false);                         //VAD GÖR DENNA
        lawnMowersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vBoxLawnMower = new VBox();
        vBoxLawnMower.getChildren().addAll(lawnMowersTableView);
        vBoxLawnMower.setPadding(new Insets(10, 10, 0, 10));

        Tab lawnMowersTab = new Tab();
        Label lawnMowersLabel = new Label("Gräsklippare");
        uiStyler.styleTabs(lawnMowersLabel, lawnMowersTab);
        lawnMowersTab.setGraphic(lawnMowersLabel);
        lawnMowersTab.setContent(lawnMowersTableView);

        //************ALLA************
        Tab allVehiclesTab = new Tab();
        Label allVehiclesLabel = new Label("Alla fordon");
        uiStyler.styleTabs(allVehiclesLabel, allVehiclesTab);
        allVehiclesTab.setGraphic(allVehiclesLabel);
        //***********************

        tabPane.getTabs().addAll(trailersTab, lawnMowersTab, allVehiclesTab);

        Label alreadyRentedLabel = new Label("");

        Button valtFordonKlarButton = new Button("Klar");
        uiStyler.styleOrdinaryButton(valtFordonKlarButton);
        valtFordonKlarButton.setOnAction(e -> {

             //*******INUTI DENNA ACTIONEVENT*********
            //Felhantering, "Du har inte valt något fordon"

            if (rentalService.checkIfRentedOut(vehicleInstansvariabel)) {
                alreadyRentedLabel.setText("Fordonet är redan uthyrt. Välj ett annat.");
                //Den här kollen borde väl ligga vid Välj-lnappen ist. Här måste det väl iuofs också vara ngn slags koll.
                //Ska inte kunna trycka på Klar förrän valt fordon som inte är bokat.
            }

            else {
                vehicleRegistryStage.close();
            }
        });

        HBox hBox = new HBox();
        hBox.setSpacing(70);
        hBox.setStyle("-fx-padding: 20");
        hBox.setAlignment(Pos.BOTTOM_RIGHT);            //HÄR HÄR IN MED TEXTFLOW
        hBox.getChildren().addAll(headLineChosenVehicle, textFlowNU, alreadyRentedLabel, valtFordonKlarButton);

        BorderPane borderPaneMain = new BorderPane();
        borderPaneMain.setTop(headingLabel);
        BorderPane.setAlignment(headingLabel, Pos.CENTER);
        BorderPane.setMargin(headingLabel, new Insets(10));
        borderPaneMain.setCenter(tabPane);
        borderPaneMain.setBottom(hBox);

        Scene scene = new Scene(borderPaneMain, 800, 750);
        vehicleRegistryStage.setScene(scene);
        vehicleRegistryStage.showAndWait();         //showAndWait låser huvudfönstret.
    }

    public void vehicleRegistrySearchChange()   {
    }

    public void vehicleRegistryBook()   {

    }

    //********UTHYRNING**************
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

            vehicleRegistryGEMENSAM(stage);
            printVehicleInfo(vehicleInstansvariabel, vehicleSummaryTextFlow);
            headLineChosenVehicle.setText("Valt fordon\n");
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

        //Ta bort den här knappen? Använder den ju inte nu.
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
               fromDateInstansvariabel = fromDatePicker.getValue();
               toDateInstansvariabel = toDatePicker.getValue();

               numberOfDays = (int) ChronoUnit.DAYS.between(fromDateInstansvariabel, toDateInstansvariabel);

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
                printMemberInfo(memberInstansvariabel, memberSummaryTextFlow);
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
        printReceiptButton.setOnAction(e -> receiptPopUp(stage));

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

            rentalInstansvariabel = rentalService.addBooking(vehicleInstansvariabel, numberOfDays, memberInstansvariabel);
            finalPriceInstansvariabel = rentalService.calculatePrice(rentalInstansvariabel);
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

    public void receiptPopUp(Stage stage) {

        Stage receiptStage = new Stage();
        receiptStage.initModality(Modality.APPLICATION_MODAL);
        receiptStage.setTitle("KVITTO");

        Label headingLabel = new Label("SHARED VEHICLES");
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label receiptLabel = new Label("KVITTO");
        receiptLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 16));
        Label starsLabel1 = new Label("********************************************");
        starsLabel1.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));

        Label vehicleTypeBrandLabel = new Label(vehicleInstansvariabel.getVehicleType() + " " + vehicleInstansvariabel.getBrand());
        vehicleTypeBrandLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label daysAndPriceLabel = new Label();
        daysAndPriceLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label amountLabel = new Label("       " + rentalInstansvariabel.getNumberOfDays() + " dag * " +
                vehicleInstansvariabel.getPrice() + " kr     " + finalPriceInstansvariabel + " kr");
        amountLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));

        Label starsLabel2 = new Label("********************************************");
        starsLabel2.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label totalAmountLabel = new Label("          Totalt      " + finalPriceInstansvariabel + " kr");
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

    public void ongoingRentalWindow(Stage stage) {
        stage.setTitle("PÅGÅENDE UTHYRNINGAR");

        Label headingLabel = new Label("Uthyrda fordon just nu");
        uiStyler.styleHeadingLabel(headingLabel);

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void returnVehicleWindow(Stage stage) {
        stage.setTitle("ÅTERLÄMNA FORDON");

        Label headingLabel = new Label("Återlämna fordon");
        uiStyler.styleHeadingLabel(headingLabel);

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    //********EKONOMI************
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

    //********PRINT-METODER
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

        Text topLine = new Text("──────────────────\n");
        topLine.setFill(Color.web("#6F8F86"));

        Text typeAndBrand = new Text(vehicle.getVehicleType() + " (" + vehicle.getBrand() + ")\n");
        typeAndBrand.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        typeAndBrand.setFill(Color.web("#1F2A26"));

        Text price = new Text(vehicle.getPrice() + " kr/dag\n\n");
        price.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        price.setFill(Color.web("#4F6F66"));

        Text itemNr = new Text("Artikelnr: " + vehicle.getItemNumber() + "\n\n");
        itemNr.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        itemNr.setFill(Color.web("#2E2E2E"));

        Text bottomLine = new Text("\n──────────────────");
        bottomLine.setFill(Color.web("#6F8F86"));

        textFlow.getChildren().addAll(topLine, typeAndBrand, price, itemNr, bottomLine);
    }

    public void printDateInfo(int numberOfDays, TextFlow textFlow)    {

        textFlow.setLineSpacing(8);

        Text topLine = new Text("────────────────────────\n");
        topLine.setFill(Color.web("#6F8F86"));

        Text fromToDate = new Text(fromDateInstansvariabel + " - " + toDateInstansvariabel + "\n");
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
        bookedVehicleText.setText("      " + vehicleInstansvariabel.getVehicleType() + " (" + vehicleInstansvariabel.getBrand() + ")");

        Text bookedDateHeadLine = new Text();
        bookedDateHeadLine.setFont(Font.font("Apple Color Emoji", FontWeight.BOLD, 20));
        bookedDateHeadLine.setText("\n\n  🗓️ Period\n");
        Text bookedDateText = new Text();
        bookedDateText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        bookedDateText.setText("    " + fromDateInstansvariabel + " - " + toDateInstansvariabel +
                    " (" + numberOfDays + " dagar)");

        Text bookedMemberHeadLine = new Text();
        bookedMemberHeadLine.setFont(Font.font("Apple Color Emoji", FontWeight.BOLD, 20));
        bookedMemberHeadLine.setText("\n\n  👤 Medlem\n");

        Text bookedMemberText = new Text();
        bookedMemberText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        bookedMemberText.setText("   " + memberInstansvariabel.getFirstName() + " " +
                    memberInstansvariabel.getLastName() + " (" + memberInstansvariabel.getPersonalIdNumber() + ")");

        Text priceHeadLine = new Text();
        priceHeadLine.setFont(Font.font("Apple Color Emoji", FontWeight.BOLD, 20));
        priceHeadLine.setText("\n\n  💵 Totalpris\n");

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("sv", "SE"));
        Text priceText = new Text();
        priceText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 14));
        priceText.setText("                   " + numberFormat.format(finalPriceInstansvariabel) + " kr");

        Text bottomLine = new Text("\n──────────────────────────────────");
        bottomLine.setStyle("-fx-text-fill: #6F8F86;");

        textFlow.getChildren().addAll(topLine, bookedVehicleHeadLine, bookedVehicleText,
                bookedDateHeadLine, bookedDateText, bookedMemberHeadLine,
                bookedMemberText, priceHeadLine, priceText, bottomLine);
    }


    //******SLASK***************
    public void searchVehicleWindow(Stage stage) {
        stage.setTitle("SÖK FORDON ");

        Label headingLabel = new Label("Sök efter fordon");
        uiStyler.styleHeadingLabel(headingLabel);

        //Hur ska man kunna söka?

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }
    public void deleteVehicleWindow(Stage stage) {
        stage.setTitle("TA BORT (ÄNDRA?) FORDON ");

        Label headingLabel = new Label("Ta bort (eller ändra)");
        uiStyler.styleHeadingLabel(headingLabel);

        Button changeInfoVehicle = new Button("Ändra");
        uiStyler.styleOrdinaryButton(changeInfoVehicle);

        Button deleteVehicle = new Button("Ta bort");
        uiStyler.styleOrdinaryButton(deleteVehicle);
        deleteVehicle.setOnAction(e -> {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("VAD SKA STÅ HÄR?");
            confirmationAlert.setHeaderText("Ta bort fordon");
            confirmationAlert.setContentText("Är du säker på att du vill ta bort fordon?");
            confirmationAlert.showAndWait();
        });

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        borderPane.setRight(deleteVehicle);          //Ligger här tillfälligt nu
        borderPane.setLeft(changeInfoVehicle);       //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);
        //borderPane.setBottom(hBox);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }
    public void changeInfoMemberWindow (Stage stage)    {
        stage.setTitle("ÄNDRA UPPGIFT OM MEDLEM");

        Label headingLabel = new Label("Sök efter medlem som ska ändras");
        uiStyler.styleHeadingLabel(headingLabel);

        //Sen komma till metoden searchForMember, samma som används för
        //att söka på "vanligt" sätt?

        Button changeInfoMember = new Button("Ändra");
        uiStyler.styleOrdinaryButton(changeInfoMember);

        Button deleteMember = new Button("Ta bort");
        uiStyler.styleOrdinaryButton(deleteMember);
        deleteMember.setOnAction(e -> {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("VAD SKA STÅ HÄR?");
            confirmationAlert.setHeaderText("Ta bort medlem");
            confirmationAlert.setContentText("Är du säker på att du vill ta bort medlem?");
            confirmationAlert.showAndWait();

        });

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        borderPane.setLeft(changeInfoMember);       //Ligger här tillfälligt nu
        borderPane.setRight(deleteMember);          //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }
    public void addMemberWindowGAMMAL(Stage stage) {

        stage.setTitle("MEDLEMSFORMULÄR");

        Label headingLabel = new Label("Fyll i uppgifter om medlem");
        uiStyler.styleHeadingLabel(headingLabel);

        Label personalIdNrLabel = new Label("Personnummer (ÅÅMMDD-XXXX) ");
        personalIdNrLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label firstNameLabel = new Label("Förnamn ");
        firstNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label lastNameLabel = new Label("Efternamn ");
        lastNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label membershipLevelLabel = new Label("Medlemsnivå (student, standard, premium) ");
        membershipLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField personalIdNrField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField membershipLevelField = new TextField();

        Label savedStatusLabel = new Label();
        savedStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextFlow infoAboutMemberTextFlow = new TextFlow();

        Button saveButton = new Button("Spara");
        uiStyler.styleOrdinaryButton(saveButton);
        saveButton.setOnAction(e -> {

            infoAboutMemberTextFlow.getChildren().clear();

            //Sänga in det här i ngn metod för stt slippa upprepa.
            //I en klass som grejar med felhantering??
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

            //**************EJ KLAR - MEN FUNKAR I KONSOLEN
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

            //**************FELHANTERING FÖR OM PNR-INPUT ÄR FEL******************
           //Säkerställer inte rätt format men iaf rätt antal tecken...
            String characters = personalIdNrField.getText();
            characters.length();
            int countCharacters = characters.length();
            System.out.println(countCharacters);

            if (countCharacters < 11 || countCharacters > 11) {
                System.out.println("För få eller för många tecken");
            }

            Member member = new Member(
                    personalIdNrField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    membershipLevelField.getText());

            memberRegistry.addMemberToRegistry(member);

            savedStatusLabel.setStyle("-fx-text-fill: #2e2e2e");
            savedStatusLabel.setText("Följande uppgifter är sparade");
            printMemberInfo(member, infoAboutMemberTextFlow);

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

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(hBox);

        Scene scene = uiStyler.styleWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }
    public void MEDLEMSREGISTER_NY (Stage stage)   {

//        Stage memberRegistryStage = new Stage();
//        memberRegistryStage.initModality(Modality.APPLICATION_MODAL);      //Detta hindrar klick i huvudfönstret
//        memberRegistryStage.setTitle("MEDLEMSREGISTER ");
//
//        Label headingLabel = new Label("Alla medlemmar");
//        uiStyler.styleHeadingLabel(headingLabel);
//
//        TextField searchTextField = new TextField();
//        searchTextField.setPromptText("Sök medlem");
//
//        Button searchButton = new Button("Sök");
//        searchButton.setPrefSize(100, 25);
//        searchButton.setStyle("-fx-background-radius: 5;");
//        searchButton.setOnAction(e -> {
//            //KOD SOM SKA SÖKA BLAND MEDLEMMAR. SKICKAS TILL METOD I MembershipService?
//            //membershipService.searchForMember();
//        });
//
//        HBox onTopHBox = new HBox(searchTextField, searchButton);
//        onTopHBox.setSpacing(20);
//
//        VBox onTopVBox = new VBox();
//        onTopVBox.setSpacing(10);
//        onTopVBox.setAlignment(Pos.CENTER);
//        onTopVBox.setPadding(new Insets(10));
//        onTopVBox.getChildren().addAll(headingLabel, onTopHBox);
//
//        TableView <Member> memberTableView = new TableView<>();
//        memberTableView.setEditable(true);                                              //???????????
//        memberTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);     //Eventuellt
//
//        TableColumn<Member, String> firstNameColumn = new TableColumn<>("Förnamn");
//        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//        uiStyler.styleTableColumn(firstNameColumn);
//        TableColumn<Member, String> lastNameColumn = new TableColumn<>("Efternamn");
//        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//        uiStyler.styleTableColumn(lastNameColumn);
//        TableColumn<Member, String> personalIdNumberColumn = new TableColumn<>("Personnummer");
//        personalIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("personalIdNumber"));
//        uiStyler.styleTableColumn(personalIdNumberColumn);
//        TableColumn<Member, String> membershipLevelColumn = new TableColumn<>("Medlemsnivå");
//        membershipLevelColumn.setCellValueFactory(new PropertyValueFactory<>("membershipLevel"));
//        uiStyler.styleTableColumn(membershipLevelColumn);
//
//        TableColumn<Member, Void> orderHistoryColumn = new TableColumn<>("Orderhistorik");
//        orderHistoryColumn.setCellFactory(col -> new TableCell<Member, Void>() {
//
//            Button orderHistoryButton = new Button("Klicka");
//            HBox orderHistoryHBox = new HBox(5);
//                {
//                    orderHistoryHBox.setAlignment(Pos.CENTER);
//                    orderHistoryHBox.getChildren().addAll(orderHistoryButton);
//                    orderHistoryButton.setOnAction(e -> {
//                        //KOD FÖR att komma åt ORDERHISTORIK, metodanrop!
//                    });
//                }
//                @Override
//                protected void updateItem (Void item, boolean empty)    {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(orderHistoryHBox);
//                }
//            }
//        });
//
//        Label showChosenMemberLabel = new Label("");
//
//        TableColumn<Member, Void> selectMemberColumn = new TableColumn<>("Välj medlem");
//        selectMemberColumn.setCellFactory(col -> new TableCell<Member, Void>() {
//
//            Button selectMemberButton = new Button("Välj");
//            HBox selectMemberHBox = new HBox(5);
//                {
//                    selectMemberHBox.setAlignment(Pos.CENTER);
//                    selectMemberHBox.getChildren().addAll(selectMemberButton);
//                    selectMemberButton.setOnAction(e -> {
//
//                    Member selectedMember = getTableView().getItems().get(getIndex());
//                    memberInstansvariabel = selectedMember;
//                    showChosenMemberLabel.setText(memberInstansvariabel.toString());
//                    });
//                }
//                @Override
//                protected void updateItem (Void item, boolean empty)    {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(selectMemberHBox);
//                }
//            }
//        });
//
//        memberTableView.getColumns().addAll(firstNameColumn, lastNameColumn,
//                personalIdNumberColumn, membershipLevelColumn, orderHistoryColumn, selectMemberColumn);
//        memberTableView.setStyle(
//            "-fx-border-color: black;" +
//                    "-fx-border-width: 1;"
//            // +
//            //"-fx-padding: 5;"
//        );
//
//        //List<Member> memberList = memberRegistry.getMemberList();
//        membersObservableList = memberRegistry.getMembersObservableList();
//        memberTableView.setItems(membersObservableList);
//
//
//        Button valdMedlemKlarButton = new Button("Klar");
//        uiStyler.styleOrdinaryButton(valdMedlemKlarButton);
//        valdMedlemKlarButton.setOnAction(e -> memberRegistryStage.close());
//            //*******INUTI DENNA ACTIONEVENT*********
//            //   - Felhantering, "Du har inte valt medlem"
//           //valdMedlemKlarButton.setDisable(true);
//           //valdMedlemKlarButton.setDisable(false);
//
//        HBox bottomHBox = new HBox();
//        bottomHBox.setSpacing(70);
//        bottomHBox.setStyle("-fx-padding: 20");
//        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
//        bottomHBox.getChildren().addAll(showChosenMemberLabel, valdMedlemKlarButton);
//
//        BorderPane borderPane = new BorderPane();
//        borderPane.setTop(onTopVBox);
//        BorderPane.setMargin(onTopHBox, new Insets(10));
//        borderPane.setCenter(memberTableView);
//        borderPane.setBottom(bottomHBox);
//
//        Scene scene = new Scene(borderPane, 800, 750);
//        memberRegistryStage.setScene(scene);
//        memberRegistryStage.showAndWait();         //showAndWait låser huvudfönstret.
    }
    public void vehicleRegistryWindow(Stage stage) {
        stage.setTitle("FORDONSREGISTER ");

        Label headingLabel = new Label("Alla fordon");
        uiStyler.styleHeadingLabel(headingLabel);

        //************TRAILERS************

        TableView <Trailer> trailersTableView = new TableView();      //Metod som fixar?
        trailersTableView.setEditable(false);                         //VAD GÖR DENNA
        trailersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vBoxTrailer = new VBox();
        vBoxTrailer.getChildren().addAll(trailersTableView);
        vBoxTrailer.setPadding(new Insets(10, 10, 0, 10));

        TableColumn<Trailer, String> brandColumn = new TableColumn("Märke");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn <Trailer, Number> priceColumn = new TableColumn("Pris (kr/dag)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn <Trailer, Number> lengthColumn = new TableColumn("Längd (cm)");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn <Trailer, Number> widthColumn = new TableColumn("Bredd (cm)");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn <Trailer, Number> itemNumberColumn = new TableColumn("Artikelnummer");
        itemNumberColumn.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));

        trailersTableView.getColumns().addAll(brandColumn, priceColumn, lengthColumn, widthColumn, itemNumberColumn);
        trailersTableView.setStyle("-fx-border-color: black;" + "-fx-border-width: 1;");

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("spaced-tabs");
        tabPane.setStyle("-fx-padding: 10");

        Tab trailersTab = new Tab();
        Label trailersLabel = new Label("Släpvagnar");
        uiStyler.styleTabs(trailersLabel, trailersTab);
        trailersTab.setGraphic(trailersLabel);
        trailersTab.setContent(vBoxTrailer);                //VBOXEN HÄR

        List<Trailer> trailerList = rentalService.filterItemsEndastTrailersDEN_NYA();
        ObservableList<Trailer> trailers = FXCollections.observableArrayList(trailerList);
        trailersTableView.setItems(trailers);

        //************LAWNMOWERS************

        TableView <LawnMower> lawnMowersTableView = new TableView();      //Metod som fixar?
        lawnMowersTableView.setEditable(false);                         //VAD GÖR DENNA
        lawnMowersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vBoxLawnMower = new VBox();
        vBoxLawnMower.getChildren().addAll(lawnMowersTableView);
        vBoxLawnMower.setPadding(new Insets(10, 10, 0, 10));

        Tab lawnMowersTab = new Tab();
        Label lawnMowersLabel = new Label("Gräsklippare");
        uiStyler.styleTabs(lawnMowersLabel, lawnMowersTab);
        lawnMowersTab.setGraphic(lawnMowersLabel);
        lawnMowersTab.setContent(lawnMowersTableView);

        //************ALLA************

        Tab allVehiclesTab = new Tab();
        Label allVehiclesLabel = new Label("Alla fordon");
        uiStyler.styleTabs(allVehiclesLabel, allVehiclesTab);
        allVehiclesTab.setGraphic(allVehiclesLabel);

         //***********************

        tabPane.getTabs().addAll(trailersTab, lawnMowersTab, allVehiclesTab);

        Button returnToMenuButton = uiStyler.styleReturnToMenuButton();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPaneMain = new BorderPane();
        borderPaneMain.setTop(headingLabel);
        BorderPane.setAlignment(headingLabel, Pos.CENTER);
        BorderPane.setMargin(headingLabel, new Insets(10));
        borderPaneMain.setCenter(tabPane);

        Scene scene = uiStyler.styleWindow(hBox, borderPaneMain);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
         launch(args);
    }
}


