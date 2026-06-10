package com.calculator;

import javafx.scene.text.Font;

public final class FontLoader {

    private static final String[] REGULAR_VARIANTS = {
            "SF-Pro-Rounded-Regular.otf",
            "SFProRounded-Regular.otf",
            "SF Pro Rounded Regular.otf"
    };

    private static final String[] LIGHT_VARIANTS = {
            "SF-Pro-Rounded-Thin.otf",
            "SFProRounded-Thin.otf",
            "SF Pro Rounded Thin.otf",
            "SF-Pro-Rounded-Light.otf",
            "SFProRounded-Light.otf",
            "SF Pro Rounded Light.otf"
    };

    private FontLoader() {
    }

    public static void loadFonts() {
        Font regular = loadFirst(REGULAR_VARIANTS);
        Font light = loadFirst(LIGHT_VARIANTS);

        if (regular == null) {
            System.err.println("Warning: SF Pro Rounded regular font was not found. Falling back to Segoe UI Variable Display.");
        }
        if (light == null) {
            System.err.println("Warning: SF Pro Rounded light font was not found. Falling back to Segoe UI Variable Display.");
        }
    }

    private static Font loadFirst(String[] filenames) {
        for (String filename : filenames) {
            Font font = Font.loadFont("file:///C:/Windows/Fonts/" + filename.replace(" ", "%20"), 14);
            if (font != null) {
                return font;
            }
        }
        return null;
    }
}
