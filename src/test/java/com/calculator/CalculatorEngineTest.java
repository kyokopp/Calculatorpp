package com.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorEngineTest {

    @Test
    void performsChainedOperations() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.inputDigit("8");
        engine.chooseOperator("+");
        engine.inputDigit("2");
        engine.chooseOperator("×");
        engine.inputDigit("3");
        engine.equals();

        assertEquals("30", engine.displayText());
    }

    @Test
    void repeatsLastOperationWhenEqualsIsPressedAgain() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.inputDigit("5");
        engine.chooseOperator("+");
        engine.inputDigit("2");
        engine.equals();
        engine.equals();

        assertEquals("9", engine.displayText());
    }

    @Test
    void handlesSignPercentDecimalClearAndBackspace() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.inputDigit("1");
        engine.inputDigit("2");
        engine.inputDecimal();
        engine.inputDigit("5");
        engine.inputDecimal();
        engine.toggleSign();
        engine.percent();
        engine.backspace();

        assertEquals("-0.12", engine.displayText());
        assertEquals("C", engine.clearLabel());

        engine.clear();

        assertEquals("0", engine.displayText());
        assertEquals("AC", engine.clearLabel());
    }

    @Test
    void showsErrorForDivisionByZero() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.inputDigit("9");
        engine.chooseOperator("÷");
        engine.inputDigit("0");
        engine.equals();

        assertEquals("Error", engine.displayText());

        engine.inputDigit("4");

        assertEquals("4", engine.displayText());
    }

    @Test
    void keepsCompletedExpressionAfterEquals() {
        CalculatorEngine engine = new CalculatorEngine();

        engine.inputDigit("5");
        engine.chooseOperator("+");
        engine.inputDigit("2");
        engine.equals();

        assertEquals("7", engine.displayText());
        assertEquals("5 + 2 =", engine.expressionText());
    }
}
