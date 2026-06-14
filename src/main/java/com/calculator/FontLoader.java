package com.calculator;

import javafx.scene.text.Font;

import java.io.File;

public final class FontLoader {

    private FontLoader() {
    }

    public static void loadFonts() {
        // List SF Pro font files found on this system for diagnostics
        File fontsDir = new File("F:/AA/STUDIES/Projects/Calculatorpp/src/main/resources/fonts/:");
        String[] sfFiles = fontsDir.list((dir, name) -> name.toLowerCase().contains("sf") && name.toLowerCase().contains("pro"));
        String[] sfFiles2 = fontsDir.list((dir, name) -> name.toLowerCase().contains("sf") && name.toLowerCase().contains("pro"));
        if (sfFiles != null && sfFiles.length > 0) {
            System.out.println("SF Pro Display font files found in F:/AA/STUDIES/Projects/Calculatorpp/src/main/resources/fonts/:");
            for (String f : sfFiles) {
                System.out.println("  " + f);
            }
        } else {
            System.err.println("No SF Pro font files found in F:/AA/STUDIES/Projects/Calculatorpp/src/main/resources/fonts/");
        }

        String[] lightVariants = {
                "file://F:/AA/STUDIES/Projects/Calculatorpp/src/main/resources/fonts/SF-Pro-Display-Semibold.otf",
                "file:///C:/Windows/Fonts/SF-Pro-Display-Light.otf"
        };

        String[] semiboldVariants = {
                "file://F:/AA/STUDIES/Projects/Calculatorpp/src/main/resources/fonts/SF-Pro-Display-Semibold.otf",
                "file:///C:/Windows/Fonts/SF-Pro-Display-Semibold.otf"
        };

        Font light = tryLoadFont("light", lightVariants);
        Font bold = tryLoadFont("bold", semiboldVariants);
        logLoaded("light", light);
        logLoaded("bold", bold);
    }

    private static Font tryLoadFont(String variant, String[] paths) {
        for (String path : paths) {
            Font font = Font.loadFont(path, 14);
            if (font != null) {
                System.out.println("Loaded " + variant + " from: " + path);
                return font;
            }
        }
        return null;
    }

    private static void logLoaded(String variant, Font font) {
        if (font == null) {
            System.err.println("Warning: SF Pro Display " + variant + " font was not found. Falling back to Segoe UI Variable Display, then Segoe UI.");
            return;
        }
        System.out.println("Loaded: " + font.getFamily());
        if (!"SF Pro Display".equals(font.getFamily())) {
            System.err.println("Warning: expected SF Pro Display for " + variant + " but loaded " + font.getFamily() + ". Falling back to Segoe UI Variable Display, then Segoe UI if CSS font matching cannot use it.");
        }
    }
}
