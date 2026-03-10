package Core.Match;

import Core.Tile.CollisionTable;
import Core.Tile.TileMap;
import Core.Moba.Units.Tour;
import Core.Moba.Units.Ancient;
import Core.Moba.World.Arena;
import Core.Config;
import java.util.*;

public class PathFinder {
    
    private static class Node implements Comparable<Node> {
        int x, y;
        int g, h, f;
        Node parent;
        
        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    
    private final TileMap tileMap;
    private final CollisionTable collisionTable;
    private Arena arena;
    
    public PathFinder(TileMap tileMap, CollisionTable collisionTable) {
        this.tileMap = tileMap;
        this.collisionTable = collisionTable;
    }
    
    public void setArena(Arena arena) {
        this.arena = arena;
    }
    
    public List<int[]> findPath(int startCol, int startRow, int targetCol, int targetRow) {
        if (targetCol < 0 || targetCol >= tileMap.getColumns() || targetRow < 0 || targetRow >= tileMap.getRows()) {
            return null;
        }

        // If target is blocked, find the nearest walkable tile
        if (hasCollision(targetRow, targetCol)) {
            int[] nearest = findNearestWalkableTile(targetCol, targetRow);
            if (nearest != null) {
                targetCol = nearest[0];
                targetRow = nearest[1];
            } else {
                return null; // No walkable tiles nearby
            }
        }
        
        // Don't pathfind if we are already at the (possibly adjusted) target
        if (startCol == targetCol && startRow == targetRow) {
            return null;
        }

        PriorityQueue<Node> openList = new PriorityQueue<>();
        Map<Node, Node> openMap = new HashMap<>();
        Set<Node> closedList = new HashSet<>();
        
        Node startNode = new Node(startCol, startRow);
        Node targetNode = new Node(targetCol, targetRow);
        
        startNode.g = 0;
        startNode.h = calculateHeuristic(startCol, startRow, targetCol, targetRow);
        startNode.f = startNode.h;
        
        openList.add(startNode);
        openMap.put(startNode, startNode);
        
        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            openMap.remove(currentNode);
            
            if (currentNode.equals(targetNode)) {
                return reconstructPath(currentNode);
            }
            
            closedList.add(currentNode);
            
            // Check neighbors (8 directions)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    
                    int nx = currentNode.x + dx;
                    int ny = currentNode.y + dy;
                    
                    if (nx < 0 || nx >= tileMap.getColumns() || ny < 0 || ny >= tileMap.getRows()) {
                        continue;
                    }
                    
                    if (hasCollision(ny, nx)) {
                        continue;
                    }
                    
                    // Prevent cutting through corners
                    if (Math.abs(dx) == 1 && Math.abs(dy) == 1) {
                        if (hasCollision(ny, currentNode.x) || 
                            hasCollision(currentNode.y, nx)) {
                            continue;
                        }
                    }
                    
                    Node neighbor = new Node(nx, ny);
                    if (closedList.contains(neighbor)) {
                        continue;
                    }
                    
                    int moveCost = (Math.abs(dx) + Math.abs(dy) == 2) ? 14 : 10;
                    int tentativeG = currentNode.g + moveCost;
                    
                    Node existingOpen = openMap.get(neighbor);
                    if (existingOpen == null || tentativeG < existingOpen.g) {
                        if (existingOpen != null) {
                            openList.remove(existingOpen);
                        }
                        
                        neighbor.parent = currentNode;
                        neighbor.g = tentativeG;
                        neighbor.h = calculateHeuristic(nx, ny, targetCol, targetRow);
                        neighbor.f = neighbor.g + neighbor.h;
                        
                        openList.add(neighbor);
                        openMap.put(neighbor, neighbor);
                    }
                }
            }
        }
        
        return null; // No path found
    }
    
    private boolean hasCollision(int row, int col) {
        if (collisionTable.hasCollision(tileMap.getTileAt(row, col))) {
            return true;
        }
        
        if (arena != null) {
            return hasTowerCollision(row, col);
        }
        
        return false;
    }
    
    private boolean hasTowerCollision(int row, int col) {
        int tileSize = Config.getTileSize();
        double tileX = col * tileSize;
        double tileY = row * tileSize;
        
        for (Tour tower : arena.tours()) {
            double towerPixelX = tower.position().x() * tileSize;
            double towerPixelY = tower.position().y() * tileSize;
            double towerWidth = tower.width() * tileSize;
            double towerHeight = tower.height() * tileSize;
            
            if (tileX < towerPixelX + towerWidth && tileX + tileSize > towerPixelX
                    && tileY < towerPixelY + towerHeight && tileY + tileSize > towerPixelY) {
                return true;
            }
        }
        
        for (Ancient ancient : arena.ancients()) {
            double ancientPixelX = ancient.position().x() * tileSize;
            double ancientPixelY = ancient.position().y() * tileSize;
            double ancientWidth = ancient.width() * tileSize;
            double ancientHeight = ancient.height() * tileSize;
            
            if (tileX < ancientPixelX + ancientWidth && tileX + tileSize > ancientPixelX
                    && tileY < ancientPixelY + ancientHeight && tileY + tileSize > ancientPixelY) {
                return true;
            }
        }
        
        return false;
    }
    
    private int calculateHeuristic(int x1, int y1, int x2, int y2) {
        // Octile distance for 8-way movement
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return 10 * (dx + dy) + (14 - 2 * 10) * Math.min(dx, dy);
    }

    private int[] findNearestWalkableTile(int targetCol, int targetRow) {
        // BFS to find the nearest walkable tile within a reasonable radius
        int maxRadius = 10;
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(new int[]{targetCol, targetRow});
        visited.add(targetCol + "," + targetRow);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];

            // If this tile is walkable, return it
            if (!hasCollision(cy, cx)) {
                return current;
            }

            // Check if we've exceeded radius
            if (Math.abs(cx - targetCol) >= maxRadius || Math.abs(cy - targetRow) >= maxRadius) {
                continue;
            }

            // Add neighbors
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = cx + dx;
                    int ny = cy + dy;

                    if (nx >= 0 && nx < tileMap.getColumns() && ny >= 0 && ny < tileMap.getRows()) {
                        String key = nx + "," + ny;
                        if (!visited.contains(key)) {
                            visited.add(key);
                            queue.add(new int[]{nx, ny});
                        }
                    }
                }
            }
        }

        return null;
    }
    
    private List<int[]> reconstructPath(Node targetNode) {
        List<int[]> path = new ArrayList<>();
        Node current = targetNode;
        while (current != null) {
            path.add(0, new int[]{current.x, current.y});
            current = current.parent;
        }
        return path;
    }
}
