package Engine.Tile;

import java.awt.image.BufferedImage;

public class Tile {

    private final int id;
    private final String name;
    private final BufferedImage image;
    private final boolean collision;

    public Tile(int id, String name, BufferedImage image, boolean collision) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.collision = collision;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isCollision() {
        return collision;
    }
}

