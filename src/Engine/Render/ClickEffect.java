package Engine.Render;

import java.awt.*;

public class ClickEffect {
    private final int worldX;
    private final int worldY;
    private int lifeSpan = 25; // Slightly longer
    private int currentLife = 0;
    private final Color primaryColor = new Color(50, 255, 50); // Brighter green
    private final float[][] particles;

    public ClickEffect(int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        
        // Initialize particles: [angle, distance, speed]
        int particleCount = 6;
        particles = new float[particleCount][3];
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < particleCount; i++) {
            particles[i][0] = (float) (i * (2 * Math.PI / particleCount)); // Angle
            particles[i][1] = 0; // Current distance
            particles[i][2] = 1.5f + rand.nextFloat() * 2.0f; // Speed
        }
    }

    public void update() {
        currentLife++;
        for (int i = 0; i < particles.length; i++) {
            particles[i][1] += particles[i][2]; // Move particles outward
        }
    }

    public boolean isDead() {
        return currentLife >= lifeSpan;
    }

    public void draw(Graphics2D g2) {
        float progress = (float) currentLife / lifeSpan;
        int alpha = (int) (200 * (1.0f - progress));
        if (alpha < 0) alpha = 0;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Primary Expanding Ring
        int size1 = (int) (5 + progress * 40);
        g2.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), alpha));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawOval(worldX - size1 / 2, worldY - size1 / 2, size1, size1);

        // 2. Secondary Faster Fading Ring
        if (progress < 0.5f) {
            float innerProgress = progress * 2.0f;
            int size2 = (int) (5 + innerProgress * 30);
            int alpha2 = (int) (150 * (1.0f - innerProgress));
            g2.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), alpha2));
            g2.drawOval(worldX - size2 / 2, worldY - size2 / 2, size2, size2);
        }

        // 3. Particle Burst
        g2.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < particles.length; i++) {
            float angle = particles[i][0];
            float dist = particles[i][1];
            
            int px = (int) (worldX + Math.cos(angle) * dist);
            int py = (int) (worldY + Math.sin(angle) * dist);
            
            // Draw small lines for particles
            int lineLen = 4;
            int px2 = (int) (worldX + Math.cos(angle) * (dist + lineLen));
            int py2 = (int) (worldY + Math.sin(angle) * (dist + lineLen));
            
            g2.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), alpha));
            g2.drawLine(px, py, px2, py2);
        }
    }
}
