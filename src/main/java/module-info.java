module calculatorpp {
    requires javafx.controls;
    requires java.desktop;
    requires com.sun.jna;
    requires com.sun.jna.platform;

    exports calculatorpp;
    exports com.calculator;
    exports com.calculator.ui;
}
