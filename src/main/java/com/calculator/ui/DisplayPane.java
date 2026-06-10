package com.calculator.ui;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class DisplayPane extends StackPane {

    private final Label label = new Label("0");

    public DisplayPane() {
        getStyleClass().add("display-pane");
        setCursor(Cursor.DEFAULT);
        setMouseTransparent(false);
        label.getStyleClass().add("display-label");
        setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(label);
    }

    public void setDisplayText(String text) {
        label.setText(text);
        int length = text.length();
        int size = Math.max(40, 80 - Math.max(0, length - 6) * 6);
        label.setStyle("-fx-font-size: " + size + "px;");
    }
}
