package com.calculator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    private static final double ASPECT_RATIO = 336.0 / 560.0;

    private double dragOffsetX;
    private double dragOffsetY;
    private double resizeStartX;
    private double resizeStartWidth;
    private boolean resizing;

    public static void main(String[] args) {
        System.setProperty("prism.forceUploadingPainter", "true");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        CalculatorController controller = new CalculatorController();
        Scene scene = new Scene(controller.root(), 336, 560);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/calculator.css")).toExternalForm());

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Calculatorpp");
        stage.setResizable(true);
        stage.setMinWidth(300);
        stage.setMinHeight(500);
        stage.setScene(scene);

        controller.displayPane().setOnMousePressed(event -> {
            dragOffsetX = event.getScreenX() - stage.getX();
            dragOffsetY = event.getScreenY() - stage.getY();
        });
        controller.displayPane().setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - dragOffsetX);
            stage.setY(event.getScreenY() - dragOffsetY);
        });

        controller.resizeHandle().setOnMousePressed(event -> {
            resizing = true;
            resizeStartX = event.getScreenX();
            resizeStartWidth = stage.getWidth();
        });
        controller.resizeHandle().setOnMouseDragged(event -> {
            double width = Math.max(stage.getMinWidth(), resizeStartWidth + event.getScreenX() - resizeStartX);
            stage.setWidth(width);
            stage.setHeight(width / ASPECT_RATIO);
        });
        controller.resizeHandle().setOnMouseReleased(event -> resizing = false);

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (!resizing) {
                stage.setHeight(newValue.doubleValue() / ASPECT_RATIO);
            }
        });

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            String text = event.getText();
            if (text != null && text.matches("[0-9]")) {
                controller.inputDigit(text);
                return;
            }
            if ("+".equals(text)) {
                controller.chooseOperator("+");
                return;
            }
            if ("-".equals(text)) {
                controller.chooseOperator("−");
                return;
            }
            if ("*".equals(text) || "x".equalsIgnoreCase(text)) {
                controller.chooseOperator("×");
                return;
            }
            if ("/".equals(text)) {
                controller.chooseOperator("÷");
                return;
            }
            if (".".equals(text) || ",".equals(text)) {
                controller.inputDecimal();
                return;
            }
            if (code == KeyCode.ENTER || code == KeyCode.EQUALS) {
                controller.equals();
                return;
            }
            if (code == KeyCode.BACK_SPACE) {
                controller.backspace();
                return;
            }
            if (code == KeyCode.ESCAPE) {
                controller.clear();
            }
        });

        stage.show();
        WindowsTransparency.apply(stage, WindowsTransparency.Backdrop.ACRYLIC);
        controller.root().requestFocus();
    }
}
