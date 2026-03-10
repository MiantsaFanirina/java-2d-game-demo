package Engine.Render;

import Core.Config;
import Core.Moba.Units.Tour;
import Core.Moba.Units.Ancient;
import Core.Moba.World.TeamColor;
import java.awt.*;

public class TowerRenderer {
    public void draw(Graphics2D g2, Tour tour, Camera camera) {
        int tileSize = Config.getTileSize();
        // Use tile coordinates and convert to world pixels
        int x = (int) (tour.position().x() * tileSize);
        int y = (int) (tour.position().y() * tileSize);

        // Tower drawing size (based on cluster dimensions found in map.txt)
        int structureWidth = tour.width() * tileSize;
        int structureHeight = tour.height() * tileSize;

        // Scaling factors from SVG (64x64) to cluster size
        double scaleX = (double)structureWidth / 64.0;
        double scaleY = (double)structureHeight / 64.0;

        // Draw shapes according to SVG
        // 1. Base Structure
        g2.setColor(new Color(0x4a, 0x55, 0x68));
        g2.fillRect(x + (int)(15 * scaleX), y + (int)(44 * scaleY), (int)(34 * scaleX), (int)(12 * scaleY));

        // 2. Main Shaft
        g2.setColor(new Color(0x71, 0x80, 0x96));
        g2.fillRect(x + (int)(20 * scaleX), y + (int)(20 * scaleY), (int)(24 * scaleX), (int)(24 * scaleY));

        // 3. Top Platform
        g2.setColor(new Color(0x2d, 0x37, 0x48));
        g2.fillRect(x + (int)(15 * scaleX), y + (int)(10 * scaleY), (int)(34 * scaleX), (int)(10 * scaleY));

        // 4. Light/Glow based on team
        if (tour.equipe().couleur() == TeamColor.BLUE) {
            g2.setColor(new Color(0x42, 0x99, 0xe1));
        } else {
            g2.setColor(new Color(0xf5, 0x65, 0x65));
        }
        g2.fillRect(x + (int)(22 * scaleX), y + (int)(14 * scaleY), (int)(20 * scaleX), (int)(6 * scaleY));

        // 5. Roof (Triangle)
        int[] rx = {x + (int)(15 * scaleX), x + (int)(32 * scaleX), x + (int)(49 * scaleX)};
        int[] ry = {y + (int)(10 * scaleY), y + (int)(2 * scaleY), y + (int)(10 * scaleY)};
        g2.setColor(new Color(0x2d, 0x37, 0x48));
        g2.fillPolygon(rx, ry, 3);
        
        // Health bar (spanning full cluster width)
        g2.setColor(Color.RED);
        g2.fillRect(x + 5, y - 10, structureWidth - 10, 6);
        g2.setColor(Color.GREEN);
        double healthPct = (double)tour.stats().hp() / tour.stats().maxHp();
        g2.fillRect(x + 5, y - 10, (int)((structureWidth - 10) * healthPct), 6);

        // Tower Name/Label (e.g., "Blue Top T1")
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        String teamName = tour.equipe().couleur() == TeamColor.BLUE ? "Blue" : "Red";
        String label = teamName + " " + tour.lane().name() + " T" + tour.tier();
        
        FontMetrics fm = g2.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        g2.drawString(label, x + (structureWidth - labelWidth) / 2, y - 15);
    }

    public void drawAncient(Graphics2D g2, Ancient ancient, Camera camera) {
        int tileSize = Config.getTileSize();
        int x = (int) (ancient.position().x() * tileSize);
        int y = (int) (ancient.position().y() * tileSize);
        int w = ancient.width() * tileSize;
        int h = ancient.height() * tileSize;

        // Ancient Visuals: Larger and different structure
        // Base Shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillOval(x + 10, y + h - 20, w - 20, 30);

        // Team Colored Glow
        Color glow = (ancient.equipe().couleur() == TeamColor.BLUE) ? new Color(66, 153, 225, 150) : new Color(245, 101, 101, 150);
        g2.setColor(glow);
        g2.fillOval(x + w/4, y + h/4, w/2, h/2);

        // Main Pyramid Structure
        int[] px = {x + w/2, x + w, x + w/2, x};
        int[] py = {y, y + h/2, y + h, y + h/2};
        g2.setColor(new Color(45, 55, 72));
        g2.fillPolygon(px, py, 4);

        // Top Crystal
        int[] cx = {x + w/2, x + 3*w/4, x + w/2, x + w/4};
        int[] cy = {y + h/4, y + h/2, y + 3*h/4, y + h/2};
        g2.setColor(glow.brighter());
        g2.fillPolygon(cx, cy, 4);

        // Health bar
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 15, w, 10);
        g2.setColor(Color.GREEN);
        double healthPct = (double)ancient.stats().hp() / ancient.stats().maxHp();
        g2.fillRect(x, y - 15, (int)(w * healthPct), 10);

        // Label
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String label = (ancient.equipe().couleur() == TeamColor.BLUE ? "Blue" : "Red") + " Ancient";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, x + (w - fm.stringWidth(label)) / 2, y - 25);
    }
}
