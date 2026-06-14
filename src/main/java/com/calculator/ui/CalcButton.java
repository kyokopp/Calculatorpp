package com.calculator.ui;

import javafx.animation.ScaleTransition;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class CalcButton extends Button {

    private final ScaleTransition pressTransition;
    private final ScaleTransition releaseTransition;

    public CalcButton(String label, ButtonRole role) {
        super(label);
        pressTransition = new ScaleTransition(Duration.millis(80), this);
        pressTransition.setToX(0.95);
        pressTransition.setToY(0.95);
        releaseTransition = new ScaleTransition(Duration.millis(100), this);
        releaseTransition.setToX(1.0);
        releaseTransition.setToY(1.0);
        getStyleClass().add("calc-button");
        getStyleClass().addAll(role.styleClasses);
        setCache(true);
        setCacheHint(CacheHint.DEFAULT);
        setFocusTraversable(false);
        setCursor(Cursor.DEFAULT);
        setMouseTransparent(false);
        setMinSize(role == ButtonRole.ZERO ? 156 : 72, 72);
        setPrefSize(role == ButtonRole.ZERO ? 156 : 72, 72);
        setMaxSize(role == ButtonRole.ZERO ? 156 : 72, 72);
        releaseTransition.setOnFinished(event -> {
            setCache(true);
            setCacheHint(CacheHint.DEFAULT);
        });
        setOnMousePressed(event -> {
            setCache(false);
            pressTransition.playFromStart();
        });
        setOnMouseReleased(event -> {
            releaseTransition.playFromStart();
        });
        setOnMouseClicked(event -> event.consume());
    }

    public void setActive(boolean active) {
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("active"), active);
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
