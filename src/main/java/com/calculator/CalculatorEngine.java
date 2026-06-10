package com.calculator;

import calculatorpp.Calculadora;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorEngine {

    private final Calculadora calculadora = new Calculadora();

    private String currentInput = "0";
    private Double accumulator;
    private String pendingOperator;
    private String activeOperator;
    private String lastOperator;
    private Double lastOperand;
    private boolean enteringNewNumber;
    private boolean error;

    public void inputDigit(String digit) {
        recoverFromError();
        if (enteringNewNumber) {
            currentInput = digit;
            enteringNewNumber = false;
            return;
        }
        if ("0".equals(currentInput)) {
            currentInput = digit;
            return;
        }
        if ("-0".equals(currentInput)) {
            currentInput = "-" + digit;
            return;
        }
        currentInput += digit;
    }

    public void inputDecimal() {
        recoverFromError();
        if (enteringNewNumber) {
            currentInput = "0.";
            enteringNewNumber = false;
            return;
        }
        if (!currentInput.contains(".")) {
            currentInput += ".";
        }
    }

    public void chooseOperator(String operator) {
        recoverFromError();
        double value = currentValue();
        if (pendingOperator != null && !enteringNewNumber) {
            if (!applyPending(value)) {
                return;
            }
        } else {
            accumulator = value;
        }
        pendingOperator = operator;
        activeOperator = operator;
        enteringNewNumber = true;
    }

    public void equals() {
        recoverFromError();
        if (pendingOperator != null) {
            double operand = currentValue();
            String operator = pendingOperator;
            if (applyPending(operand)) {
                lastOperator = operator;
                lastOperand = operand;
                pendingOperator = null;
                activeOperator = null;
                enteringNewNumber = true;
            }
            return;
        }
        if (lastOperator != null && lastOperand != null) {
            double result = calculate(currentValue(), lastOperand, lastOperator);
            currentInput = format(result);
            accumulator = result;
            enteringNewNumber = true;
        }
    }

    public void clear() {
        if ("AC".equals(clearLabel())) {
            resetAll();
            return;
        }
        currentInput = "0";
        error = false;
        enteringNewNumber = false;
    }

    public void toggleSign() {
        recoverFromError();
        if ("0".equals(currentInput)) {
            currentInput = "-0";
            return;
        }
        if ("-0".equals(currentInput)) {
            currentInput = "0";
            return;
        }
        if (currentInput.startsWith("-")) {
            currentInput = currentInput.substring(1);
            return;
        }
        currentInput = "-" + currentInput;
    }

    public void percent() {
        recoverFromError();
        currentInput = format(currentValue() / 100.0);
        enteringNewNumber = false;
    }

    public void backspace() {
        recoverFromError();
        if (enteringNewNumber) {
            return;
        }
        if (currentInput.length() <= 1 || currentInput.matches("-\\d?")) {
            currentInput = "0";
            return;
        }
        currentInput = currentInput.substring(0, currentInput.length() - 1);
    }

    public String displayText() {
        return error ? "Error" : normalizeCurrentInput();
    }

    public String clearLabel() {
        if (error) {
            return "AC";
        }
        return "0".equals(normalizeCurrentInput()) && pendingOperator == null ? "AC" : "C";
    }

    public String activeOperator() {
        return activeOperator;
    }

    private boolean applyPending(double operand) {
        try {
            double result = calculate(accumulator, operand, pendingOperator);
            accumulator = result;
            currentInput = format(result);
            enteringNewNumber = true;
            return true;
        } catch (ArithmeticException exception) {
            error = true;
            currentInput = "0";
            accumulator = null;
            pendingOperator = null;
            activeOperator = null;
            return false;
        }
    }

    private double calculate(double left, double right, String operator) {
        return switch (operator) {
            case "+" -> calculadora.somar(left, right);
            case "−" -> calculadora.subtrair(left, right);
            case "×" -> calculadora.multiplicar(left, right);
            case "÷" -> calculadora.dividir(left, right);
            default -> right;
        };
    }

    private double currentValue() {
        if ("-0".equals(currentInput) || "-0.".equals(currentInput)) {
            return 0.0;
        }
        return Double.parseDouble(currentInput);
    }

    private String normalizeCurrentInput() {
        if ("-0".equals(currentInput)) {
            return "-0";
        }
        if (currentInput.endsWith(".") && currentInput.indexOf('.') == currentInput.length() - 1) {
            return currentInput;
        }
        return format(currentValue());
    }

    private String format(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            error = true;
            return "0";
        }
        BigDecimal decimal = BigDecimal.valueOf(value).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros();
        String formatted = decimal.toPlainString();
        return "-0".equals(formatted) ? "0" : formatted;
    }

    private void recoverFromError() {
        if (error) {
            resetAll();
        }
    }

    private void resetAll() {
        currentInput = "0";
        accumulator = null;
        pendingOperator = null;
        activeOperator = null;
        lastOperator = null;
        lastOperand = null;
        enteringNewNumber = false;
        error = false;
    }
}
