package Engine.Render.Cache;

import game.shared.infrastructure.ImageLoader;
import java.awt.image.BufferedImage;

public class ProjectileSpriteCache {

    private final BufferedImage[] fireballFrames;
    private final BufferedImage explosionSprite;

    public ProjectileSpriteCache() {
        this.fireballFrames = loadFireballFrames();
        this.explosionSprite = loadExplosionSprite();
    }

    private BufferedImage[] loadFireballFrames() {
        BufferedImage[] frames = new BufferedImage[3];
        String[] frameNames = {"fireball_1.png", "fireball_2.png", "fireball_3.png"};
        
        for (int i = 0; i < 3; i++) {
            String path = "src/Resource/Projectile/" + frameNames[i];
            BufferedImage loaded = ImageLoader.loadImage(path);
            frames[i] = loaded != null ? loaded : createFallbackSprite(i);
        }
        return frames;
    }

    private BufferedImage loadExplosionSprite() {
        String explosionPath = "src/Resource/Projectile/explosion.png";
        BufferedImage loaded = ImageLoader.loadImage(explosionPath);
        return loaded != null ? loaded : createExplosionFallback();
    }

    private BufferedImage createFallbackSprite(int frame) {
        BufferedImage img = new BufferedImage(12, 12, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(new java.awt.Color(255, 100, 0));
        g.fillRect(3, 3, 6, 6);
        g.setColor(new java.awt.Color(255, 200, 0));
        g.fillRect(5, 5, 2, 2);
        g.dispose();
        return img;
    }

    private BufferedImage createExplosionFallback() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(new java.awt.Color(255, 100, 0, 200));
        g.fillOval(8, 8, 48, 48);
        g.setColor(new java.awt.Color(255, 200, 0, 150));
        g.fillOval(16, 16, 32, 32);
        g.setColor(new java.awt.Color(255, 255, 100, 100));
        g.fillOval(24, 24, 16, 16);
        g.dispose();
        return img;
    }

    public BufferedImage getFireballFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < fireballFrames.length) {
            return fireballFrames[frameIndex];
        }
        return fireballFrames[0];
    }

    public BufferedImage getExplosionSprite() {
        return explosionSprite;
    }
}
