package Engine.UI.HeroSelection;

import Core.Database.model.Hero;
import Core.Entity.Direction;
import Engine.Render.Cache.HeroSpriteCache;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * UI for hero selection: handles rendering and layout.
 */
public class HeroSelectionUI extends JPanel {
    
    // Colors
    private final Color BACKGROUND = new Color(25, 25, 40);
    private final Color HEADER_BG = new Color(35, 35, 55, 230);
    private final Color CARD_BG = new Color(50, 50, 75);
    private final Color CARD_HOVER = new Color(70, 70, 100);
    private final Color CARD_SELECTED = new Color(90, 130, 200);
    private final Color CARD_BORDER = new Color(100, 100, 140);
    private final Color SELECTED_BORDER = new Color(255, 215, 0);
    private final Color TEXT_MAIN = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color STAT_POSITIVE = new Color(120, 230, 120);
    private final Color TAB_BG = new Color(45, 45, 65);
    private final Color TAB_SELECTED = new Color(60, 90, 140);
    private final Color TAB_HOVER = new Color(55, 55, 80);
    private final Color SCROLLBAR_BG = new Color(80, 80, 110, 200);
    private final Color SCROLLBAR_THUMB = new Color(150, 150, 190);
    
    // Layout constants
    private final int HEADER_HEIGHT = 140;
    private final int FOOTER_HEIGHT = 100;
    private final int VERTICAL_PADDING = 20;
    private final int HORIZONTAL_PADDING = 30;
    private final int TAB_HEIGHT = 30;
    private final int TAB_WIDTH = 120;
    private final int TAB_SPACING = 8;
    private final int TAB_Y_OFFSET = 60;
    private final int SPRITE_SIZE = 56;
    
    // Fonts
    private final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 28);
    private final Font FONT_CARD_NAME = new Font("SansSerif", Font.BOLD, 13);
    private final Font FONT_STAT = new Font("Monospaced", Font.BOLD, 11);
    private final Font FONT_DESC = new Font("SansSerif", Font.PLAIN, 10);
    private final Font FONT_TAB = new Font("SansSerif", Font.BOLD, 12);
    
    // Layout bounds (computed)
    private Rectangle headerBounds;
    private Rectangle contentBounds;
    private Rectangle footerBounds;
    private int cardWidth = 200;
    private int cardHeight = 170;
    private int cardSpacing = 20;
    
    // Scrolling
    private int scrollY = 0;
    private final int SCROLL_SPEED = 60;
    
    // Hover state
    private int hoveredHeroIndex = -1;
    private boolean hoveredBackButton = false;
    private boolean hoveredStartButton = false;
    private boolean hoveredTab = false;
    private String hoveredTabName = null;
    
    private final HeroSelectionModel model;
    private final HeroSpriteCache spriteCache;
    
    public HeroSelectionUI(HeroSelectionModel model, Dimension screenSize) {
        this.model = model;
        this.spriteCache = new HeroSpriteCache();
        setPreferredSize(screenSize);
        setBackground(BACKGROUND);
        setFocusable(true);
        setOpaque(true);
        setVisible(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        calculateLayoutBounds();
        
        drawBackground(g2);
        drawHeader(g2);
        drawCategoryTabs(g2);
        drawHeroGrid(g2);
        drawFooter(g2);
    }
    
    private void drawBackground(Graphics2D g2) {
        g2.setColor(BACKGROUND);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
    
    private void drawHeader(Graphics2D g2) {
        // Implementation similar to existing HeroSelectionPanel
        // ... (to be implemented)
    }
    
    private void drawCategoryTabs(Graphics2D g2) {
        // ... (to be implemented)
    }
    
    private void drawHeroGrid(Graphics2D g2) {
        // ... (to be implemented)
    }
    
    private void drawFooter(Graphics2D g2) {
        // ... (to be implemented)
    }
    
    public void calculateLayoutBounds() {
        headerBounds = new Rectangle(0, 0, getWidth(), HEADER_HEIGHT);
        contentBounds = new Rectangle(
            HORIZONTAL_PADDING,
            HEADER_HEIGHT,
            getWidth() - HORIZONTAL_PADDING * 2,
            getHeight() - HEADER_HEIGHT - FOOTER_HEIGHT
        );
        footerBounds = new Rectangle(
            0,
            getHeight() - FOOTER_HEIGHT,
            getWidth(),
            FOOTER_HEIGHT
        );
    }
    
    public int calculateColumns() {
        int availableWidth = contentBounds.width;
        return Math.max(1, (availableWidth + cardSpacing) / (cardWidth + cardSpacing));
    }
    
    public void scroll(int rotation) {
        if (contentBounds == null) return;
        int maxScroll = calculateMaxScroll();
        scrollY += rotation * SCROLL_SPEED;
        if (scrollY < 0) scrollY = 0;
        if (maxScroll > 0 && scrollY > maxScroll) scrollY = maxScroll;
    }
    
    private int calculateMaxScroll() {
        int totalHeight = (int) Math.ceil((double) model.getFilteredHeroCount() / calculateColumns()) * (cardHeight + cardSpacing);
        return Math.max(0, totalHeight - contentBounds.height);
    }
    
    public void ensureVisible(int index) {
        // ... (to be implemented)
    }
    
    public void updateHoverState(int x, int y) {
        // ... (to be implemented)
    }
    
    public boolean isOverTab(int x, int y) {
        return headerBounds != null && headerBounds.contains(x, y);
    }
    
    public String getCategoryAt(int x, int y) {
        // ... (to be implemented)
        return null;
    }
    
    public int getHeroIndexAt(int x, int y) {
        // ... (to be implemented)
        return -1;
    }
    
    public Rectangle getBackButtonBounds() {
        // ... (to be implemented)
        return null;
    }
    
    public Rectangle getStartButtonBounds() {
        // ... (to be implemented)
        return null;
    }
}