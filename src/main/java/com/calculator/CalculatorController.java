package com.calculator;

import com.calculator.ui.CalcButton;
import com.calculator.ui.DisplayPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;

public class CalculatorController {

    private final CalculatorEngine engine = new CalculatorEngine();
    private final DisplayPane displayPane = new DisplayPane();
    private final StackPane root = new StackPane();
    private final Region resizeHandle = new Region();
    private final Map<String, CalcButton> operatorButtons = new LinkedHashMap<>();
    private CalcButton clearButton;

    public CalculatorController() {
        root.getStyleClass().add("window-root");
        root.setFocusTraversable(true);

        VBox shell = new VBox(12);
        shell.getStyleClass().add("calculator-shell");
        shell.setPadding(new Insets(18, 18, 18, 18));
        shell.getChildren().addAll(displayPane, keypad());
        VBox.setVgrow(displayPane, Priority.NEVER);

        resizeHandle.getStyleClass().add("resize-handle");
        StackPane.setAlignment(resizeHandle, Pos.BOTTOM_RIGHT);

        root.getChildren().addAll(shell, resizeHandle);
        refresh();
    }

    public StackPane root() {
        return root;
    }

    public DisplayPane displayPane() {
        return displayPane;
    }

    public Region resizeHandle() {
        return resizeHandle;
    }

    public void inputDigit(String digit) {
        engine.inputDigit(digit);
        refresh();
    }

    public void inputDecimal() {
        engine.inputDecimal();
        refresh();
    }

    public void chooseOperator(String operator) {
        engine.chooseOperator(operator);
        refresh();
    }

    public void equals() {
        engine.equals();
        refresh();
    }

    public void backspace() {
        engine.backspace();
        refresh();
    }

    public void clear() {
        engine.clear();
        refresh();
    }

    private GridPane keypad() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("keypad");
        grid.setHgap(12);
        grid.setVgap(12);

        addButton(grid, "AC", CalcButton.ButtonRole.UTILITY, 0, 0, 1);
        addButton(grid, "+/-", CalcButton.ButtonRole.UTILITY, 1, 0, 1);
        addButton(grid, "%", CalcButton.ButtonRole.UTILITY, 2, 0, 1);
        addButton(grid, "÷", CalcButton.ButtonRole.OPERATOR, 3, 0, 1);
        addButton(grid, "7", CalcButton.ButtonRole.NUMBER, 0, 1, 1);
        addButton(grid, "8", CalcButton.ButtonRole.NUMBER, 1, 1, 1);
        addButton(grid, "9", CalcButton.ButtonRole.NUMBER, 2, 1, 1);
        addButton(grid, "×", CalcButton.ButtonRole.OPERATOR, 3, 1, 1);
        addButton(grid, "4", CalcButton.ButtonRole.NUMBER, 0, 2, 1);
        addButton(grid, "5", CalcButton.ButtonRole.NUMBER, 1, 2, 1);
        addButton(grid, "6", CalcButton.ButtonRole.NUMBER, 2, 2, 1);
        addButton(grid, "−", CalcButton.ButtonRole.OPERATOR, 3, 2, 1);
        addButton(grid, "1", CalcButton.ButtonRole.NUMBER, 0, 3, 1);
        addButton(grid, "2", CalcButton.ButtonRole.NUMBER, 1, 3, 1);
        addButton(grid, "3", CalcButton.ButtonRole.NUMBER, 2, 3, 1);
        addButton(grid, "+", CalcButton.ButtonRole.OPERATOR, 3, 3, 1);
        addButton(grid, "0", CalcButton.ButtonRole.ZERO, 0, 4, 2);
        addButton(grid, ".", CalcButton.ButtonRole.NUMBER, 2, 4, 1);
        addButton(grid, "=", CalcButton.ButtonRole.OPERATOR, 3, 4, 1);

        return grid;
    }

    private void addButton(GridPane grid, String label, CalcButton.ButtonRole role, int column, int row, int span) {
        CalcButton button = new CalcButton(label, role);
        button.setOnAction(event -> handle(label));
        grid.add(button, column, row, span, 1);
        if ("AC".equals(label)) {
            clearButton = button;
        }
        if (role == CalcButton.ButtonRole.OPERATOR && !"=".equals(label)) {
            operatorButtons.put(label, button);
        }
    }

    private void handle(String label) {
        switch (label) {
            case "AC", "C" -> clear();
            case "+/-" -> {
                engine.toggleSign();
                refresh();
            }
            case "%" -> {
                engine.percent();
                refresh();
            }
            case "." -> inputDecimal();
            case "+", "−", "×", "÷" -> chooseOperator(label);
            case "=" -> equals();
            default -> inputDigit(label);
        }
    }

    private void refresh() {
        displayPane.setDisplayText(engine.displayText());
        if (clearButton != null) {
            clearButton.setText(engine.clearLabel());
        }
        operatorButtons.forEach((operator, button) -> button.setActive(operator.equals(engine.activeOperator())));
    }
}
