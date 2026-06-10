package com.calculator.ui;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class WindowControls {

    private static final PseudoClass REVEALED = PseudoClass.getPseudoClass("revealed");

    private final HBox root = new HBox(8);
    private final TrafficButton close = new TrafficButton("✕", "close-dot");
    private final TrafficButton minimize = new TrafficButton("─", "minimize-dot");
    private final TrafficButton maximize = new TrafficButton("⤢", "maximize-dot");

    private double restoreX;
    private double restoreY;
    private double restoreWidth;
    private double restoreHeight;

    public WindowControls() {
        root.getStyleClass().add("traffic-lights");
        root.setAlignment(Pos.CENTER_LEFT);
        root.setMouseTransparent(false);
        root.setOnMouseEntered(event -> reveal(true));
        root.setOnMouseExited(event -> reveal(false));
        root.getChildren().addAll(close, minimize, maximize);
    }

    public HBox root() {
        return root;
    }

    public void attach(Stage stage) {
        close.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });
        minimize.setOnAction(event -> stage.setIconified(true));
        maximize.setOnAction(event -> toggleMaximize(stage));
    }

    private void toggleMaximize(Stage stage) {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setX(restoreX);
            stage.setY(restoreY);
            stage.setWidth(restoreWidth);
            stage.setHeight(restoreHeight);
            return;
        }
        restoreX = stage.getX();
        restoreY = stage.getY();
        restoreWidth = stage.getWidth();
        restoreHeight = stage.getHeight();
        stage.setMaximized(true);
    }

    private void reveal(boolean visible) {
        for (TrafficButton button : List.of(close, minimize, maximize)) {
            button.pseudoClassStateChanged(REVEALED, visible);
            button.setText(visible ? button.symbol : "");
        }
    }

    private static class TrafficButton extends Button {

        private final String symbol;

        private TrafficButton(String symbol, String styleClass) {
            this.symbol = symbol;
            getStyleClass().addAll("traffic-button", styleClass);
            setText("");
            setCursor(Cursor.DEFAULT);
            setMouseTransparent(false);
            setFocusTraversable(false);
            setMinSize(12, 12);
            setPrefSize(12, 12);
            setMaxSize(12, 12);
        }
    }
}
