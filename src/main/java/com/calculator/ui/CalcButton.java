package com.calculator.ui;

import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

public class CalcButton extends Button {

    private final ColorAdjust colorAdjust = new ColorAdjust();

    public CalcButton(String label, ButtonRole role) {
        super(label);
        getStyleClass().add("calc-button");
        getStyleClass().addAll(role.styleClasses);
        setEffect(colorAdjust);
        setFocusTraversable(false);
        setCursor(Cursor.DEFAULT);
        setMouseTransparent(false);
        setMinSize(role == ButtonRole.ZERO ? 156 : 72, 72);
        setPrefSize(role == ButtonRole.ZERO ? 156 : 72, 72);
        setMaxSize(role == ButtonRole.ZERO ? 156 : 72, 72);
        setOnMouseEntered(event -> colorAdjust.setBrightness(0.10));
        setOnMouseExited(event -> colorAdjust.setBrightness(0.0));
        setOnMousePressed(event -> {
            colorAdjust.setBrightness(-0.15);
            animateScale(0.95, 80);
        });
        setOnMouseReleased(event -> {
            colorAdjust.setBrightness(isHover() ? 0.10 : 0.0);
            animateScale(1.0, 100);
        });
    }

    public void setActive(boolean active) {
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("active"), active);
    }

    private void animateScale(double scale, double millis) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(millis), this);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }

    public enum ButtonRole {
        NUMBER("number-button"),
        ZERO("number-button", "zero-button"),
        UTILITY("utility-button"),
        OPERATOR("operator-button");

        private final String[] styleClasses;

        ButtonRole(String... styleClasses) {
            this.styleClasses = styleClasses;
        }
    }
}
