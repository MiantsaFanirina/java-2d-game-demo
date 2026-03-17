package Engine.Render.Utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for loading images with consistent error handling.
 * Centralizes image loading logic to avoid duplication across sprite caches.
 */
public class ImageLoader {
    
    /**
     * Loads an image from the specified file path.
     * 
     * @param path Path to the image file
     * @return BufferedImage if successful, null if loading fails
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Failed to load image: " + path + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Loads an image from an input stream.
     * 
     * @param stream Input stream containing image data
     * @return BufferedImage if successful, null if loading fails
     */
    public static BufferedImage loadImage(InputStream stream) {
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            System.err.println("Failed to load image from stream: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Loads an image with a fallback default image if loading fails.
     * 
     * @param path Path to the image file
     * @param defaultImage Image to return if loading fails
     * @return BufferedImage if successful, defaultImage if loading fails
     */
    public static BufferedImage loadImageOrDefault(String path, BufferedImage defaultImage) {
        BufferedImage loaded = loadImage(path);
        return loaded != null ? loaded : defaultImage;
    }
    
    /**
     * Creates a simple colored square as a fallback sprite.
     * 
     * @param size Width and height of the square
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @param a Alpha component (0-255)
     * @return BufferedImage representing the colored square
     */
    public static BufferedImage createColoredSquare(int size, int r, int g, int b, int a) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2 = img.createGraphics();
        g2.setColor(new java.awt.Color(r, g, b, a));
        g2.fillRect(0, 0, size, size);
        g2.dispose();
        return img;
    }
    
    /**
     * Creates a fallback sprite for missing images.
     * 
     * @param size Size of the sprite
     * @return BufferedImage with a warning pattern
     */
    public static BufferedImage createFallbackSprite(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2 = img.createGraphics();
        // Draw a warning pattern (yellow/black stripes)
        g2.setColor(java.awt.Color.YELLOW);
        g2.fillRect(0, 0, size, size);
        g2.setColor(java.awt.Color.BLACK);
        for (int i = 0; i < size; i += 4) {
            g2.drawLine(i, 0, i, size);
        }
        g2.dispose();
        return img;
    }
    
    // Private constructor to prevent instantiation
    private ImageLoader() {}
}