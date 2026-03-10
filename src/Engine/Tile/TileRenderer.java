package Engine.Tile;

import Core.Config;
import Core.Tile.TileMap;

import Engine.Render.Camera;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public class TileRenderer {

    private final TileMap tileMap;
    private final Tile[] tiles;

    public TileRenderer(TileMap tileMap, Tile[] tiles) {
        this.tileMap = tileMap;
        this.tiles = tiles;
    }

    public void draw(Graphics2D g2, Camera camera, int panelWidth, int panelHeight) {
        int tileSize = Config.getTileSize();
        
        // Calculate visible tile range
        float zoom = camera.getZoom();
        float startX = camera.getX();
        float startY = camera.getY();
        float endX = startX + (panelWidth / zoom);
        float endY = startY + (panelHeight / zoom);

        int startCol = Math.max(0, (int) (startX / tileSize));
        int startRow = Math.max(0, (int) (startY / tileSize));
        int endCol = Math.min(tileMap.getColumns(), (int) (endX / tileSize) + 1);
        int endRow = Math.min(tileMap.getRows(), (int) (endY / tileSize) + 1);

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                drawTile(g2, row, col, tileSize);
            }
        }
    }

    private void drawTile(Graphics2D g2, int row, int col, int tileSize) {
        int tileId = tileMap.getTileAt(row, col);
        int x = col * tileSize;
        int y = row * tileSize;

        if (tileId >= 0 && tileId < tiles.length && tiles[tileId] != null) {
            Tile tile = tiles[tileId];
            Image image;
            
            if (tileId == 5 && tile.getImages().size() > 1) {
                 // Randomize water texture every 3s
                 long timeInSeconds = System.currentTimeMillis() / 3000;
                 // Use a simple hash of position and time to pick an image index
                int seed = (row * 73 + col * 37 + (int)timeInSeconds);
                // Use a proper pseudo-random selection based on seed
                int index = Math.abs(new java.util.Random(seed).nextInt()) % tile.getImages().size();
                image = tile.getImages().get(index);
            } else {
                image = tile.getImage();
            }

            if (image != null) {
                g2.drawImage(image, x, y, tileSize, tileSize, null);
                return;
            }
        }

        g2.setColor(Color.black);
        g2.fillRect(x, y, tileSize, tileSize);
    }
}

