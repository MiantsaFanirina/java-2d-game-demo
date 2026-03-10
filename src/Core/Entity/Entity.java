package Core.Entity;

import Core.Config;

public class Entity {
    
    private double x;
    private double y;
    private int speed;
    private Direction direction;
    
    private int spriteCounter = 0;
    private int spriteNum = 1;
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    public int getSpriteCounter() {
        return spriteCounter;
    }
    
    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }
    
    public int getSpriteNum() {
        return spriteNum;
    }
    
    public void setSpriteNum(int spriteNum) {
        this.spriteNum = spriteNum;
    }
    
    public void updateSpriteAnimation() {
        spriteCounter++;
        if (spriteCounter > Config.getSpriteAnimationSpeed()) {
            spriteNum = spriteNum == 1 ? 2 : 1;
            spriteCounter = 0;
        }
    }
    
    public void moveUp() {
        y -= speed;
        direction = Direction.UP;
    }
    
    public void moveDown() {
        y += speed;
        direction = Direction.DOWN;
    }
    
    public void moveLeft() {
        x -= speed;
        direction = Direction.LEFT;
    }
    
    public void moveRight() {
        x += speed;
        direction = Direction.RIGHT;
    }
}
