package Core.Entity;

import Core.Config;
import Core.Input.MoveInput;
import Core.Input.TargetInput;
import Core.Tile.CollisionTable;
import Core.Tile.TileMap;

public class Player extends Entity {
    
    private final MoveInput moveInput;
    private final TargetInput targetInput;
    private final TileMap tileMap;
    private final CollisionTable collisionTable;
    
    // Targets are stored as the desired TOP-LEFT pixel position of the player,
    // but are derived from mouse clicks on the desired CENTER position.
    private double targetX = -1;
    private double targetY = -1;
    private boolean hasTarget = false;
    private Direction targetDirection = Direction.DOWN;

    // Collision hitbox is slightly inset from the sprite bounds to feel better.
    // Coordinates are in pixels relative to the player's top-left position.
    private static final double HITBOX_INSET_PX = 6.0;
    
    public Player(MoveInput moveInput, TargetInput targetInput, TileMap tileMap, CollisionTable collisionTable) {
        this.moveInput = moveInput;
        this.targetInput = targetInput;
        this.tileMap = tileMap;
        this.collisionTable = collisionTable;
        setDefaultValues();
    }
    
    private void setDefaultValues() {
        setX(Config.getPlayerDefaultX());
        setY(Config.getPlayerDefaultY());
        setSpeed(Config.getPlayerDefaultSpeed());
        setDirection(Direction.DOWN);
    }
    
    public void update() {
        boolean isMoving = false;
        
        if (targetInput.hasTarget()) {
            int tileSize = Config.getTileSize();
            double half = tileSize / 2.0;
            // Convert a click (center target) into the player's top-left target.
            targetX = targetInput.getTargetX() - half;
            targetY = targetInput.getTargetY() - half;
            hasTarget = true;
            targetInput.clearTarget();
        }
        
        if (hasTarget) {
            isMoving = moveToTarget();
        } else if (moveInput.isAnyKeyPressed()) {
            isMoving = handleKeyboardMovement();
        }
        
        if (isMoving) {
            updateSpriteAnimation();
        }
    }
    
    private boolean moveToTarget() {
        double dx = targetX - getX();
        double dy = targetY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > getSpeed()) {
            double ratioX = dx / distance;
            double ratioY = dy / distance;
            
            targetDirection = Direction.fromDelta((int) Math.round(dx), (int) Math.round(dy));
            setDirection(targetDirection);
            
            double newX = getX() + ratioX * getSpeed();
            double newY = getY() + ratioY * getSpeed();
            
            if (!collidesAt(newX, newY)) {
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
            // Snap to target only if it's not inside a wall.
            if (!collidesAt(targetX, targetY)) {
                setX(targetX);
                setY(targetY);
            }
            hasTarget = false;
            targetX = -1;
            targetY = -1;
            return false;
        }
    }
    
    private boolean handleKeyboardMovement() {
        int xAxis = 0;
        int yAxis = 0;

        if (moveInput.isLeftPressed()) xAxis -= 1;
        if (moveInput.isRightPressed()) xAxis += 1;
        if (moveInput.isUpPressed()) yAxis -= 1;
        if (moveInput.isDownPressed()) yAxis += 1;

        if (xAxis == 0 && yAxis == 0) return false;

        // Choose facing direction for animation.
        if (xAxis != 0 && yAxis != 0) {
            // Diagonal: face the dominant axis (stable).
            if (Math.abs(xAxis) >= Math.abs(yAxis)) {
                setDirection(xAxis > 0 ? Direction.RIGHT : Direction.LEFT);
            } else {
                setDirection(yAxis > 0 ? Direction.DOWN : Direction.UP);
            }
        } else if (xAxis != 0) {
            setDirection(xAxis > 0 ? Direction.RIGHT : Direction.LEFT);
        } else {
            setDirection(yAxis > 0 ? Direction.DOWN : Direction.UP);
        }

        // Normalize so diagonals aren't faster than straight movement.
        double len = Math.sqrt(xAxis * xAxis + yAxis * yAxis);
        double stepX = (xAxis / len) * getSpeed();
        double stepY = (yAxis / len) * getSpeed();

        double currentX = getX();
        double currentY = getY();
        double nextX = currentX + stepX;
        double nextY = currentY + stepY;

        // Try full move; if blocked, try sliding along each axis.
        if (!collidesAt(nextX, nextY)) {
            setX(nextX);
            setY(nextY);
            return true;
        }
        if (!collidesAt(nextX, currentY)) {
            setX(nextX);
            return true;
        }
        if (!collidesAt(currentX, nextY)) {
            setY(nextY);
            return true;
        }

        return false;
    }
    
    private boolean collidesAt(double topLeftX, double topLeftY) {
        int tileSize = Config.getTileSize();

        double left = topLeftX + HITBOX_INSET_PX;
        double top = topLeftY + HITBOX_INSET_PX;
        double right = topLeftX + tileSize - HITBOX_INSET_PX;
        double bottom = topLeftY + tileSize - HITBOX_INSET_PX;

        return isWallAt(left, top)
                || isWallAt(right, top)
                || isWallAt(left, bottom)
                || isWallAt(right, bottom);
    }

    private boolean isWallAt(double pixelX, double pixelY) {
        int tileSize = Config.getTileSize();
        int col = (int) Math.floor(pixelX / tileSize);
        int row = (int) Math.floor(pixelY / tileSize);
        int tileId = tileMap.getTileAt(row, col);
        return collisionTable.hasCollision(tileId);
    }
}
