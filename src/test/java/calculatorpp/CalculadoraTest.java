package calculatorpp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraTest {

    private static final double DELTA = 0.000001;

    private final Calculadora calculadora = new Calculadora();

    @Test
    void somaNumerosPositivosNegativosEZero() {
        assertEquals(7.5, calculadora.somar(3.0, 4.5), DELTA);
        assertEquals(-8.0, calculadora.somar(-3.5, -4.5), DELTA);
        assertEquals(-1.5, calculadora.somar(3.0, -4.5), DELTA);
        assertEquals(5.25, calculadora.somar(5.25, 0.0), DELTA);
    }

    @Test
    void subtraiNumerosPositivosNegativosEZero() {
        assertEquals(2.5, calculadora.subtrair(7.0, 4.5), DELTA);
        assertEquals(1.0, calculadora.subtrair(-3.5, -4.5), DELTA);
        assertEquals(7.5, calculadora.subtrair(3.0, -4.5), DELTA);
        assertEquals(5.25, calculadora.subtrair(5.25, 0.0), DELTA);
    }

    @Test
    void multiplicaNumerosPositivosNegativosEZero() {
        assertEquals(13.5, calculadora.multiplicar(3.0, 4.5), DELTA);
        assertEquals(15.75, calculadora.multiplicar(-3.5, -4.5), DELTA);
        assertEquals(-13.5, calculadora.multiplicar(3.0, -4.5), DELTA);
        assertEquals(0.0, calculadora.multiplicar(5.25, 0.0), DELTA);
    }

    @Test
    void divideNumerosPositivosNegativosEZero() {
        assertEquals(2.0, calculadora.dividir(9.0, 4.5), DELTA);
        assertEquals(2.0, calculadora.dividir(-9.0, -4.5), DELTA);
        assertEquals(-2.0, calculadora.dividir(9.0, -4.5), DELTA);
        assertEquals(0.0, calculadora.dividir(0.0, 4.5), DELTA);
    }

    @Test
    void lancaArithmeticExceptionAoDividirPorZero() {
        assertThrows(ArithmeticException.class, () -> calculadora.dividir(10.0, 0.0));
    }
}
