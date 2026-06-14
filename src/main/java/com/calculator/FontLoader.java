package com.calculator;

import javafx.scene.text.Font;

import java.io.InputStream;

public final class FontLoader {

    private FontLoader() {
    }

    public static void loadFonts() {
        loadVariant(
                "light",
                "/fonts/SF-Pro-Display-Light.otf",
                "file:///F:/AA/STUDIES/Projects/Calculatorpp/src/main/resources/fonts/SF-Pro-Display-Light.otf",
                "file:///C:/Windows/Fonts/SF-Pro-Display-Light.otf"
        );
        loadVariant(
                "bold",
                "/fonts/SF-Pro-Display-Bold.otf",
                "file:///F:/AA/STUDIES/Projects/Calculatorpp/src/main/resources/fonts/SF-Pro-Display-Bold.otf",
                "file:///C:/Windows/Fonts/SF-Pro-Display-Bold.otf"
        );
    }

    private static void loadVariant(String variant, String classpathUri, String... fileUris) {
        try (InputStream stream = FontLoader.class.getResourceAsStream(classpathUri)) {
            if (stream != null) {
                Font font = Font.loadFont(stream, 14);
                if (font != null && "SF Pro Display".equals(font.getFamily())) {
                    System.out.println("Loaded SF Pro Display " + variant + " from classpath.");
                    return;
                }
            }
        } catch (Exception exception) {
            System.err.println("Warning: unable to load SF Pro Display " + variant + " from classpath.");
        }

        for (String uri : fileUris) {
            Font font = Font.loadFont(uri, 14);
            if (font != null && "SF Pro Display".equals(font.getFamily())) {
                System.out.println("Loaded SF Pro Display " + variant + " from: " + uri);
                return;
            }
        }

        System.err.println("Warning: SF Pro Display " + variant + " not found. Falling back to Segoe UI Variable Display, then Segoe UI.");
    }
}
