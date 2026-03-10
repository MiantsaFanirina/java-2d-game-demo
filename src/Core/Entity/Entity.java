package Core.Entity;

import Core.Config;

import java.awt.image.BufferedImage;

public class Entity {
    
    private double x;
    private double y;
    private int speed;
    private Direction direction;
    
    private BufferedImage up1, up2;
    private BufferedImage down1, down2;
    private BufferedImage left1, left2;
    private BufferedImage right1, right2;
    
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
    
    public BufferedImage getUp1() {
        return up1;
    }
    
    public void setUp1(BufferedImage up1) {
        this.up1 = up1;
    }
    
    public BufferedImage getUp2() {
        return up2;
    }
    
    public void setUp2(BufferedImage up2) {
        this.up2 = up2;
    }
    
    public BufferedImage getDown1() {
        return down1;
    }
    
    public void setDown1(BufferedImage down1) {
        this.down1 = down1;
    }
    
    public BufferedImage getDown2() {
        return down2;
    }
    
    public void setDown2(BufferedImage down2) {
        this.down2 = down2;
    }
    
    public BufferedImage getLeft1() {
        return left1;
    }
    
    public void setLeft1(BufferedImage left1) {
        this.left1 = left1;
    }
    
    public BufferedImage getLeft2() {
        return left2;
    }
    
    public void setLeft2(BufferedImage left2) {
        this.left2 = left2;
    }
    
    public BufferedImage getRight1() {
        return right1;
    }
    
    public void setRight1(BufferedImage right1) {
        this.right1 = right1;
    }
    
    public BufferedImage getRight2() {
        return right2;
    }
    
    public void setRight2(BufferedImage right2) {
        this.right2 = right2;
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
    
    public BufferedImage getCurrentSprite() {
        return switch (direction) {
            case UP -> spriteNum == 1 ? up1 : up2;
            case DOWN -> spriteNum == 1 ? down1 : down2;
            case LEFT -> spriteNum == 1 ? left1 : left2;
            case RIGHT -> spriteNum == 1 ? right1 : right2;
        };
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
