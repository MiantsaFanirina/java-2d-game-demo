package Engine.Render;

import Core.Config;
import Core.Entity.Player;
import Core.Moba.Combat.Stats;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayerRenderer {
    private final PlayerSprites sprites;

    public PlayerRenderer(PlayerSprites sprites) {
        this.sprites = sprites;
    }

    public void draw(Graphics2D g2, Player player) {
        BufferedImage image = sprites.get(player.getDirection(), player.getSpriteNum());
        int tileSize = Config.getTileSize();
        g2.drawImage(image, (int) player.getX(), (int) player.getY(), tileSize, tileSize, null);
        
        drawLevel(g2, player, tileSize);
        drawHealthManaBars(g2, player, tileSize);
    }
    
    private void drawLevel(Graphics2D g2, Player player, int tileSize) {
        int x = (int) player.getX() + 2;
        int y = (int) player.getY() - 20;
        g2.setColor(java.awt.Color.YELLOW);
        g2.drawString("Lv " + player.level(), x, y);
    }

    private void drawHealthManaBars(Graphics2D g2, Player player, int tileSize) {
        int barWidth = tileSize - 4;
        int barHeight = 4;
        int x = (int) player.getX() + 2;
        int y = (int) player.getY() - 10;
        
        Stats stats = player.stats();
        if (stats != null) {
            // Health bar background: light grey
            g2.setColor(java.awt.Color.LIGHT_GRAY);
            g2.fillRect(x, y, barWidth, barHeight);
            
            double hpPercent = (double) stats.hp() / stats.maxHp();
            if (hpPercent > 0) {
                // Health fill: green
                g2.setColor(java.awt.Color.GREEN);
                g2.fillRect(x, y, (int) (barWidth * hpPercent), barHeight);
            }
            
            if (stats.maxMana() > 0) {
                int manaY = y + barHeight + 2;
                // Mana bar background: light grey
                g2.setColor(java.awt.Color.LIGHT_GRAY);
                g2.fillRect(x, manaY, barWidth, barHeight);
                
                double manaPercent = (double) stats.mana() / stats.maxMana();
                if (manaPercent > 0) {
                    // Mana fill: blue
                    g2.setColor(new java.awt.Color(100, 100, 255));
                    g2.fillRect(x, manaY, (int) (barWidth * manaPercent), barHeight);
                }
            }
        }
    }
}

