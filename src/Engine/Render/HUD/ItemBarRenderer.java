package Engine.Render.HUD;

import Core.Entity.Player;

import java.awt.*;

public class ItemBarRenderer {
    private final Player player;
    private final int x, y, width, height;

    public ItemBarRenderer(Player player, int x, int y, int width, int height) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(Graphics2D g2) {
        int padding = 4;
        g2.setColor(new Color(20, 20, 30, 200));
        g2.fillRoundRect(x, y, width, height, 6, 6);

        g2.setColor(new Color(180, 180, 200));
        g2.setFont(new Font("Arial", Font.BOLD, 9));
        g2.drawString("ITEMS", x + padding + 4, y + padding + 8);

        int slotSize = 28;
        int slotCount = 6;
        int spacing = 3;
        int startX = x + padding + 4;
        int startY = y + padding + 18;

        for (int i = 0; i < slotCount; i++) {
            int slotX = startX + i * (slotSize + spacing);
            
            g2.setColor(new Color(50, 50, 70));
            g2.fillRect(slotX, startY, slotSize, slotSize);
            g2.setColor(new Color(100, 100, 120));
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(slotX, startY, slotSize, slotSize);

            g2.setColor(new Color(120, 120, 140));
            g2.setFont(new Font("Arial", Font.PLAIN, 7));
            String num = (i + 1) + "";
            g2.drawString(num, slotX + 2, startY + 8);
        }
    }
}