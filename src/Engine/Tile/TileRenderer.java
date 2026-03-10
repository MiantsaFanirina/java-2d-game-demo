package Engine.Tile;

import Core.Config;
import Core.Tile.TileMap;

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

    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        int tileSize = Config.getTileSize();
        int maxCol = (panelWidth + tileSize - 1) / tileSize;
        int maxRow = (panelHeight + tileSize - 1) / tileSize;

        int endCol = Math.min(maxCol, tileMap.getColumns());
        int endRow = Math.min(maxRow, tileMap.getRows());

        for (int row = 0; row < endRow; row++) {
            for (int col = 0; col < endCol; col++) {
                drawTile(g2, row, col, tileSize);
            }
        }
    }

    private void drawTile(Graphics2D g2, int row, int col, int tileSize) {
        int tileId = tileMap.getTileAt(row, col);
        int x = col * tileSize;
        int y = row * tileSize;

        if (tileId >= 0 && tileId < tiles.length && tiles[tileId] != null) {
            Image image = tiles[tileId].getImage();
            if (image != null) {
                g2.drawImage(image, x, y, tileSize, tileSize, null);
                return;
            }
        }

        g2.setColor(Color.black);
        g2.fillRect(x, y, tileSize, tileSize);
    }
}

