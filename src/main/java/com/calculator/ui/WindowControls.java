package com.calculator.ui;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

public class WindowControls {

    private static final PseudoClass REVEALED = PseudoClass.getPseudoClass("revealed");

    private final HBox root = new HBox(8);
    private final TrafficDot close = new TrafficDot("✕", "close-dot");
    private final TrafficDot minimize = new TrafficDot("─", "minimize-dot");
    private final TrafficDot maximize = new TrafficDot("⤢", "maximize-dot");

    private double prevX;
    private double prevY;
    private double prevW;
    private double prevH;

    public WindowControls() {
        root.getStyleClass().add("title-bar");
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPickOnBounds(false);
        root.setMouseTransparent(false);
        root.setOnMouseEntered(event -> reveal(true));
        root.setOnMouseExited(event -> reveal(false));
        root.getChildren().addAll(close.root(), minimize.root(), maximize.root());
    }

    public HBox root() {
        return root;
    }

    public void attach(Stage stage) {
        close.circle().setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        minimize.circle().setOnMouseClicked(event -> {
            stage.setIconified(true);
            event.consume();
        });
        maximize.circle().setOnMouseClicked(event -> {
            toggleMaximize(stage);
            event.consume();
        });
    }

    private void toggleMaximize(Stage stage) {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setX(prevX);
            stage.setY(prevY);
            stage.setWidth(prevW);
            stage.setHeight(prevH);
        } else {
            prevX = stage.getX();
            prevY = stage.getY();
            prevW = stage.getWidth();
            prevH = stage.getHeight();
            stage.setMaximized(true);
        }
    }

    private void reveal(boolean visible) {
        for (TrafficDot dot : List.of(close, minimize, maximize)) {
            dot.root().pseudoClassStateChanged(REVEALED, visible);
            dot.label().setVisible(visible);
        }
    }

    private static class TrafficDot {

        private final StackPane root = new StackPane();
        private final Circle circle = new Circle(6);
        private final Label label;

        private TrafficDot(String symbol, String styleClass) {
            label = new Label(symbol);
            root.getStyleClass().add("traffic-dot");
            root.setPickOnBounds(false);
            root.setMouseTransparent(false);
            root.setCursor(Cursor.DEFAULT);
            circle.getStyleClass().addAll("traffic-circle", styleClass);
            circle.setMouseTransparent(false);
            circle.setCursor(Cursor.DEFAULT);
            circle.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> root.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), true));
            circle.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> root.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false));
            label.getStyleClass().add("traffic-icon");
            label.setMouseTransparent(true);
            label.setVisible(false);
            root.getChildren().addAll(circle, label);
        }

        private StackPane root() {
            return root;
        }

        private Circle circle() {
            return circle;
        }

        private Label label() {
            return label;
        }
    }
}
