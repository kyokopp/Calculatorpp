package calculatorpp;

import com.pixelduke.window.ThemeWindowManagerFactory;
import com.pixelduke.window.Win11ThemeWindowManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class CalculatorApp extends Application {

    private final Calculadora calculadora = new Calculadora();
    private final TextField primeiroValor = new TextField();
    private final TextField segundoValor = new TextField();
    private final TextField resultado = new TextField();

    public static void main(String[] args) {
        System.setProperty("prism.forceUploadingPainter", "true");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNIFIED);

        primeiroValor.setPromptText("Primeiro valor");
        segundoValor.setPromptText("Segundo valor");
        resultado.setPromptText("Resultado");
        resultado.setEditable(false);
        resultado.setFocusTraversable(false);
        resultado.setText("0");

        primeiroValor.getStyleClass().add("operand-field");
        segundoValor.getStyleClass().add("operand-field");
        resultado.getStyleClass().add("display-field");

        GridPane botoes = new GridPane();
        botoes.getStyleClass().add("operator-pad");
        botoes.setHgap(12);
        botoes.setVgap(12);
        botoes.setAlignment(Pos.CENTER);
        botoes.getColumnConstraints().addAll(criarColuna(), criarColuna());

        botoes.add(criarBotao("+"), 0, 0);
        botoes.add(criarBotao("-"), 1, 0);
        botoes.add(criarBotao("*"), 0, 1);
        botoes.add(criarBotao("/"), 1, 1);

        HBox entradas = new HBox(8, primeiroValor, segundoValor);
        entradas.getStyleClass().add("input-row");
        entradas.setAlignment(Pos.CENTER);
        HBox.setHgrow(primeiroValor, Priority.ALWAYS);
        HBox.setHgrow(segundoValor, Priority.ALWAYS);

        VBox raiz = new VBox(14, resultado, entradas, botoes);
        raiz.getStyleClass().add("calculator-shell");
        raiz.setPadding(new Insets(22));
        raiz.setAlignment(Pos.CENTER);

        Scene scene = new Scene(raiz, 330, 430);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/calculatorpp/calculator.css")).toExternalForm());

        stage.setTitle("Calculatorpp");
        stage.setScene(scene);
        stage.show();
        aplicarEfeitoNativo(stage);
    }

    private Button criarBotao(String operador) {
        Button botao = new Button(operador);
        botao.getStyleClass().addAll("calculator-button", "operator-button");
        botao.setMinSize(68, 68);
        botao.setPrefSize(68, 68);
        botao.setOnAction(event -> calcular(operador));
        return botao;
    }

    private ColumnConstraints criarColuna() {
        ColumnConstraints coluna = new ColumnConstraints();
        coluna.setHgrow(Priority.ALWAYS);
        coluna.setFillWidth(true);
        return coluna;
    }

    private void aplicarEfeitoNativo(Stage stage) {
        try {
            if (ThemeWindowManagerFactory.create() instanceof Win11ThemeWindowManager gerenciador) {
                gerenciador.setDarkModeForWindowFrame(stage, true);
                gerenciador.setWindowBackdrop(stage, Win11ThemeWindowManager.Backdrop.ACRYLIC);
                gerenciador.setWindowCornerPreference(stage, Win11ThemeWindowManager.CornerPreference.ROUND);
                gerenciador.setWindowFrameColor(stage, Color.rgb(24, 24, 26));
                gerenciador.setWindowTextColor(stage, Color.rgb(245, 245, 247));
                gerenciador.setWindowBorderColor(stage, Color.rgb(76, 76, 80));
            }
        } catch (RuntimeException | LinkageError ignored) {
        }
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
