package com.calculator;

import javafx.scene.text.Font;

import java.io.File;

public final class FontLoader {

    private FontLoader() {
    }

    public static void loadFonts() {
        // List SF Pro font files found on this system for diagnostics
        File fontsDir = new File("C:/Windows/Fonts/");
        String[] sfFiles = fontsDir.list((dir, name) -> name.toLowerCase().contains("sf") && name.toLowerCase().contains("pro"));
        if (sfFiles != null && sfFiles.length > 0) {
            System.out.println("SF Pro font files found in C:/Windows/Fonts/:");
            for (String f : sfFiles) {
                System.out.println("  " + f);
            }
        } else {
            System.err.println("No SF Pro font files found in C:/Windows/Fonts/");
        }

        String[] lightVariants = {
                "file:///C:/Windows/Fonts/SF%20Pro%20Rounded%20Light.otf",
                "file:///C:/Windows/Fonts/SFProRounded-Light.otf",
                "file:///C:/Windows/Fonts/SF%20Pro%20Rounded%20Light.ttf",
                "file:///C:/Windows/Fonts/SF-Pro-Rounded-Light.otf",
                "file:///C:/Windows/Fonts/SF-Pro-Rounded-Light.ttf"
        };

        String[] boldVariants = {
                "file:///C:/Windows/Fonts/SF%20Pro%20Rounded%20Bold.otf",
                "file:///C:/Windows/Fonts/SFProRounded-Bold.otf",
                "file:///C:/Windows/Fonts/SF%20Pro%20Rounded%20Bold.ttf",
                "file:///C:/Windows/Fonts/SF-Pro-Rounded-Bold.otf",
                "file:///C:/Windows/Fonts/SF-Pro-Rounded-Bold.ttf"
        };

        Font light = tryLoadFont("light", lightVariants);
        Font bold = tryLoadFont("bold", boldVariants);
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
            System.err.println("Warning: SF Pro Rounded " + variant + " font was not found. Falling back to Segoe UI Variable Display, then Segoe UI.");
            return;
        }
        System.out.println("Loaded: " + font.getFamily());
        if (!"SF Pro Rounded".equals(font.getFamily())) {
            System.err.println("Warning: expected SF Pro Rounded for " + variant + " but loaded " + font.getFamily() + ". Falling back to Segoe UI Variable Display, then Segoe UI if CSS font matching cannot use it.");
        }
    }
}
