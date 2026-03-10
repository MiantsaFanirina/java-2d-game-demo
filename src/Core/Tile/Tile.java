package Core.Tile;

import java.awt.image.BufferedImage;

public class Tile {
    
    private int id;
    private String name;
    private BufferedImage image;
    private boolean collision = false;
    
    public Tile() {}
    
    public Tile(int id, String name, BufferedImage image, boolean collision) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.collision = collision;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public boolean isCollision() {
        return collision;
    }
    
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
