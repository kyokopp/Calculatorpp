package com.calculator.ui;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DisplayPane extends StackPane {

    private final Label expressionLabel = new Label("");
    private final Label label = new Label("0");

    public DisplayPane() {
        getStyleClass().add("display-pane");
        setCursor(Cursor.DEFAULT);
        setMouseTransparent(false);

        expressionLabel.getStyleClass().add("expression-label");
        expressionLabel.setMaxWidth(Double.MAX_VALUE);
        expressionLabel.setAlignment(Pos.BOTTOM_RIGHT);
        expressionLabel.setMouseTransparent(true);

        label.getStyleClass().add("display-label");

        VBox displayStack = new VBox(0);
        displayStack.setAlignment(Pos.BOTTOM_RIGHT);
        displayStack.getChildren().addAll(expressionLabel, label);

        setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(displayStack);
    }

    public void setDisplayText(String text) {
        label.setText(text);
        int length = text.replace("-", "").replace(".", "").length();
        int size = Math.max(40, 80 - Math.max(0, length - 6) * 6);
        label.setStyle(
            "-fx-font-size: " + size + "px;" +
            "-fx-font-family: 'SF Pro Display', 'Segoe UI Variable Display', 'Segoe UI';" +
            "-fx-font-weight: 300;"
        );
    }

    public void setExpressionText(String text) {
        expressionLabel.setText(text);
        expressionLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-family: 'SF Pro Display', 'Segoe UI Variable Display', 'Segoe UI';" +
            "-fx-font-weight: 300;"
        );
    }
}
