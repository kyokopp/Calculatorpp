package calculatorpp;

public class Calculadora {

    public double somar(double primeiroValor, double segundoValor) {
        return primeiroValor + segundoValor;
    }

    public double subtrair(double primeiroValor, double segundoValor) {
        return primeiroValor - segundoValor;
    }

    public double multiplicar(double primeiroValor, double segundoValor) {
        return primeiroValor * segundoValor;
    }

    public double dividir(double numerador, double denominador) {
        if (denominador == 0.0) {
            throw new ArithmeticException("Divisao por zero");
        }

        return numerador / denominador;
    }
}
