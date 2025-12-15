package com.linnea.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UIStyler {

    public Scene styleWindow(HBox hBox, BorderPane borderPane)  {
        borderPane.setStyle("-fx-background-color: #96ACA6;");
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setStyle("-fx-padding: 30");

        borderPane.setBottom(hBox);
        Scene scene = new Scene(borderPane, 550, 850);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        return scene;
    }

    public void styleMainMenuLabels(Label mainMenulabel)   {
        mainMenulabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        mainMenulabel.setStyle("-fx-text-fill: #3f4240");
    }

    public Button styleReturnToMenuButton()  {
        Button returnToMenuButton = new Button("Huvudmeny");
        returnToMenuButton.setPrefSize(135, 40);
        returnToMenuButton.getStyleClass().add("button-return");
        return returnToMenuButton;
    }

    public void styleOrdinaryButton(Button ordinaryButton) {
        ordinaryButton.setPrefSize(125, 40);
        ordinaryButton.getStyleClass().add("button-ordinary");
    }

    public void styleLongerButton(Button longerButton) {
       longerButton.setPrefSize(330, 50);
       longerButton.getStyleClass().add("button-ordinary");
    }

    public void styleDropdownMenuButton(Button dropdownMenuButton)  {
        dropdownMenuButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        dropdownMenuButton.setStyle(
                "-fx-background-color: #85a199;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 10;"
        );
        dropdownMenuButton.setPrefSize(200, 50);
    }

    public void styleHeadingLabel(Label headingLabel)  {
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    }

    public void styleTabs(Label label, Tab tab)    {
        label.setPadding(new Insets(10, 22, 10, 22));
        tab.setClosable(false);
    }

    public void styleHBoxBooking(HBox hBox) {
        hBox.setStyle("-fx-padding: 30");
        hBox.setSpacing(255);
        hBox.setAlignment(Pos.CENTER_RIGHT);
    }

    public void styleVBoxBooking(VBox vBox) {
       vBox.setMinSize(550, 600);
       vBox.setMaxSize(550, 600);
       vBox.setAlignment(Pos.TOP_CENTER);
       vBox.setSpacing(10);

        vBox.setBackground(new Background(new BackgroundFill(
                Color.web("#B7C4BF"),
                new CornerRadii(12),
                Insets.EMPTY
        )));

        vBox.setBorder(new Border(new BorderStroke(
                Color.web("#00000022"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(12),
                new BorderWidths(1)
        )));

        DropShadow shadow = new DropShadow();
        shadow.setRadius(12);
        shadow.setOffsetY(2);
        shadow.setColor(Color.web("#00000033"));

        vBox.setEffect(shadow);
    }
}







