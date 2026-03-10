package Engine.Render;

import Core.Config;
import Core.Entity.Player;

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
    }
}

