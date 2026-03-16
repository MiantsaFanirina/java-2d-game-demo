package Engine;

import java.awt.*;

public class HeroSelectionConstants {

    public static final Color BACKGROUND = new Color(25, 25, 40);
    public static final Color HEADER_BG = new Color(35, 35, 55, 230);
    public static final Color CARD_BG = new Color(50, 50, 75);
    public static final Color CARD_HOVER = new Color(70, 70, 100);
    public static final Color CARD_SELECTED = new Color(90, 130, 200);
    public static final Color CARD_BORDER = new Color(100, 100, 140);
    public static final Color SELECTED_BORDER = new Color(255, 215, 0);
    public static final Color TEXT_MAIN = new Color(240, 240, 240);
    public static final Color TEXT_SECONDARY = new Color(180, 180, 200);
    public static final Color STAT_POSITIVE = new Color(120, 230, 120);
    public static final Color TAB_BG = new Color(45, 45, 65);
    public static final Color TAB_SELECTED = new Color(60, 90, 140);
    public static final Color TAB_HOVER = new Color(55, 55, 80);
    public static final Color SCROLLBAR_BG = new Color(80, 80, 110, 200);
    public static final Color SCROLLBAR_THUMB = new Color(150, 150, 190);

    public static final int HEADER_HEIGHT = 140;
    public static final int FOOTER_HEIGHT = 100;
    public static final int VERTICAL_PADDING = 20;
    public static final int HORIZONTAL_PADDING = 30;

    public static final int TAB_HEIGHT = 30;
    public static final int TAB_WIDTH = 120;
    public static final int TAB_SPACING = 8;
    public static final int TAB_Y_OFFSET = 60;

    public static final int DEFAULT_CARD_WIDTH = 200;
    public static final int DEFAULT_CARD_HEIGHT = 170;
    public static final int CARD_SPACING = 20;
    public static final int SPRITE_SIZE = 56;

    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 28);
    public static final Font FONT_CARD_NAME = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_STAT = new Font("Monospaced", Font.BOLD, 11);
    public static final Font FONT_DESC = new Font("SansSerif", Font.PLAIN, 10);
    public static final Font FONT_TAB = new Font("SansSerif", Font.BOLD, 12);

    public static final int SCROLL_SPEED = 60;

    public static final String[] CATEGORIES = {"All", "Force", "Agilité", "Intelligence"};

    private HeroSelectionConstants() {}
}
