package com.calculator;

import com.sun.glass.ui.Window;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import javafx.stage.Stage;

import java.util.List;

public final class WindowsTransparency {

    private static final int DWMWA_WINDOW_CORNER_PREFERENCE = 33;
    private static final int DWMWA_SYSTEMBACKDROP_TYPE = 38;
    private static final int DWMWA_USE_IMMERSIVE_DARK_MODE = 20;
    private static final int DWM_BB_ENABLE = 1;

    private WindowsTransparency() {
    }

    public enum Backdrop {
        MICA(2),
        ACRYLIC(3);

        private final int value;

        Backdrop(int value) {
            this.value = value;
        }
    }

    public static void apply(Stage stage, Backdrop backdrop) {
        if (!System.getProperty("os.name", "").toLowerCase().contains("win")) {
            return;
        }

        try {
            WinDef.HWND hwnd = findHandle(stage);
            setIntAttribute(hwnd, DWMWA_USE_IMMERSIVE_DARK_MODE, 1);
            setIntAttribute(hwnd, DWMWA_WINDOW_CORNER_PREFERENCE, 2);
            setIntAttribute(hwnd, DWMWA_SYSTEMBACKDROP_TYPE, backdrop.value);
            enableBlurFallback(hwnd);
        } catch (RuntimeException | LinkageError ignored) {
        }
    }

    private static WinDef.HWND findHandle(Stage stage) {
        for (Window window : Window.getWindowsClone()) {
            if (stage.getTitle().equals(window.getTitle())) {
                return new WinDef.HWND(Pointer.createConstant(window.getRawHandle()));
            }
        }
        throw new IllegalStateException("Window handle unavailable");
    }

    private static void setIntAttribute(WinDef.HWND hwnd, int attribute, int value) {
        IntByReference reference = new IntByReference(value);
        Dwm.INSTANCE.DwmSetWindowAttribute(hwnd, attribute, reference, Integer.BYTES);
    }

    private static void enableBlurFallback(WinDef.HWND hwnd) {
        BlurBehind blurBehind = new BlurBehind();
        blurBehind.dwFlags = DWM_BB_ENABLE;
        blurBehind.fEnable = 1;
        blurBehind.hRgnBlur = null;
        blurBehind.fTransitionOnMaximized = 1;
        blurBehind.write();
        Dwm.INSTANCE.DwmEnableBlurBehindWindow(hwnd, blurBehind);
    }

    public interface Dwm extends StdCallLibrary {
        Dwm INSTANCE = Native.load("dwmapi", Dwm.class);

        int DwmSetWindowAttribute(WinDef.HWND hwnd, int attribute, IntByReference attributeValue, int attributeSize);

        int DwmEnableBlurBehindWindow(WinDef.HWND hwnd, BlurBehind blurBehind);
    }

    public static class BlurBehind extends Structure {
        public int dwFlags;
        public int fEnable;
        public WinDef.HRGN hRgnBlur;
        public int fTransitionOnMaximized;

        @Override
        protected List<String> getFieldOrder() {
            return List.of("dwFlags", "fEnable", "hRgnBlur", "fTransitionOnMaximized");
        }
    }
}
