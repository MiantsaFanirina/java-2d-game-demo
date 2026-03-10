package Core.Entity;

import Core.Config;
import Core.Input.MoveInput;
import Core.Input.TargetInput;
import Core.Match.PathFinder;
import Core.Moba.Units.Tour;
import Core.Moba.Units.Ancient;
import Core.Moba.World.Arena;
import Core.Tile.CollisionTable;
import Core.Tile.TileMap;

import java.util.List;

public class Player extends Entity {
    
    private final MoveInput moveInput;
    private final TargetInput targetInput;
    private final TileMap tileMap;
    private final CollisionTable collisionTable;
    private final PathFinder pathFinder;
    private final Arena arena;
    
    // Targets are stored as the desired TOP-LEFT pixel position of the player,
    // but are derived from mouse clicks on the desired CENTER position.
    private double targetX = -1;
    private double targetY = -1;
    private boolean hasTarget = false;
    private Direction targetDirection = Direction.DOWN;

    private List<int[]> currentPath = null;
    private int currentPathIndex = 0;
    private double lastPathX = -1;
    private double lastPathY = -1;
    private int stuckCounter = 0;

    // Collision hitbox is slightly inset from the sprite bounds to feel better.
    // Coordinates are in pixels relative to the player's top-left position.
    private static final double HITBOX_INSET_PX = 6.0;
    
    public Player(MoveInput moveInput, TargetInput targetInput, TileMap tileMap, CollisionTable collisionTable, Arena arena) {
        this.moveInput = moveInput;
        this.targetInput = targetInput;
        this.tileMap = tileMap;
        this.collisionTable = collisionTable;
        this.arena = arena;
        this.pathFinder = new PathFinder(tileMap, collisionTable);
        this.pathFinder.setArena(arena);
        setDefaultValues();
    }
    
    private void setDefaultValues() {
        setX(Config.getPlayerDefaultX());
        setY(Config.getPlayerDefaultY());
        setSpeed(Config.getPlayerDefaultSpeed());
        setDirection(Direction.DOWN);
        lastPathX = getX();
        lastPathY = getY();
    }
    
    public void update() {
        boolean isMoving = false;
        
        if (targetInput.hasTarget()) {
            int tileSize = Config.getTileSize();
            double half = tileSize / 2.0;
            double clickedX = targetInput.getTargetX() - half;
            double clickedY = targetInput.getTargetY() - half;

            if (isPathClear(getX(), getY(), clickedX, clickedY)) {
                targetX = clickedX;
                targetY = clickedY;
                hasTarget = true;
                currentPath = null;
            } else {
                int startCol = (int) Math.floor((getX() + half) / tileSize);
                int startRow = (int) Math.floor((getY() + half) / tileSize);
                int targetCol = (int) Math.floor((clickedX + half) / tileSize);
                int targetRow = (int) Math.floor((clickedY + half) / tileSize);

                currentPath = pathFinder.findPath(startCol, startRow, targetCol, targetRow);
                if (currentPath != null && currentPath.size() > 1) {
                    smoothPath();
                    currentPathIndex = 1; // Skip start node
                    setNextPathTarget();
                    hasTarget = true;
                } else {
                    hasTarget = false;
                }
            }
            targetInput.clearTarget();
        }
        
        if (hasTarget) {
            isMoving = moveToTarget();
            checkStuckAndRecalculate();
        } else if (moveInput.isAnyKeyPressed()) {
            isMoving = handleKeyboardMovement();
            currentPath = null;
        }
        
        if (isMoving) {
            updateSpriteAnimation();
        }
    }

    private void smoothPath() {
        if (currentPath == null || currentPath.size() <= 2) return;

        int tileSize = Config.getTileSize();
        int i = 0;
        while (i < currentPath.size() - 2) {
            int[] start = currentPath.get(i);
            int[] nextNext = currentPath.get(i + 2);

            double x1 = start[0] * tileSize;
            double y1 = start[1] * tileSize;
            double x2 = nextNext[0] * tileSize;
            double y2 = nextNext[1] * tileSize;

            if (isPathClear(x1, y1, x2, y2)) {
                currentPath.remove(i + 1);
                // Don't increment i, try to smooth further from same start
            } else {
                i++;
            }
        }
    }

    private void setNextPathTarget() {
        if (currentPath != null && currentPathIndex < currentPath.size()) {
            int[] tile = currentPath.get(currentPathIndex);
            int tileSize = Config.getTileSize();
            targetX = tile[0] * tileSize;
            targetY = tile[1] * tileSize;
        }
    }

    private boolean isPathClear(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < 1) return true;

        // Use a smaller hitbox for path clearing to avoid getting stuck on corners
        // while trying to move in a straight line.
        double originalInset = HITBOX_INSET_PX;
        double pathClearInset = originalInset + 2.0; // More inset = smaller hitbox

        int steps = (int) Math.max(1, distance / (Config.getTileSize() / 8.0));
        for (int i = 1; i <= steps; i++) {
            double ratio = (double) i / steps;
            if (collidesAtCustom(x1 + dx * ratio, y1 + dy * ratio, pathClearInset)) {
                return false;
            }
        }
        return true;
    }

    private boolean collidesAtCustom(double topLeftX, double topLeftY, double inset) {
        int tileSize = Config.getTileSize();
        double left = topLeftX + inset;
        double top = topLeftY + inset;
        double right = topLeftX + tileSize - inset;
        double bottom = topLeftY + tileSize - inset;

        // Check tile collision - use more thorough check for pathfinding
        double centerX = (left + right) / 2;
        double centerY = (top + bottom) / 2;
        
        if (isWallAt(left, top) || isWallAt(right, top)
                || isWallAt(left, bottom) || isWallAt(right, bottom)
                || isWallAt(centerX, centerY)) {
            return true;
        }
        
        // Check tower collision for pathfinding
        if (arena != null) {
            return collidesWithTowersCustom(topLeftX, topLeftY, inset);
        }
        
        return false;
    }
    
    private boolean collidesWithTowersCustom(double topLeftX, double topLeftY, double inset) {
        int tileSize = Config.getTileSize();
        double left = topLeftX + inset;
        double top = topLeftY + inset;
        double right = topLeftX + tileSize - inset;
        double bottom = topLeftY + tileSize - inset;
        
        // Check tower collisions
        for (Tour tower : arena.tours()) {
            double towerPixelX = tower.position().x() * tileSize;
            double towerPixelY = tower.position().y() * tileSize;
            double towerWidth = tower.width() * tileSize;
            double towerHeight = tower.height() * tileSize;
            
            // AABB collision detection
            if (right > towerPixelX && left < towerPixelX + towerWidth
                    && bottom > towerPixelY && top < towerPixelY + towerHeight) {
                return true;
            }
        }
        
        // Check ancient (base) collisions
        for (Ancient ancient : arena.ancients()) {
            double ancientPixelX = ancient.position().x() * tileSize;
            double ancientPixelY = ancient.position().y() * tileSize;
            double ancientWidth = ancient.width() * tileSize;
            double ancientHeight = ancient.height() * tileSize;
            
            // AABB collision detection
            if (right > ancientPixelX && left < ancientPixelX + ancientWidth
                    && bottom > ancientPixelY && top < ancientPixelY + ancientHeight) {
                return true;
            }
        }
        
        return false;
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
            
            boolean isFollowingPath = currentPath != null && currentPathIndex < currentPath.size();
            
            if (!collidesAt(newX, newY)) {
                setX(newX);
                setY(newY);
                return true;
            } else if (isFollowingPath) {
                double currentX = getX();
                double currentY = getY();
                
                if (!collidesAt(newX, currentY)) {
                    setX(newX);
                    return true;
                }
                if (!collidesAt(currentX, newY)) {
                    setY(newY);
                    return true;
                }
                
                if (!collidesAt(newX - getSpeed(), newY)) {
                    setX(newX - getSpeed());
                    return true;
                }
                if (!collidesAt(newX + getSpeed(), newY)) {
                    setX(newX + getSpeed());
                    return true;
                }
                if (!collidesAt(newX, newY - getSpeed())) {
                    setY(newY - getSpeed());
                    return true;
                }
                if (!collidesAt(newX, newY + getSpeed())) {
                    setY(newY + getSpeed());
                    return true;
                }
                
                currentPathIndex++;
                if (currentPathIndex < currentPath.size()) {
                    setNextPathTarget();
                    return true;
                }
                
                stuckCounter = 0;
                return false;
            } else {
                hasTarget = false;
                targetX = -1;
                targetY = -1;
                currentPath = null;
                stuckCounter = 0;
                return false;
            }
        } else {
            // Reached current target node
            if (currentPath != null) {
                currentPathIndex++;
                if (currentPathIndex < currentPath.size()) {
                    setNextPathTarget();
                    stuckCounter = 0;
                    return true;
                }
            }

            // Final target reached
            if (!collidesAt(targetX, targetY)) {
                setX(targetX);
                setY(targetY);
            }
            hasTarget = false;
            targetX = -1;
            targetY = -1;
            currentPath = null;
            stuckCounter = 0;
            return false;
        }
    }
    
    private void checkStuckAndRecalculate() {
        if (currentPath == null || !hasTarget) {
            stuckCounter = 0;
            return;
        }
        
        double dx = targetX - getX();
        double dy = targetY - getY();
        double distToTarget = Math.sqrt(dx * dx + dy * dy);
        
        if (distToTarget < 1) {
            stuckCounter = 0;
            return;
        }
        
        if (Math.abs(lastPathX - getX()) < 1 && Math.abs(lastPathY - getY()) < 1) {
            stuckCounter++;
            if (stuckCounter > 30) {
                int tileSize = Config.getTileSize();
                double half = tileSize / 2.0;
                int startCol = (int) Math.floor((getX() + half) / tileSize);
                int startRow = (int) Math.floor((getY() + half) / tileSize);
                int targetCol = (int) Math.floor((targetX + half) / tileSize);
                int targetRow = (int) Math.floor((targetY + half) / tileSize);
                
                List<int[]> newPath = pathFinder.findPath(startCol, startRow, targetCol, targetRow);
                if (newPath != null && newPath.size() > 1) {
                    currentPath = newPath;
                    smoothPath();
                    currentPathIndex = 1;
                    setNextPathTarget();
                    stuckCounter = 0;
                }
            }
        } else {
            stuckCounter = 0;
        }
        
        lastPathX = getX();
        lastPathY = getY();
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
        // Check tile collision
        if (collidesWithTile(topLeftX, topLeftY)) {
            return true;
        }
        
        // Check tower collision
        if (arena != null) {
            return collidesWithTowers(topLeftX, topLeftY);
        }
        
        return false;
    }
    
    private boolean collidesWithTowers(double topLeftX, double topLeftY) {
        int tileSize = Config.getTileSize();
        double left = topLeftX + HITBOX_INSET_PX;
        double top = topLeftY + HITBOX_INSET_PX;
        double right = topLeftX + tileSize - HITBOX_INSET_PX;
        double bottom = topLeftY + tileSize - HITBOX_INSET_PX;
        
        // Check tower collisions
        for (Tour tower : arena.tours()) {
            double towerPixelX = tower.position().x() * tileSize;
            double towerPixelY = tower.position().y() * tileSize;
            double towerWidth = tower.width() * tileSize;
            double towerHeight = tower.height() * tileSize;
            
            // AABB collision detection
            if (right > towerPixelX && left < towerPixelX + towerWidth
                    && bottom > towerPixelY && top < towerPixelY + towerHeight) {
                return true;
            }
        }
        
        // Check ancient (base) collisions
        for (Ancient ancient : arena.ancients()) {
            double ancientPixelX = ancient.position().x() * tileSize;
            double ancientPixelY = ancient.position().y() * tileSize;
            double ancientWidth = ancient.width() * tileSize;
            double ancientHeight = ancient.height() * tileSize;
            
            // AABB collision detection
            if (right > ancientPixelX && left < ancientPixelX + ancientWidth
                    && bottom > ancientPixelY && top < ancientPixelY + ancientHeight) {
                return true;
            }
        }
        
        return false;
    }

    private boolean isWallAt(double pixelX, double pixelY) {
        int tileSize = Config.getTileSize();
        int col = (int) Math.floor(pixelX / tileSize);
        int row = (int) Math.floor(pixelY / tileSize);
        int tileId = tileMap.getTileAt(row, col);
        return collisionTable.hasCollision(tileId);
    }
    
    private boolean collidesWithTile(double topLeftX, double topLeftY) {
        int tileSize = Config.getTileSize();
        double left = topLeftX + HITBOX_INSET_PX;
        double top = topLeftY + HITBOX_INSET_PX;
        double right = topLeftX + tileSize - HITBOX_INSET_PX;
        double bottom = topLeftY + tileSize - HITBOX_INSET_PX;

        if (isWallAt(left, top)
                || isWallAt(right, top)
                || isWallAt(left, bottom)
                || isWallAt(right, bottom)) {
            return true;
        }
        
        double centerX = (left + right) / 2;
        double centerY = (top + bottom) / 2;
        if (isWallAt(centerX, centerY)) {
            return true;
        }
        
        double quarterX = left + (right - left) / 4;
        double quarterY = top + (bottom - top) / 4;
        double threeQuarterX = left + 3 * (right - left) / 4;
        double threeQuarterY = top + 3 * (bottom - top) / 4;
        
        if (isWallAt(quarterX, quarterY) || isWallAt(threeQuarterX, quarterY)
                || isWallAt(quarterX, threeQuarterY) || isWallAt(threeQuarterX, threeQuarterY)) {
            return true;
        }
        
        return false;
    }
}
