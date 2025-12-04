package com.linnea;

import com.linnea.entity.*;
import com.linnea.service.MembershipService;
import com.linnea.service.RentalService;
import com.linnea.util.CreateStuff;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    List<Member> memberList = new ArrayList<>();
    List<Vehicle> inventoryList = new ArrayList<>();
    List<Rental> rentalList = new ArrayList<>();

    CreateStuff createStuff = new CreateStuff();

    MemberRegistry memberRegistry;
    MembershipService membershipService;
    Inventory inventory;
    RentalService rentalService;

    public int numberOfDays;
    public Vehicle vehicleInstansvariabel;
    public Member memberInstansvariabel;

    @Override
    public void start(Stage stage)/* throws Exception*/ {
        initialValuesLists();
        mainMenuWindow(stage);
    }

    public void initialValuesLists()    {

        memberList.add(new Member("851103-7841", "Eja", "Bylund", "Premium"));
        memberList.add(new Member("050119-8765", "Emma", "Hagman", "Student"));
        memberList.add(new Member("750110-7888", "Sara", "Stenlund", "Standard"));
        memberList.add(new Member("020705-7465", "Jennie", "Larsson", "Student"));
        memberList.add(new Member("890305-7458", "Emil", "Gustafsson", "Premium"));

        memberRegistry = new MemberRegistry(memberList);
        membershipService = new MembershipService(memberRegistry);

        inventoryList.add(new Trailer(400, "Thule", "145", 325, 152));
        inventoryList.add(new Trailer(350, "Fogelsta", "117", 258, 128));
        inventoryList.add(new Trailer(450, "Tikin", "250", 240, 170));
        inventoryList.add(new Trailer(400, "Brenderup", "314", 200, 110));
        inventoryList.add(new RideOnLawnMower(350, "Husqvarna", "125", 180));
        inventoryList.add(new RideOnLawnMower(250, "Husqvarna", "215", 145));
        inventoryList.add(new RoboticLawnMower(300, "Husqvarna", "205", 60, 2000));
        inventoryList.add(new RoboticLawnMower(200, "Klippo", "113", 75, 1000));

        inventory = new Inventory(inventoryList);

        Member member = memberRegistry.findMember("Emma");
        Vehicle vehicle = inventory.findItem("205");
        rentalList.add(new Rental(vehicle, 5, member));
        member.getOrderHistory().add(new Rental(vehicle, 5, member));
        Member member2 = memberRegistry.findMember("Emil");
        Vehicle vehicle2 = inventory.findItem("125");
        rentalList.add(new Rental(vehicle2, 8, member2));
        member2.getOrderHistory().add(new Rental(vehicle2, 8, member2));

        rentalService = new RentalService(membershipService, inventory, rentalList);
    }

    public void mainMenuWindow(Stage stage) {
        stage.setTitle("HUVUDMENY");

        System.out.println(inventoryList.size());           //TA BORT SEN
        System.out.println(memberList.size());              //TA BORT SEN
        System.out.println(rentalList.size());              //TA BORT SEN

        Label headingLabel = new Label("NAMN PÅ MEDLEMSKLUBBEN");
        createStuff.createHeaderLabel(headingLabel);

        Label memberHandlingLabel = new Label("Hantera medlem");
        memberHandlingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Button memberRegistryButton = new Button("Medlemsregister");
        createStuff.createButtonRullgardinsmeny(memberRegistryButton);
        memberRegistryButton.setOnAction(e -> memberRegistryWindow(stage));

        Button addMemberButton = new Button();
        addMemberButton.setText("Lägg till medlem");
        createStuff.createButtonRullgardinsmeny(addMemberButton);
        addMemberButton.setOnAction(e -> addMemberWindow(stage));

        Button searchMemberButton = new Button();
        searchMemberButton.setText("Sök uppgift om medlem");
        createStuff.createButtonRullgardinsmeny(searchMemberButton);
        searchMemberButton.setOnAction(e -> searchMemberWindow(stage));

        Button changeInfoMemberButton = new Button();
        changeInfoMemberButton.setText("Ändra uppgift om medlem");
        createStuff.createButtonRullgardinsmeny(changeInfoMemberButton);
        changeInfoMemberButton.setOnAction(e -> changeInfoMemberWindow(stage));

        ComboBox memberComboBox = new ComboBox();
        memberComboBox.getItems().addAll(memberRegistryButton, addMemberButton, searchMemberButton, changeInfoMemberButton);

        Label vehicleHandlingLabel = new Label("Hantera fordon");
        vehicleHandlingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Button vehicleRegistryButton = new Button();
        vehicleRegistryButton.setText("Fordonsregister");
        createStuff.createButtonRullgardinsmeny(vehicleRegistryButton);
        vehicleRegistryButton.setOnAction(e -> vehicleRegistryWindow(stage));

        Button addVehicleButton = new Button();
        addVehicleButton.setText("Lägg till fordon");
        createStuff.createButtonRullgardinsmeny(addVehicleButton);
        addVehicleButton.setOnAction(e -> addVehicleWindow(stage));

        Button searchVehicleButton = new Button();
        searchVehicleButton.setText("Sök uppgift om fordon");
        createStuff.createButtonRullgardinsmeny(searchVehicleButton);
        searchVehicleButton.setOnAction(e -> searchVehicleWindow(stage));

        Button deleteVehicleButton = new Button();
        deleteVehicleButton.setText("Ta bort (och ändra?) fordon");
        createStuff.createButtonRullgardinsmeny(deleteVehicleButton);
        deleteVehicleButton.setOnAction(e -> deleteVehicleWindow(stage));

        ComboBox vehicleComboBox = new ComboBox();
        vehicleComboBox.getItems().addAll(vehicleRegistryButton, addVehicleButton, searchVehicleButton, deleteVehicleButton);

        Label rentalHandlingLabel = new Label("Hantera uthyrning");
        rentalHandlingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Button bookRentalButton = new Button();
        bookRentalButton.setText("Boka uthyrning");
        createStuff.createButtonRullgardinsmeny(bookRentalButton);
        bookRentalButton.setOnAction(e -> bookRentalsWindow(stage));

        Button ongoingRentalsButton = new Button();
        ongoingRentalsButton.setText("Pågående uthyrningar");
        createStuff.createButtonRullgardinsmeny(ongoingRentalsButton);
        ongoingRentalsButton.setOnAction(e -> ongoingRentalWindow(stage));

        Button returnVehicleButton = new Button();
        returnVehicleButton.setText("Återlämna fordon");
        createStuff.createButtonRullgardinsmeny(returnVehicleButton);
        returnVehicleButton.setOnAction(e -> returnVehicleWindow(stage));

        //ComboBox rentalComboBox = new ComboBox();
        ComboBox<Button> rentalComboBox = new ComboBox<>();
        rentalComboBox.getItems().addAll(bookRentalButton, ongoingRentalsButton, returnVehicleButton);

        Label economyLabel = new Label("Hantera ekonomi");
        economyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        //economyLabel.setStyle("-fx-padding: 30");

        Button sumRevenueButton = new Button();
        sumRevenueButton.setText("Summera intäkter");
        createStuff.createButtonRullgardinsmeny(sumRevenueButton);
        sumRevenueButton.setOnAction(e -> sumRevenueWindow(stage));

        ComboBox economyComboBox = new ComboBox();
        //economyComboBox.setStyle("-fx-border-color: #85a199" + "-fx-border-width: 1.5;");
        //economyComboBox.setStyle("-fx-background-color: #82aaff; -fx-text-fill: white;");
//        economyComboBox.setStyle("-fx-background-color: #85a199;" +
//    "-fx-background-radius: 10;" +
//    "-fx-border-color: black;" +
//    "-fx-border-width: 1;" +
//    "-fx-border-radius: 10;");

        economyComboBox.getItems().addAll(sumRevenueButton);

        Button quitButton = new Button("Avsluta");
        createStuff.createButtonVanlig(quitButton);
        quitButton.setOnAction(e -> System.exit(0));

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-padding: 30");
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.add(headingLabel, 0, 0);
        gridPane.add(memberHandlingLabel, 0, 3);
        gridPane.add(memberComboBox, 0, 4);
        gridPane.add(vehicleHandlingLabel, 0, 7);
        gridPane.add(vehicleComboBox, 0, 8);
        gridPane.add(rentalHandlingLabel, 0, 11);
        gridPane.add(rentalComboBox, 0, 12);
        gridPane.add(economyLabel, 0, 15);
        gridPane.add(economyComboBox, 0, 16);

        Label wigellLabel = new Label("Skapat av Wigellkoncernen");
        wigellLabel.setFont(Font.font("Nanum Gothic", FontWeight.BOLD, 10));

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(145);
        bottomHBox.getChildren().addAll(wigellLabel, quitButton);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setRight(bottomHBox);

        BorderPane borderPaneMain = new BorderPane();
        borderPaneMain.setCenter(gridPane);
        borderPaneMain.setBottom(bottomPane);

        Scene scene = createStuff.createWindow(bottomHBox, borderPaneMain);
        stage.setScene(scene);
        stage.show();
    }

    public void addMemberWindow(Stage stage) {

        stage.setTitle("MEDLEMSFORMULÄR");

        Label headingLabel = new Label("Fyll i uppgifter om medlem");
        createStuff.createHeaderLabel(headingLabel);

        Label personalIdNrLabel = new Label("Personnummer (ÅÅMMDD-XXXX) ");
        personalIdNrLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label firstNameLabel = new Label("Förnamn ");
        firstNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label lastNameLabel = new Label("Efternamn ");
        lastNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label membershipLevelLabel = new Label("Medlemsnivå (student, standard, premium) ");
        membershipLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        //Måste säkerställa att input blir rätt antal siffror
        TextField personalIdNrField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        //Måste säkerställa att input blir antingen standard, student eller premium.
        TextField membershipLevelField = new TextField();

        Label savedStatusLabel = new Label();
        savedStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextFlow medlemmensUppgifterTextFlow = new TextFlow();
        medlemmensUppgifterTextFlow.setLineSpacing(5);

        Button saveButton = new Button("Spara");
        createStuff.createButtonVanlig(saveButton);
        saveButton.setOnAction(e -> {

            if (personalIdNrField.getText().isEmpty() ||
                    firstNameField.getText().isEmpty() ||
                    lastNameField.getText().isEmpty()  ||
                    membershipLevelField.getText().isEmpty()) {

                savedStatusLabel.setText("Du har inte fyllt i alla fält!");
                savedStatusLabel.setStyle("-fx-text-fill: red");

                personalIdNrField.setStyle("-fx-border-color: red; -fx-border-width: 2;");

                return;
            }

            Member member = new Member(
                    personalIdNrField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    membershipLevelField.getText());

            memberList.add(member);

            savedStatusLabel.setStyle("-fx-text-fill: black");
            savedStatusLabel.setText("Följande uppgifter är sparade");

            printInfoAboutMember(member, medlemmensUppgifterTextFlow);

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
        gridPane.add(savedStatusLabel, 0, 13);
        gridPane.add(medlemmensUppgifterTextFlow, 0, 14);

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(hBox);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();

    }

    public void memberRegistryWindow(Stage stage)    {

        stage.setTitle("MEDLEMSREGISTER");

        Label headingLabel = new Label("Registrerade medlemmar");
        createStuff.createHeaderLabel(headingLabel);
        headingLabel.setStyle("-fx-padding: 30");
        headingLabel.setAlignment(Pos.CENTER);

         if (memberList.isEmpty()) {
            headingLabel.setText("Saknas registrerade medlemmar");
        }

        TextFlow medlemmensUppgifterTextFlow = new TextFlow();
        medlemmensUppgifterTextFlow.setLineSpacing(5);

        for (Member member : memberList) {
            printInfoAboutMember(member, medlemmensUppgifterTextFlow);
        }

        ScrollPane scrollPane = new ScrollPane(medlemmensUppgifterTextFlow);

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);
        borderPane.setCenter(scrollPane);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void orderHistoryWindow(Stage stage) {

        Stage stageObjektNamn = new Stage();
        stageObjektNamn.setTitle("ORDERHISTORIK");
        stageObjektNamn.initOwner(stage);                       //initOwner =  koppla ihop med ursprungsfönstret
        //stageObjektNamn.initModality(Modality.WINDOW_MODAL);    // Blockerar huvudfönstret

        Label headingLabel = new Label("Orderhistorik för *namn*");
        createStuff.createHeaderLabel(headingLabel);
        headingLabel.setStyle("-fx-padding: 30");
        headingLabel.setAlignment(Pos.CENTER);

        Button closeWindowButton = new Button("Stäng");
        createStuff.createButtonVanlig(closeWindowButton);
        closeWindowButton.setOnAction(e -> stageObjektNamn.close());

        HBox hBox = new HBox();
        hBox.getChildren().addAll(closeWindowButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);
        //borderPane.setCenter(gridPane);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stageObjektNamn.setScene(scene);
        stageObjektNamn.showAndWait();
    }
    
    public void searchMemberWindow(Stage stage)    {
        stage.setTitle("SÖK MEDLEM");

        Label headingLabel = new Label("Sök efter medlem");
        createStuff.createHeaderLabel(headingLabel);

        Label personalIdNrLabel = new Label("Personnummer (10 siffror) ");
        personalIdNrLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label firstNameLabel = new Label("Förnamn ");
        firstNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label lastNameLabel = new Label("Efternamn ");
        lastNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        Label membershipLevelLabel = new Label("Medlemsnivå (student, standard, premium) ");
//        membershipLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        //Måste säkerställa att input blir rätt antal siffror
        TextField personalIdNrField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
//        //Måste säkerställa att input blir antingen standard, student eller premium.
//        TextField membershipLevelField = new TextField();

        Label hittadEllerEjLabel = new Label();
        hittadEllerEjLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

//        Label denhittadeMedlemmensUppgifter = new Label();
//        denhittadeMedlemmensUppgifter.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
//
        TextFlow denhittadeMedlemmensUppgifter = new TextFlow();
        denhittadeMedlemmensUppgifter.setLineSpacing(5);

        Button searchButton = new Button("Sök");
        createStuff.createButtonVanlig(searchButton);
        searchButton.setOnAction(e -> {

            for (Member member : memberList) {
                if (member.getPersonalIdNumber().equalsIgnoreCase(personalIdNrField.getText()) ||
                        member.getFirstName().equalsIgnoreCase(firstNameField.getText()) ||
                        member.getLastName().equalsIgnoreCase(lastNameField.getText())) {

                    hittadEllerEjLabel.setStyle("-fx-text-fill: black");
                    hittadEllerEjLabel.setText("Uppgifter om medlemmen");

                    printInfoAboutMember(member, denhittadeMedlemmensUppgifter);

                    personalIdNrField.clear();
                    firstNameField.clear();
                    lastNameField.clear();

                    return;
                }
            }

            personalIdNrField.clear();
            firstNameField.clear();
            lastNameField.clear();

            hittadEllerEjLabel.setStyle("-fx-text-fill: red");
            hittadEllerEjLabel.setText("Ingen medlem hittades");
            //denhittadeMedlemmensUppgifter.setText("");
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
//        gridPane.add(membershipLevelLabel, 0, 8);
//        gridPane.add(membershipLevelField, 0, 9);
        gridPane.add(searchButton, 0, 12);
        gridPane.add(hittadEllerEjLabel, 0, 14);
        gridPane.add(denhittadeMedlemmensUppgifter, 0, 15);

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void changeInfoMemberWindow (Stage stage)    {
        stage.setTitle("ÄNDRA UPPGIFT OM MEDLEM");

        Label headingLabel = new Label("Sök efter medlem som ska ändras");
        createStuff.createHeaderLabel(headingLabel);

        //Sen komma till metoden searchForMember, samma som används för
        //att söka på "vanligt" sätt?

        Button changeInfoMember = new Button("Ändra");
        createStuff.createButtonVanlig(changeInfoMember);

        Button deleteMember = new Button("Ta bort");
        createStuff.createButtonVanlig(deleteMember);
        deleteMember.setOnAction(e -> {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("VAD SKA STÅ HÄR?");
            confirmationAlert.setHeaderText("Ta bort medlem");
            confirmationAlert.setContentText("Är du säker på att du vill ta bort medlem?");
            confirmationAlert.showAndWait();

        });

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        borderPane.setLeft(changeInfoMember);       //Ligger här tillfälligt nu
        borderPane.setRight(deleteMember);          //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void vehicleRegistryWindow(Stage stage) {
        stage.setTitle("FORDONSREGISTER ");

        Label headingLabel = new Label("Alla fordon");
        createStuff.createHeaderLabel(headingLabel);

        //************TRAILERS************

        TableView <Trailer> trailersTableView = new TableView();      //Metod som fixar?
        trailersTableView.setEditable(false);                         //VAD GÖR DENNA
        trailersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vBoxTrailer = new VBox();
        vBoxTrailer.getChildren().addAll(trailersTableView);
        vBoxTrailer.setPadding(new Insets(10, 10, 0, 10));

        TableColumn<Trailer, String> brandColumn = new TableColumn("Märke");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        createStuff.createTableColumnFordonsregister(brandColumn);

        TableColumn <Trailer, Number> priceColumn = new TableColumn("Pris (kr/dag)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        createStuff.createTableColumnFordonsregister(priceColumn);

        TableColumn <Trailer, Number> lengthColumn = new TableColumn("Längd (cm)");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        createStuff.createTableColumnFordonsregister(lengthColumn);

        TableColumn <Trailer, Number> widthColumn = new TableColumn("Bredd (cm)");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));
        createStuff.createTableColumnFordonsregister(widthColumn);

        TableColumn <Trailer, Number> itemNumberColumn = new TableColumn("Artikelnummer");
        itemNumberColumn.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));
        createStuff.createTableColumnFordonsregister(itemNumberColumn);

        trailersTableView.getColumns().addAll(brandColumn, priceColumn, lengthColumn, widthColumn, itemNumberColumn);
        trailersTableView.setStyle(
        "-fx-border-color: black;" +
        "-fx-border-width: 1;"
        // +
        //"-fx-padding: 5;"
                );

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("spaced-tabs");
        tabPane.setStyle("-fx-padding: 10");

        Tab trailersTab = new Tab();
        Label trailersLabel = new Label("Släpvagnar");
        createStuff.createTabsFlikarFordonsregister(trailersLabel, trailersTab);
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
        createStuff.createTabsFlikarFordonsregister(lawnMowersLabel, lawnMowersTab);
        lawnMowersTab.setGraphic(lawnMowersLabel);
        lawnMowersTab.setContent(lawnMowersTableView);

        //************ALLA************

        Tab allVehiclesTab = new Tab();
        Label allVehiclesLabel = new Label("Alla fordon");
        createStuff.createTabsFlikarFordonsregister(allVehiclesLabel, allVehiclesTab);
        allVehiclesTab.setGraphic(allVehiclesLabel);

         //***********************

        tabPane.getTabs().addAll(trailersTab, lawnMowersTab, allVehiclesTab);

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPaneMain = new BorderPane();
        borderPaneMain.setTop(headingLabel);        //Ligger här tillfälligt nu KANSKE till metod
        BorderPane.setAlignment(headingLabel, Pos.CENTER);
        BorderPane.setMargin(headingLabel, new Insets(10));
        borderPaneMain.setCenter(tabPane);

        Scene scene = createStuff.createWindow(hBox, borderPaneMain);
        stage.setScene(scene);
        stage.show();
    }

    public void addVehicleWindow(Stage stage) {
        stage.setTitle("LÄGG TILL FORDON ");

        Label headingLabel = new Label("Lägga till fordon");
        createStuff.createHeaderLabel(headingLabel);

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void searchVehicleWindow(Stage stage) {
        stage.setTitle("SÖK FORDON ");

        Label headingLabel = new Label("Sök efter fordon");
        createStuff.createHeaderLabel(headingLabel);

        //Hur ska man kunna söka?

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void FORDONSREGISTER(Stage stage) {

        Stage vehicleRegistryStage = new Stage();
        vehicleRegistryStage.initModality(Modality.APPLICATION_MODAL);      //Detta hindrar klick i huvudfönstret
        vehicleRegistryStage.setTitle("FORDONSREGISTER ");

        Label headingLabel = new Label("Alla fordon");
        createStuff.createHeaderLabel(headingLabel);

        //************TRAILERS************
        TableView<Trailer> trailersTableView = new TableView();      //Metod som fixar?
        trailersTableView.setEditable(false);                         //VAD GÖR DENNA
        trailersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vBoxTrailer = new VBox();
        vBoxTrailer.getChildren().addAll(trailersTableView);
        vBoxTrailer.setPadding(new Insets(10, 10, 0, 10));

        TableColumn<Trailer, String> brandColumn = new TableColumn("Märke");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        createStuff.createTableColumnFordonsregister(brandColumn);

        TableColumn<Trailer, Number> priceColumn = new TableColumn("Pris (kr/dag)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        createStuff.createTableColumnFordonsregister(priceColumn);

        TableColumn<Trailer, Number> lengthColumn = new TableColumn("Längd (cm)");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        createStuff.createTableColumnFordonsregister(lengthColumn);

        TableColumn<Trailer, Number> widthColumn = new TableColumn("Bredd (cm)");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));
        createStuff.createTableColumnFordonsregister(widthColumn);

        TableColumn<Trailer, Number> itemNumberColumn = new TableColumn("Artikelnummer");
        itemNumberColumn.setCellValueFactory(new PropertyValueFactory<>("itemNumber"));
        createStuff.createTableColumnFordonsregister(itemNumberColumn);

        //Kolumn med knapp Bruksanvisning
        TableColumn<Trailer, Void> instructionManualColumn = new TableColumn<>("Bruksanvisning");
        instructionManualColumn.setCellFactory(col -> new TableCell<Trailer, Void>() {
        Button instructionManualButton = new Button("Klicka");
        HBox buttons = new HBox(5);
            {
                buttons.setAlignment(Pos.CENTER);
                buttons.getChildren().addAll(instructionManualButton);
                instructionManualButton.setOnAction(e -> {
                    //KOD FÖR att komma åt BRUKSANVISNING, metodanrop!
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        Label showChosenVehicleLabel = new Label("");

        //Kolumn med knapp Välj
        TableColumn<Trailer, Void> selectButtonColumn = new TableColumn<>("Välj");
        selectButtonColumn.setCellFactory(col -> new TableCell<Trailer, Void>() {
            Button selectButton = new Button("Välj");
            HBox buttons = new HBox(5);
            {
                buttons.setAlignment(Pos.CENTER);
                buttons.getChildren().addAll(selectButton);
                selectButton.setOnAction(e -> {
                    //*************FELHANTERING HÄR!!****************
                    //"Det här fordondet är redan uthyrt!"

                    Trailer selectedTrailer = getTableView().getItems().get(getIndex());
                    vehicleInstansvariabel = selectedTrailer;
                    showChosenVehicleLabel.setText(vehicleInstansvariabel.toString());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        trailersTableView.getColumns().addAll(brandColumn, priceColumn, lengthColumn, widthColumn,
                itemNumberColumn, instructionManualColumn,  selectButtonColumn);
        trailersTableView.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 1;"
                // +
                //"-fx-padding: 5;"
        );

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("spaced-tabs");
        tabPane.setStyle("-fx-padding: 10");

        Tab trailersTab = new Tab();
        Label trailersLabel = new Label("Släpvagnar");
        createStuff.createTabsFlikarFordonsregister(trailersLabel, trailersTab);
        trailersTab.setGraphic(trailersLabel);
        trailersTab.setContent(vBoxTrailer);                //VBOXEN HÄR

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
        createStuff.createTabsFlikarFordonsregister(lawnMowersLabel, lawnMowersTab);
        lawnMowersTab.setGraphic(lawnMowersLabel);
        lawnMowersTab.setContent(lawnMowersTableView);

        //************ALLA************
        Tab allVehiclesTab = new Tab();
        Label allVehiclesLabel = new Label("Alla fordon");
        createStuff.createTabsFlikarFordonsregister(allVehiclesLabel, allVehiclesTab);
        allVehiclesTab.setGraphic(allVehiclesLabel);
        //***********************

        tabPane.getTabs().addAll(trailersTab, lawnMowersTab, allVehiclesTab);

        Button valtFordonKlarButton = new Button("Klar");
        createStuff.createButtonVanlig(valtFordonKlarButton);
        valtFordonKlarButton.setOnAction(e -> vehicleRegistryStage.close());
        //*******INUTI DENNA ACTIONEVENT*********
        //   - Felhantering, "Du har inte valt något fordon"

        HBox hBox = new HBox();
        hBox.setSpacing(70);
        hBox.setStyle("-fx-padding: 20");
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(showChosenVehicleLabel, valtFordonKlarButton);

        BorderPane borderPaneMain = new BorderPane();
        borderPaneMain.setTop(headingLabel);        //Ligger här tillfälligt nu KANSKE till metod
        BorderPane.setAlignment(headingLabel, Pos.CENTER);
        BorderPane.setMargin(headingLabel, new Insets(10));
        borderPaneMain.setCenter(tabPane);
        borderPaneMain.setBottom(hBox);

        Scene scene = new Scene(borderPaneMain, 800, 750);
        vehicleRegistryStage.setScene(scene);
        vehicleRegistryStage.showAndWait();         //showAndWait låser huvudfönstret.
    }

    public void MEDLEMSREGISTER_NY (Stage stage)   {

        Stage memberRegistryStage = new Stage();
        memberRegistryStage.initModality(Modality.APPLICATION_MODAL);      //Detta hindrar klick i huvudfönstret
        memberRegistryStage.setTitle("MEDLEMSREGISTER ");

        Label headingLabel = new Label("Alla medlemmar");
        createStuff.createHeaderLabel(headingLabel);

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Sök medlem");

        Button searchButton = new Button("Sök");
        searchButton.setPrefSize(100, 25);
        searchButton.setStyle("-fx-background-radius: 5;");
        searchButton.setOnAction(e -> {
            //KOD SOM SKA SÖKA BLAND MEDLEMMAR. SKICKAS TILL METOD I MembershipService?
            //membershipService.searchForMember();
        });

        HBox onTopHBox = new HBox(searchTextField, searchButton);
        onTopHBox.setSpacing(20);

        VBox onTopVBox = new VBox();
        onTopVBox.setSpacing(10);
        onTopVBox.setAlignment(Pos.CENTER);
        onTopVBox.setPadding(new Insets(10));
        onTopVBox.getChildren().addAll(headingLabel, onTopHBox);

        TableView <Member> memberTableView = new TableView<>();
        memberTableView.setEditable(true);                                              //???????????
        memberTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);     //Eventuellt

        TableColumn<Member, String> firstNameColumn = new TableColumn<>("Förnamn");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        createStuff.createTableColumnFordonsregister(firstNameColumn);
        TableColumn<Member, String> lastNameColumn = new TableColumn<>("Efternamn");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        createStuff.createTableColumnFordonsregister(lastNameColumn);
        TableColumn<Member, String> personalIdNumberColumn = new TableColumn<>("Personnummer");
        personalIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("personalIdNumber"));
        createStuff.createTableColumnFordonsregister(personalIdNumberColumn);
        TableColumn<Member, String> membershipLevelColumn = new TableColumn<>("Medlemsnivå");
        membershipLevelColumn.setCellValueFactory(new PropertyValueFactory<>("membershipLevel"));
        createStuff.createTableColumnFordonsregister(membershipLevelColumn);

        TableColumn<Member, Void> orderHistoryColumn = new TableColumn<>("Orderhistorik");
        orderHistoryColumn.setCellFactory(col -> new TableCell<Member, Void>() {

            Button orderHistoryButton = new Button("Klicka");
            HBox orderHistoryHBox = new HBox(5);
                {
                    orderHistoryHBox.setAlignment(Pos.CENTER);
                    orderHistoryHBox.getChildren().addAll(orderHistoryButton);
                    orderHistoryButton.setOnAction(e -> {
                        //KOD FÖR att komma åt ORDERHISTORIK, metodanrop!
                    });
                }
                @Override
                protected void updateItem (Void item, boolean empty)    {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(orderHistoryHBox);
                }
            }
        });

        Label showChosenMemberLabel = new Label("");

        TableColumn<Member, Void> selectMemberColumn = new TableColumn<>("RUBRIK");
        selectMemberColumn.setCellFactory(col -> new TableCell<Member, Void>() {

            Button selectMemberButton = new Button("Välj");
            HBox selectMemberHBox = new HBox(5);
                {
                    selectMemberHBox.setAlignment(Pos.CENTER);
                    selectMemberHBox.getChildren().addAll(selectMemberButton);
                    selectMemberButton.setOnAction(e -> {

                    Member selectedMember = getTableView().getItems().get(getIndex());
                    memberInstansvariabel = selectedMember;
                    showChosenMemberLabel.setText(memberInstansvariabel.toString());
                    });
                }
                @Override
                protected void updateItem (Void item, boolean empty)    {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(selectMemberHBox);
                }
            }
        });

        memberTableView.getColumns().addAll(firstNameColumn, lastNameColumn,
                personalIdNumberColumn, membershipLevelColumn, orderHistoryColumn, selectMemberColumn);
        memberTableView.setStyle(
            "-fx-border-color: black;" +
                    "-fx-border-width: 1;"
            // +
            //"-fx-padding: 5;"
        );

        //***************HÄR SKA LISTORNA VARA***************
        //List<Member> membersList = memberRegistry.getMemberList();
        List<Member> memberList = membershipService.returnListaMedlemmar_NY();

        ObservableList<Member> membersObservableList = FXCollections.observableArrayList(memberList);
        memberTableView.setItems(membersObservableList);

        Button valdMedlemKlarButton = new Button("Klar");
        createStuff.createButtonVanlig(valdMedlemKlarButton);
        valdMedlemKlarButton.setOnAction(e -> memberRegistryStage.close());
            //*******INUTI DENNA ACTIONEVENT*********
            //   - Felhantering, "Du har inte valt medlem"

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(70);
        bottomHBox.setStyle("-fx-padding: 20");
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomHBox.getChildren().addAll(showChosenMemberLabel, valdMedlemKlarButton);

        BorderPane borderPane = new BorderPane();
//        borderPane.setTop(headingLabel);        //Ligger här tillfälligt nu KANSKE till metod
//        BorderPane.setAlignment(headingLabel, Pos.CENTER);
//        BorderPane.setMargin(headingLabel, new Insets(10));
        borderPane.setTop(onTopVBox);
        BorderPane.setMargin(onTopHBox, new Insets(10));
        borderPane.setCenter(memberTableView);
        borderPane.setBottom(bottomHBox);

        Scene scene = new Scene(borderPane, 800, 750);
        memberRegistryStage.setScene(scene);
        memberRegistryStage.showAndWait();         //showAndWait låser huvudfönstret.
    }

    public void bookRentalsWindow(Stage stage) {
        stage.setTitle("BOKA FORDON");

        Label headingLabel = new Label("Bokningssida");
        createStuff.createHeaderLabel(headingLabel);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);
        BorderPane.setAlignment(headingLabel, Pos.CENTER);
        BorderPane.setMargin(headingLabel, new Insets(20));

        //***********FORDON***********
        Label choseVehicleLabel = new Label("Välj fordon");
        createStuff.createHeaderLabel(choseVehicleLabel);

        Label showChosenVehicle = new Label("");
        showChosenVehicle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Button openVehicleRegisterButton = new Button("Till fordonsregistet (öppnas i nytt fönster) ");
        openVehicleRegisterButton.setPrefSize(280, 40);
        openVehicleRegisterButton.setStyle("-fx-background-radius: 5;");
        openVehicleRegisterButton.setOnAction(e -> {
                     FORDONSREGISTER(stage);
               showChosenVehicle.setText("Du har valt följande:\n\n" + vehicleInstansvariabel.toString());
        });

        Button nextButtonVehicle = new Button("Nästa");
        createStuff.createButtonVanlig(nextButtonVehicle);

        HBox nextButtonVehicleHBox = new HBox();               //Skicka detta till metod också?
        nextButtonVehicleHBox.setStyle("-fx-padding: 30");
        nextButtonVehicleHBox.setAlignment(Pos.CENTER_RIGHT);
        nextButtonVehicleHBox.getChildren().addAll(nextButtonVehicle);

        VBox vehicleVBox = new VBox();
        createStuff.createVbox(vehicleVBox);
        VBox.setMargin(choseVehicleLabel, new Insets(20, 0, 20, 0));
        VBox.setMargin(openVehicleRegisterButton, new Insets(0, 0, 30, 0));
        VBox.setMargin(showChosenVehicle, new Insets(0, 0, 100, 0));
        vehicleVBox.getChildren().addAll(choseVehicleLabel, openVehicleRegisterButton,
                showChosenVehicle, nextButtonVehicleHBox);

        //***********DATUM***********
        Label choseDateLabel = new Label("Välj datum");
        createStuff.createHeaderLabel(choseDateLabel);
        Label fromDateLabel = new Label("Från och med");
        fromDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        DatePicker fromDatePicker = new DatePicker();
        Label toDateLabel = new Label("Till och med");
        toDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        DatePicker toDatePicker = new DatePicker();

        Button selectFromDateButton = new Button("Välj");
        selectFromDateButton.setPrefSize(60, 20);
        selectFromDateButton.setStyle("-fx-background-radius: 5;");
//        choseFromDateButton.setOnAction(e -> {
//            fromDatePicker.setValue(LocalDate.now());
//            toDatePicker.setValue(LocalDate.now());
//        });

        Label showChosenDateLabel = new Label("");

        Button selectToDateButton = new Button("Välj");
        selectToDateButton.setPrefSize(60, 20);
        selectToDateButton.setStyle("-fx-background-radius: 5;");
        selectToDateButton.setOnAction(e -> {
                LocalDate fromDate = fromDatePicker.getValue();
                LocalDate toDate = toDatePicker.getValue();

                //*************FELHANTERING HÄR!!****************

                numberOfDays = (int) ChronoUnit.DAYS.between(fromDate, toDate); //(int) är en casting från long
                showChosenDateLabel.setText("Antal dagar: " + numberOfDays);
            });

        Button nextButtonDate = new Button("Nästa");
        createStuff.createButtonVanlig(nextButtonDate);
        Button goBackButtonDate = new Button("Tillbaka");
        createStuff.createButtonVanlig(goBackButtonDate);

        HBox nextBackButtonsDateHBox = new HBox();               //Skicka detta till metod också?
        nextBackButtonsDateHBox.setStyle("-fx-padding: 30");
        nextBackButtonsDateHBox.setSpacing(110);
        nextBackButtonsDateHBox.setAlignment(Pos.CENTER_RIGHT);
        nextBackButtonsDateHBox.getChildren().addAll(goBackButtonDate, nextButtonDate);

        VBox dateVBox = new VBox();
        createStuff.createVbox(dateVBox);
        VBox.setMargin(choseDateLabel, new Insets(20, 0, 10, 0));
        VBox.setMargin(fromDateLabel, new Insets(0, 0, 0, 0));
        VBox.setMargin(fromDatePicker, new Insets(0, 0, 0, 0));
        VBox.setMargin(selectFromDateButton, new Insets(0, 0, 20, 0));
        VBox.setMargin(toDateLabel, new Insets(0, 0, 0, 0));
        VBox.setMargin(toDatePicker, new Insets(0, 0, 0, 0));
        VBox.setMargin(selectToDateButton, new Insets(0, 0, 10, 0));
        VBox.setMargin(showChosenDateLabel, new Insets(0, 0, 8, 0));
        VBox.setMargin(nextBackButtonsDateHBox, new Insets(0, 0, 10, 0));

        dateVBox.getChildren().addAll(choseDateLabel, fromDateLabel, fromDatePicker, selectFromDateButton,
                toDateLabel, toDatePicker, selectToDateButton, showChosenDateLabel, nextBackButtonsDateHBox);

        //***************MEDLEMSSIDA************************************
        Label choseMemberLabel = new Label("Välj medlem");
        createStuff.createHeaderLabel(choseMemberLabel);

        Label showChosenMemberLabel = new Label("Du har valt medlem XXX");

        Button openMemberRegisterButton = new Button("Till medlemsregistret (öppnas i nytt fönster) ");
        openMemberRegisterButton.setPrefSize(280, 40);
        openMemberRegisterButton.setStyle("-fx-background-radius: 5;");
        openMemberRegisterButton.setOnAction(e -> {
                MEDLEMSREGISTER_NY(stage);
               showChosenMemberLabel.setText("Du har valt följande:\n\n" + memberInstansvariabel.toString());
        });

        Button finishBookingButton = new Button("Boka");
        createStuff.createButtonVanlig(finishBookingButton);

        Button goBackButtonMember = new Button("Tillbaka");
        createStuff.createButtonVanlig(goBackButtonMember);

        HBox finishBackButtonsMemberHBox = new HBox();               //Skicka detta till metod också?
        finishBackButtonsMemberHBox.setStyle("-fx-padding: 30");
        finishBackButtonsMemberHBox.setSpacing(110);
        finishBackButtonsMemberHBox.setAlignment(Pos.CENTER_RIGHT);
        finishBackButtonsMemberHBox.getChildren().addAll(goBackButtonMember, finishBookingButton);

        VBox memberVBox = new VBox();
        createStuff.createVbox(memberVBox);
        VBox.setMargin(choseMemberLabel, new Insets(20, 0, 20, 0));
        VBox.setMargin(openMemberRegisterButton, new Insets(0, 0, 30, 0));
        VBox.setMargin(showChosenMemberLabel, new Insets(0, 0, 150, 0));

        memberVBox.getChildren().addAll(choseMemberLabel, openMemberRegisterButton,
                showChosenMemberLabel, finishBackButtonsMemberHBox);

        //***************BOKNINGSBEKRÄFTELSE************************************
        Label confirmationLabel = new Label("Bokningsbekräftelse");
        createStuff.createHeaderLabel(confirmationLabel);

        Label showBookingLabel = new Label();

        Button printReceiptButton = new Button("Skriv ut kvitto");
        createStuff.createButtonVanlig(printReceiptButton);
        printReceiptButton.setOnAction(e -> receiptPopUp(stage));

        VBox confirmationVBox = new VBox();
        createStuff.createVbox(confirmationVBox);
        VBox.setMargin(confirmationLabel, new Insets(20, 0, 20, 0));
        VBox.setMargin(showBookingLabel, new Insets(0, 0, 30, 0));
        VBox.setMargin(printReceiptButton, new Insets(0, 0, 30, 0));
        confirmationVBox.getChildren().addAll(confirmationLabel, showBookingLabel, printReceiptButton);

        borderPane.setCenter(vehicleVBox);
        nextButtonVehicle.setOnAction(e -> borderPane.setCenter(dateVBox));
        goBackButtonDate.setOnAction(e -> borderPane.setCenter(vehicleVBox));
        nextButtonDate.setOnAction(e -> borderPane.setCenter(memberVBox));
        goBackButtonMember.setOnAction(e -> borderPane.setCenter(dateVBox));
        finishBookingButton.setOnAction(e -> {
            rentalService.bookItemDEN_NYA(vehicleInstansvariabel, numberOfDays, memberInstansvariabel);

            String bookingSummary =
                            vehicleInstansvariabel.toString() + "\n\n" +
                            "Medlem:\n" +
                            memberInstansvariabel.toString() + "\n\n" +
                            "Dagar:\n" + numberOfDays + " stycken\n";
                        //OCH HÄR SKA OCKSÅ SLUTPRISET STÅ, alltså anropa metod som räknar ut det.
                        //Just nu ligger det i RentalService -> SumRevenue + de två override-metoderna som räknar pris.
                        //Behöver nog en egen metod som endast räknar på pris.

            showBookingLabel.setText(bookingSummary);
            borderPane.setCenter(confirmationVBox);

        });

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void receiptPopUp(Stage stage) {

        Stage receiptStage = new Stage();
        receiptStage.initModality(Modality.APPLICATION_MODAL);
        receiptStage.setTitle("KVITTO");

        Label headingLabel = new Label("NAMN MEDLEMSKLUBBEN");
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label receiptLabel = new Label("KVITTO");
        receiptLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 16));
        Label starsLabel = new Label("********************************************");
        starsLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label fordonsTypOchMärkeLabel = new Label("Släpvagn Tiki");
        fordonsTypOchMärkeLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label antalDagarOchPris = new Label("5 dag * 425 kr");
        antalDagarOchPris.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label prisPerGrej = new Label("2125 kr");
        prisPerGrej.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));

        Label fordonsTypOchMärkeLabel2 = new Label("Gräskl Husqvar");
        fordonsTypOchMärkeLabel2.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label antalDagarOchPris2 = new Label("2 dag * 210 kr");
        antalDagarOchPris2.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label prisPerGrej2 = new Label("420 kr");
        prisPerGrej2.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label starsLabel2 = new Label("******************");
        starsLabel2.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label total = new Label("Totalt");
        total.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        Label totalAmount = new Label("2545 kr");
        totalAmount.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        Label starsLabel3 = new Label("****************");
        starsLabel3.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label saveReceiptLabel = new Label("SPARA KVITTOT");
        saveReceiptLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 18));
        Label welcomeBackLabel = new Label("Välkommen åter!");
        welcomeBackLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 16));

        Label starsLabel4 = new Label("************");
        starsLabel4.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label starsLabel5 = new Label("******************");
        starsLabel5.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label starsLabel6 = new Label("****************");
        starsLabel6.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        Label starsLabel7 = new Label("************");
        starsLabel7.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));

        Button okButton = new Button("OK");
        okButton.setPrefSize(80, 30);
        okButton.setStyle("-fx-background-radius: 5;");
        okButton.setOnAction(e -> receiptStage.close());

//        ColumnConstraints column1 = new ColumnConstraints();
//        column1.setPrefWidth(100);
//        ColumnConstraints column2 = new ColumnConstraints();
//        column2.setPrefWidth(80);
//        ColumnConstraints column3 = new ColumnConstraints();
//        column3.setPrefWidth(80);

        GridPane gridPane = new GridPane();
//        gridPane.getColumnConstraints().addAll(column1, column2, column3);
//        gridPane.setStyle(
//            "-fx-background-color: white;" +
//            "-fx-background-radius: 15;" +
//            "-fx-border-color: lightgray;" +
//            "-fx-border-radius: 15;" +
//            "-fx-border-width: 1;" +
//             "-fx-padding: 10;"
//        );
        //gridPane.setHgap(10);   //mellan kolumn
        gridPane.setVgap(10);   //mellan rader
        gridPane.setPadding(new Insets(0, 5, 0, 5));
        gridPane.setAlignment(Pos.CENTER_LEFT);

        gridPane.add(fordonsTypOchMärkeLabel, 0, 1);
        gridPane.add(antalDagarOchPris, 1, 2);
        gridPane.add(prisPerGrej, 2, 2);
        gridPane.add(fordonsTypOchMärkeLabel2, 0, 3);
        gridPane.add(antalDagarOchPris2, 1, 4);
        gridPane.add(prisPerGrej2, 2, 4);
        gridPane.add(starsLabel2, 1, 5);
        gridPane.add(starsLabel3, 0, 5);
        gridPane.add(starsLabel4, 2, 5);
        gridPane.add(total, 1, 6);
        gridPane.add(totalAmount, 2, 6);
        gridPane.add(starsLabel5, 1, 7);
        gridPane.add(starsLabel6, 0, 7);
        gridPane.add(starsLabel7, 2, 7);
        gridPane.add(saveReceiptLabel, 1, 9);
        gridPane.add(welcomeBackLabel, 1, 10);

        VBox topVBox = new VBox();
        topVBox.setAlignment(Pos.CENTER);
        topVBox.setSpacing(10);
        topVBox.setPadding(new Insets(10));
        topVBox.getChildren().addAll(headingLabel, receiptLabel, starsLabel);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topVBox);
        borderPane.setCenter(gridPane);
        borderPane.setBottom(okButton);
        BorderPane.setAlignment(okButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(okButton, new Insets(10));

        Scene scene = new Scene(borderPane, 400, 400);
        receiptStage.setScene(scene);
        receiptStage.showAndWait();
    }

    public void deleteVehicleWindow(Stage stage) {
        stage.setTitle("TA BORT (ÄNDRA?) FORDON ");

        Label headingLabel = new Label("Ta bort (eller ändra)");
        createStuff.createHeaderLabel(headingLabel);

        Button changeInfoVehicle = new Button("Ändra");
        createStuff.createButtonVanlig(changeInfoVehicle);

        Button deleteVehicle = new Button("Ta bort");
        createStuff.createButtonVanlig(deleteVehicle);
        deleteVehicle.setOnAction(e -> {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("VAD SKA STÅ HÄR?");
            confirmationAlert.setHeaderText("Ta bort fordon");
            confirmationAlert.setContentText("Är du säker på att du vill ta bort fordon?");
            confirmationAlert.showAndWait();
        });

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        borderPane.setRight(deleteVehicle);          //Ligger här tillfälligt nu
        borderPane.setLeft(changeInfoVehicle);       //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);
        //borderPane.setBottom(hBox);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();

    }

    public void ongoingRentalWindow(Stage stage) {
        stage.setTitle("PÅGÅENDE UTHYRNINGAR");

        Label headingLabel = new Label("Uthyrda fordon just nu");
        createStuff.createHeaderLabel(headingLabel);


        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu
        //borderPane.setCenter(gridPane);

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void returnVehicleWindow(Stage stage) {
        stage.setTitle("ÅTERLÄMNA FORDON");

        Label headingLabel = new Label("Återlämna fordon");
        createStuff.createHeaderLabel(headingLabel);


        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void sumRevenueWindow(Stage stage) {
        stage.setTitle("SUMMERA INTÄKTER");

        Label headingLabel = new Label("Summera intäkter");
        createStuff.createHeaderLabel(headingLabel);

        //Olika val här, både se dagens intäkter och se intäkter för samtliga pågående uthyrningar.

        Button returnToMenuButton = createStuff.createButtonReturnToMenu();
        returnToMenuButton.setOnAction(e -> mainMenuWindow(stage));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(returnToMenuButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headingLabel);            //Ligger här tillfälligt nu

        Scene scene = createStuff.createWindow(hBox, borderPane);
        stage.setScene(scene);
        stage.show();
    }

    //Den här metoden ska ligga i MemberShipService men bestäm hur den ska vara först.
    public void printInfoAboutMember(Member member, TextFlow textFlow) {

        Text personalIdNumberTitle = new Text("\nPersonnummer: ");
        personalIdNumberTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text personalIdNumberNrValue = new Text(member.getPersonalIdNumber() + "\n");
        personalIdNumberNrValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Text firstNameTitle = new Text("Förnamn: ");
        firstNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text firstNameValue = new Text(member.getFirstName() + "\n");
        firstNameValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Text lastNameTitle = new Text("Efternamn: ");
        lastNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text lastNameValue = new Text(member.getLastName() + "\n");
        lastNameValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Text membershipLevelTitle = new Text("Medlemsnivå: ");
        membershipLevelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text membershipLevelValue = new Text(member.getMembershipLevel() + "\n");
        membershipLevelValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Button orderHistoryButton = new Button("Orderhistorik");
        orderHistoryButton.setStyle("-fx-background-radius: 5;");
        //orderHistoryButton.setStyle("-fx-padding: 30");

        //DEt här blev ju INTE enligt plan.
        //orderHistoryButton.setOnAction(e -> orderHistoryWindow(stage));

        textFlow.getChildren().addAll(personalIdNumberTitle, personalIdNumberNrValue,
                firstNameTitle, firstNameValue, lastNameTitle, lastNameValue,
                membershipLevelTitle, membershipLevelValue, orderHistoryButton, new Text("\n"));
    }

    public static void main(String[] args) {
         launch(args);
    }
}


