package calculatorpp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    private final Calculadora calculadora = new Calculadora();
    private final TextField primeiroValor = new TextField();
    private final TextField segundoValor = new TextField();
    private final TextField resultado = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primeiroValor.setPromptText("Primeiro valor");
        segundoValor.setPromptText("Segundo valor");
        resultado.setPromptText("Resultado");
        resultado.setEditable(false);
        resultado.setFocusTraversable(false);

        GridPane botoes = new GridPane();
        botoes.setHgap(8);
        botoes.setVgap(8);
        botoes.setAlignment(Pos.CENTER);

        botoes.add(criarBotao("+"), 0, 0);
        botoes.add(criarBotao("-"), 1, 0);
        botoes.add(criarBotao("*"), 0, 1);
        botoes.add(criarBotao("/"), 1, 1);

        HBox entradas = new HBox(8, primeiroValor, segundoValor);
        entradas.setAlignment(Pos.CENTER);

        VBox raiz = new VBox(12, entradas, botoes, resultado);
        raiz.setPadding(new Insets(16));
        raiz.setAlignment(Pos.CENTER);

        Scene scene = new Scene(raiz, 360, 180);
        stage.setTitle("Calculatorpp");
        stage.setScene(scene);
        stage.show();
    }

    private Button criarBotao(String operador) {
        Button botao = new Button(operador);
        botao.setMinSize(64, 36);
        botao.setOnAction(event -> calcular(operador));
        return botao;
    }

    private void calcular(String operador) {
        try {
            double primeiro = Double.parseDouble(primeiroValor.getText());
            double segundo = Double.parseDouble(segundoValor.getText());
            double valor = switch (operador) {
                case "+" -> calculadora.somar(primeiro, segundo);
                case "-" -> calculadora.subtrair(primeiro, segundo);
                case "*" -> calculadora.multiplicar(primeiro, segundo);
                case "/" -> calculadora.dividir(primeiro, segundo);
                default -> throw new IllegalArgumentException("Operador invalido");
            };
            resultado.setText(Double.toString(valor));
        } catch (NumberFormatException exception) {
            resultado.setText("Entrada invalida");
        } catch (ArithmeticException exception) {
            resultado.setText(exception.getMessage());
        }
    }
}
