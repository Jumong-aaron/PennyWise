import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Locale;

public final class Theme {

    private Theme() {
    }

    public static final String PESO = "\u20B1";
    public static final String LOGO_FILE = "images/pennywise_logo.png";

    public static final Color DARK_GREEN = new Color(15, 96, 70);
    public static final Color LIGHT_GREEN = new Color(225, 244, 232);
    public static final Color BACKGROUND = new Color(247, 250, 250);
    public static final Color BORDER_COLOR = new Color(190, 220, 215);
    public static final Color BUTTON_GREEN = new Color(45, 155, 85);
    public static final Color BUTTON_GREEN_HOVER = new Color(35, 130, 72);
    public static final Color GOLD = new Color(245, 180, 30);

    private static final Font BUBBLE_BASE = loadBubbleFont();

    private static Font loadBubbleFont() {
        File[] files = new File("fonts").listFiles((dir, name) -> {
            String n = name.toLowerCase(Locale.ROOT);
            return n.endsWith(".ttf") || n.endsWith(".otf");
        });
        if (files == null) {
            return null;
        }
        for (File file : files) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, file);
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .registerFont(font);
                return font;
            } catch (Exception ex) {
                
            }
        }
        return null;
    }

    public static Font bubble(int style, float size) {
        if (BUBBLE_BASE != null) {
            return BUBBLE_BASE.deriveFont(Font.PLAIN, size);
        }
        return new Font("Comic Sans MS", style, Math.round(size));
    }

    public static Font money(int style, float size) {
        Font f = bubble(style, size);
        if (f.canDisplay('\u20B1')) {
            return f;
        }
        return new Font("Dialog", style, Math.round(size));
    }
}

