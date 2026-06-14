package com.calculator;

import com.calculator.ui.CalcButton;
import com.calculator.ui.DisplayPane;
import com.calculator.ui.WindowControls;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class CalculatorController {

    private final CalculatorEngine engine = new CalculatorEngine();
    private final DisplayPane displayPane = new DisplayPane();
    private final VBox root = new VBox();
    private final Group scaleGroup;
    private final WindowControls windowControls = new WindowControls();
    private final Map<String, CalcButton> operatorButtons = new LinkedHashMap<>();
    private CalcButton clearButton;

    public CalculatorController() {
        root.getStyleClass().add("window-root");
        root.setFocusTraversable(true);
        root.setSnapToPixel(false);
        root.setPrefSize(336, 588);
        root.setMinSize(336, 588);

        VBox scalableContent = new VBox();
        scalableContent.getStyleClass().add("calculator-shell");
        scalableContent.setPickOnBounds(false);
        scalableContent.setPrefSize(336, 588);
        scalableContent.setMinSize(336, 588);
        scalableContent.setMaxSize(336, 588);
        scalableContent.getChildren().addAll(windowControls.root(), displayPane, keypad());
        VBox.setVgrow(windowControls.root(), Priority.NEVER);
        VBox.setMargin(windowControls.root(), new Insets(0));
        VBox.setVgrow(displayPane, Priority.NEVER);

        scaleGroup = new Group(scalableContent);
        scaleGroup.setPickOnBounds(false);
        scaleGroup.setCache(false);
        scaleGroup.setCacheHint(CacheHint.QUALITY);

        StackPane scaleWrapper = new StackPane(scaleGroup);
        scaleWrapper.setAlignment(Pos.CENTER);
        scaleWrapper.setPickOnBounds(false);
        scaleWrapper.setStyle("-fx-background-color: transparent;");
        scaleWrapper.setSnapToPixel(false);
        scaleWrapper.setMinSize(0, 0);
        scaleWrapper.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(scaleWrapper, Priority.ALWAYS);

        root.getChildren().add(scaleWrapper);
        refresh();
    }

    public VBox root() {
        return root;
    }

    public Group scaleGroup() {
        return scaleGroup;
    }

    public DisplayPane displayPane() {
        return displayPane;
    }

    public WindowControls windowControls() {
        return windowControls;
    }

    public void attachStage(Stage stage) {
        windowControls.attach(stage);
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
        displayPane.setExpressionText(engine.expressionText());
        if (clearButton != null) {
            clearButton.setText(engine.clearLabel());
        }
        operatorButtons.forEach((operator, button) -> button.setActive(operator.equals(engine.activeOperator())));
    }
}
