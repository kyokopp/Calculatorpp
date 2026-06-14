package com.calculator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    private static final double BASE_WIDTH = 336.0;
    private static final double CONTENT_HEIGHT = 560.0;
    private static final double RESIZE_BORDER = 6.0;

    private double dragOffsetX;
    private double dragOffsetY;
    private double resizeStartScreenX;
    private double resizeStartScreenY;
    private double resizeStartStageX;
    private double resizeStartStageY;
    private double resizeStartWidth;
    private double resizeStartHeight;
    private boolean manualResizing;
    private boolean isMoving;
    private boolean scaleDirty;
    private ResizeDirection activeResizeDirection = ResizeDirection.NONE;

    public static void main(String[] args) {
        System.setProperty("prism.forceUploadingPainter", "true");
        System.setProperty("prism.lcdtext", "true");
        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.subpixeltext", "true");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        FontLoader.loadFonts();

        CalculatorController controller = new CalculatorController();
        Scene scene = new Scene(controller.root(), BASE_WIDTH, CONTENT_HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/calculator.css")).toExternalForm());

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Calculatorpp");
        stage.setResizable(true);
        stage.setMinWidth(200);
        stage.setMinHeight(334);
        stage.setScene(scene);
        controller.attachStage(stage);

        controller.displayPane().setOnMousePressed(event -> {
            isMoving = true;
            dragOffsetX = event.getScreenX() - stage.getX();
            dragOffsetY = event.getScreenY() - stage.getY();
        });
        controller.displayPane().setOnMouseDragged(event -> {
            if (isMoving && !manualResizing && !stage.isMaximized()) {
                stage.setX(event.getScreenX() - dragOffsetX);
                stage.setY(event.getScreenY() - dragOffsetY);
            }
        });
        controller.displayPane().setOnMouseReleased(event -> isMoving = false);

        controller.root().setOnMouseMoved(event -> {
            if (isMoving || manualResizing) {
                return;
            }
            if (!isNearEdge(event, scene)) {
                activeResizeDirection = ResizeDirection.NONE;
                controller.root().setCursor(Cursor.DEFAULT);
                return;
            }
            activeResizeDirection = resizeDirection(event, scene);
            controller.root().setCursor(activeResizeDirection.cursor);
        });
        controller.root().setOnMouseDragged(event -> {
            if (isMoving) {
                return;
            }
            if (activeResizeDirection != ResizeDirection.NONE || resizeDirection(event, scene) != ResizeDirection.NONE) {
                resize(event, scene, stage);
            }
        });
        controller.root().setOnMouseReleased(event -> {
            manualResizing = false;
            isMoving = false;
        });

        stage.widthProperty().addListener((observable, oldValue, newValue) -> scaleDirty = true);
        stage.heightProperty().addListener((observable, oldValue, newValue) -> scaleDirty = true);

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            String text = event.getText();
            if (text != null && text.matches("[0-9]")) {
                controller.inputDigit(text);
                return;
            }
            if ("+".equals(text)) {
                controller.chooseOperator("+");
                return;
            }
            if ("-".equals(text)) {
                controller.chooseOperator("−");
                return;
            }
            if ("*".equals(text) || "x".equalsIgnoreCase(text)) {
                controller.chooseOperator("×");
                return;
            }
            if ("/".equals(text)) {
                controller.chooseOperator("÷");
                return;
            }
            if (".".equals(text) || ",".equals(text)) {
                controller.inputDecimal();
                return;
            }
            if (code == KeyCode.ENTER || code == KeyCode.EQUALS) {
                controller.equals();
                return;
            }
            if (code == KeyCode.BACK_SPACE) {
                controller.backspace();
                return;
            }
            if (code == KeyCode.ESCAPE) {
                controller.clear();
            }
        });

        stage.setOnShown(event -> {
            applyScale(stage, controller);
            WindowsTransparency.apply(stage, WindowsTransparency.Backdrop.ACRYLIC);
            controller.root().requestFocus();
        });
        stage.show();
        applyScale(stage, controller);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (scaleDirty) {
                    applyScale(stage, controller);
                    scaleDirty = false;
                }
            }
        }.start();
        controller.root().requestFocus();
    }

    private void applyScale(Stage stage, CalculatorController controller) {
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();
        double scale = Math.min(stageWidth / BASE_WIDTH, stageHeight / CONTENT_HEIGHT);
        scale = Math.max(scale, 0.3);
        Group scaleGroup = controller.scaleGroup();
        scaleGroup.setScaleX(scale);
        scaleGroup.setScaleY(scale);
        scaleGroup.setTranslateX((stageWidth - BASE_WIDTH * scale) / 2.0);
        scaleGroup.setTranslateY((stageHeight - CONTENT_HEIGHT * scale) / 2.0);
    }

    private boolean isNearEdge(MouseEvent event, Scene scene) {
        double x = event.getSceneX();
        double y = event.getSceneY();
        double width = scene.getWidth();
        double height = scene.getHeight();
        return x <= RESIZE_BORDER || x >= width - RESIZE_BORDER || y <= RESIZE_BORDER || y >= height - RESIZE_BORDER;
    }

    private ResizeDirection resizeDirection(MouseEvent event, Scene scene) {
        double x = event.getSceneX();
        double y = event.getSceneY();
        boolean left = x <= RESIZE_BORDER;
        boolean right = x >= scene.getWidth() - RESIZE_BORDER;
        boolean top = y <= RESIZE_BORDER;
        boolean bottom = y >= scene.getHeight() - RESIZE_BORDER;

        if (top && left) {
            return ResizeDirection.NORTH_WEST;
        }
        if (top && right) {
            return ResizeDirection.NORTH_EAST;
        }
        if (bottom && left) {
            return ResizeDirection.SOUTH_WEST;
        }
        if (bottom && right) {
            return ResizeDirection.SOUTH_EAST;
        }
        if (top) {
            return ResizeDirection.NORTH;
        }
        if (bottom) {
            return ResizeDirection.SOUTH;
        }
        if (left) {
            return ResizeDirection.WEST;
        }
        if (right) {
            return ResizeDirection.EAST;
        }
        return ResizeDirection.NONE;
    }

    private void resize(MouseEvent event, Scene scene, Stage stage) {
        if (stage.isMaximized()) {
            manualResizing = false;
            return;
        }
        if (!manualResizing) {
            activeResizeDirection = resizeDirection(event, scene);
            if (activeResizeDirection == ResizeDirection.NONE) {
                return;
            }
            resizeStartScreenX = event.getScreenX();
            resizeStartScreenY = event.getScreenY();
            resizeStartStageX = stage.getX();
            resizeStartStageY = stage.getY();
            resizeStartWidth = stage.getWidth();
            resizeStartHeight = stage.getHeight();
            manualResizing = true;
        }

        double deltaX = event.getScreenX() - resizeStartScreenX;
        double deltaY = event.getScreenY() - resizeStartScreenY;
        double x = resizeStartStageX;
        double y = resizeStartStageY;
        double width = resizeStartWidth;
        double height = resizeStartHeight;

        if (activeResizeDirection.west) {
            width = Math.max(stage.getMinWidth(), resizeStartWidth - deltaX);
            x = resizeStartStageX + resizeStartWidth - width;
        }
        if (activeResizeDirection.east) {
            width = Math.max(stage.getMinWidth(), resizeStartWidth + deltaX);
        }
        if (activeResizeDirection.north) {
            height = Math.max(stage.getMinHeight(), resizeStartHeight - deltaY);
            y = resizeStartStageY + resizeStartHeight - height;
        }
        if (activeResizeDirection.south) {
            height = Math.max(stage.getMinHeight(), resizeStartHeight + deltaY);
        }

        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);
        event.consume();
    }

    private enum ResizeDirection {
        NONE(Cursor.DEFAULT, false, false, false, false),
        NORTH(Cursor.N_RESIZE, true, false, false, false),
        SOUTH(Cursor.S_RESIZE, false, true, false, false),
        WEST(Cursor.W_RESIZE, false, false, true, false),
        EAST(Cursor.E_RESIZE, false, false, false, true),
        NORTH_WEST(Cursor.NW_RESIZE, true, false, true, false),
        NORTH_EAST(Cursor.NE_RESIZE, true, false, false, true),
        SOUTH_WEST(Cursor.SW_RESIZE, false, true, true, false),
        SOUTH_EAST(Cursor.SE_RESIZE, false, true, false, true);

        private final Cursor cursor;
        private final boolean north;
        private final boolean south;
        private final boolean west;
        private final boolean east;

        ResizeDirection(Cursor cursor, boolean north, boolean south, boolean west, boolean east) {
            this.cursor = cursor;
            this.north = north;
            this.south = south;
            this.west = west;
            this.east = east;
        }
    }
}
