package Core.Entity;

import Core.Config;
import Core.Tile.TileManager;
import Engine.Input.KeyHandler;
import Engine.Input.MouseHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;
    private final TileManager tileManager;
    
    private int targetX = -1;
    private int targetY = -1;
    private boolean hasTarget = false;
    private Direction targetDirection = Direction.DOWN;
    
    public Player(KeyHandler keyHandler, MouseHandler mouseHandler, TileManager tileManager) {
        this.keyHandler = keyHandler;
        this.mouseHandler = mouseHandler;
        this.tileManager = tileManager;
        setDefaultValues();
        loadPlayerSprites();
    }
    
    private void setDefaultValues() {
        setX(Config.getPlayerDefaultX());
        setY(Config.getPlayerDefaultY());
        setSpeed(Config.getPlayerDefaultSpeed());
        setDirection(Direction.DOWN);
    }
    
    private void loadPlayerSprites() {
        try {
            String path = Config.getPlayerImagePath();
            setUp1(ImageIO.read(new java.io.File(path + "boy_up_1.png")));
            setUp2(ImageIO.read(new java.io.File(path + "boy_up_2.png")));
            setDown1(ImageIO.read(new java.io.File(path + "boy_down_1.png")));
            setDown2(ImageIO.read(new java.io.File(path + "boy_down_2.png")));
            setLeft1(ImageIO.read(new java.io.File(path + "boy_left_1.png")));
            setLeft2(ImageIO.read(new java.io.File(path + "boy_left_2.png")));
            setRight1(ImageIO.read(new java.io.File(path + "boy_right_1.png")));
            setRight2(ImageIO.read(new java.io.File(path + "boy_right_2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void update() {
        boolean isMoving = false;
        
        if (mouseHandler.hasTarget()) {
            targetX = mouseHandler.getTargetX();
            targetY = mouseHandler.getTargetY();
            hasTarget = true;
            mouseHandler.clearTarget();
        }
        
        if (hasTarget) {
            isMoving = moveToTarget();
        } else if (keyHandler.isAnyKeyPressed()) {
            isMoving = handleKeyboardMovement();
        }
        
        if (isMoving) {
            updateSpriteAnimation();
        }
    }
    
    private boolean moveToTarget() {
        int dx = targetX - (int) getX();
        int dy = targetY - (int) getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > getSpeed()) {
            double ratioX = (double) dx / distance;
            double ratioY = (double) dy / distance;
            
            targetDirection = Direction.fromDelta(dx, dy);
            setDirection(targetDirection);
            
            int newX = (int) (getX() + ratioX * getSpeed());
            int newY = (int) (getY() + ratioY * getSpeed());
            
            if (!tileManager.hasCollision(getTileAt(newX, newY))) {
                setX(newX);
                setY(newY);
                return true;
            } else {
                hasTarget = false;
                targetX = -1;
                targetY = -1;
                return false;
            }
        } else {
            setX(targetX);
            setY(targetY);
            hasTarget = false;
            targetX = -1;
            targetY = -1;
            return false;
        }
    }
    
    private boolean handleKeyboardMovement() {
        if (keyHandler.isUpPressed()) {
            tryMoveUp();
            return true;
        }
        if (keyHandler.isDownPressed()) {
            tryMoveDown();
            return true;
        }
        if (keyHandler.isLeftPressed()) {
            tryMoveLeft();
            return true;
        }
        if (keyHandler.isRightPressed()) {
            tryMoveRight();
            return true;
        }
        return false;
    }
    
    private void tryMoveUp() {
        int newY = (int) getY() - getSpeed();
        if (!tileManager.hasCollision(getTileAt((int) getX(), newY))) {
            moveUp();
        }
    }
    
    private void tryMoveDown() {
        int newY = (int) getY() + getSpeed();
        if (!tileManager.hasCollision(getTileAt((int) getX(), newY))) {
            moveDown();
        }
    }
    
    private void tryMoveLeft() {
        int newX = (int) getX() - getSpeed();
        if (!tileManager.hasCollision(getTileAt(newX, (int) getY()))) {
            moveLeft();
        }
    }
    
    private void tryMoveRight() {
        int newX = (int) getX() + getSpeed();
        if (!tileManager.hasCollision(getTileAt(newX, (int) getY()))) {
            moveRight();
        }
    }
    
    private int getTileAt(int x, int y) {
        int col = x / Config.getTileSize();
        int row = y / Config.getTileSize();
        return tileManager.getTileAt(row, col);
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage image = getCurrentSprite();
        g2.drawImage(image, (int) getX(), (int) getY(), Config.getTileSize(), Config.getTileSize(), null);
    }
}
