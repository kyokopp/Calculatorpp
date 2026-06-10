package com.calculator;

import javafx.scene.text.Font;

public final class FontLoader {

    private FontLoader() {
    }

    public static void loadFonts() {
        Font regular = Font.loadFont("file:///C:/Windows/Fonts/SF%20Pro%20Rounded%20Light.otf", 14);
        Font light = Font.loadFont("file:///C:/Windows/Fonts/SF%20Pro%20Rounded%20Light.otf", 14);
        Font bold = Font.loadFont("file:///C:/Windows/Fonts/SF%20Pro%20Rounded%20Bold.otf", 14);

        logLoaded("regular", regular);
        logLoaded("light", light);
        logLoaded("bold", bold);
    }

    private static void logLoaded(String variant, Font font) {
        if (font == null) {
            System.err.println("Warning: SF Pro Rounded " + variant + " font was not found. Falling back to Segoe UI Variable Display, then Segoe UI.");
            return;
        }
        System.out.println("Loaded: " + font.getFamily());
        if (!"SF Pro Rounded".equals(font.getFamily())) {
            System.err.println("Warning: expected SF Pro Rounded for " + variant + " but loaded " + font.getFamily() + ". Falling back to Segoe UI Variable Display, then Segoe UI if CSS font matching cannot use it.");
        }
    }
}
