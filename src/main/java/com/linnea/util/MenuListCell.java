package com.linnea.util;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;

public class MenuListCell extends ListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }

        else {
            setText(item);
            setAlignment(Pos.CENTER);

            setStyle(
                    "-fx-background-color: #85a199;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-color: black;" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 10;" +
                    "-fx-padding: 8 20 8 20;"
            );
        }
    }
}
