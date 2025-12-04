package com.linnea.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CreateStuff {

    public Scene createWindow(HBox hBox, BorderPane borderPane)  {
    //borderPane.setStyle("-fx-background-color: #AFC4BD;");
    //borderPane.setStyle("-fx-background-color: #BCCDC7;");
    //borderPane.setStyle("-fx-background-color: #CAD7D3;");
//        borderPane.setStyle("-fx-background-color: #B3C6C1;");
        //borderPane.setStyle("-fx-background-color: #A7BBB5;");//
        //borderPane.setStyle("-fx-background-color: #9EB3AE;");
        borderPane.setStyle("-fx-background-color: #96ACA6;");

    //Kan ju lägga till returnToMenuButton här också
    //Och headingLabel också!
    //Använd detta för headingLabel
    //BorderPane.setAlignment(label, Pos.CENTER);
    //BorderPane.setMargin(label, new Insets(10));

    hBox.setAlignment(Pos.BOTTOM_RIGHT);
    hBox.setStyle("-fx-padding: 30");

    borderPane.setBottom(hBox);
    Scene scene = new Scene(borderPane, 450, 750);
    scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

    return scene;
            // Leta efter /style.css på classpath
//        URL cssUrl = getClass().getResource("/style.css");
//        if (cssUrl != null) {
//            System.out.println("DEBUG: Hittade CSS på: " + cssUrl);
//            scene.getStylesheets().add(cssUrl.toExternalForm());
//        } else {
//            System.out.println("DEBUG: Hittade INTE /style.css");
//        }
    }

    public Button createButtonReturnToMenu()  {
        Button returnToMenuButton = new Button("Huvudmeny");
        returnToMenuButton.setPrefSize(115, 40);
        returnToMenuButton.setStyle("-fx-background-radius: 5;");
        return returnToMenuButton;
    }

    public void createButtonVanlig(Button vanligButton) {
        vanligButton.setPrefSize(115, 40);
        vanligButton.setStyle("-fx-background-radius: 5;");
    }

    public void createButtonRullgardinsmeny(Button buttonRullgardinsmeny)  {
        buttonRullgardinsmeny.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        //buttonRullgardinsmeny.setStyle("-fx-background-color: #9fb9ea; -fx-background-radius: 10;");
//        buttonRullgardinsmeny.setStyle(
//        "-fx-background-color: linear-gradient(#a0c4ff, #82aaff);" +
//         "-fx-background-radius: 10;");

//        buttonRullgardinsmeny.setStyle(
//    "-fx-background-color: #99b6f6;" +
//    "-fx-background-radius: 10;" +
//    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 5, 0.0, 0, 2);");
//
            buttonRullgardinsmeny.setStyle(
        "-fx-background-color: #85a199;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: black;" +
        "-fx-border-width: 1.5;" +
        "-fx-border-radius: 10;"
            );

        buttonRullgardinsmeny.setPrefSize(200, 50);
    }

    public void createHeaderLabel(Label headingLabel)  {
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    }

    //Fixa sidan med alla fordon - FORDONSREGISTER
    //OBS används också till medlemsregistret
    public void createTableColumnFordonsregister(TableColumn tableColumn) {
        tableColumn.setPrefWidth(100);
        tableColumn.setMinWidth(100);
        tableColumn.setMaxWidth(100);
    }

    public void createTabsFlikarFordonsregister(Label label, Tab tab)    {
        //label.setFont(Font.font("Arial", FontWeight.BOLD, 16));               //GÖRS I CSS istället för här
        label.setPadding(new Insets(10, 22, 10, 22));
        tab.setClosable(false);
    }

    public void createVbox(VBox vBox) {
       //vBox.setPrefSize(300, 300);
       vBox.setMinSize(450, 500);
       vBox.setMaxSize(450, 500);
       vBox.setAlignment(Pos.TOP_CENTER);
       vBox.setSpacing(10);
       vBox.setStyle(
            "-fx-border-color: #444; " +
            "-fx-border-width: 2; " +
//    "-fx-background-color: #f9f9f9; " +
//            "-fx-border-radius: 8; " +
//            "-fx-background-radius: 8; " +
            "-fx-padding: 20;"
        );

    }




    //Om ska fixa huvudmenyn
    public void styleMenuComboBox(ComboBox<String> comboBox) {
    // cellerna i listan
    comboBox.setCellFactory(listView -> new MenuListCell());
    // hur den valda raden visas när comboboxen är stängd
    comboBox.setButtonCell(new MenuListCell());
}

}







