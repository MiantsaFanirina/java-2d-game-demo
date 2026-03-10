package Core.Tile;

import Core.Config;

import java.awt.*;

public class TileManager {
    
    private final Tile[] tiles;
    private final int[][] mapTiles;
    private final int mapColumns;
    private final int mapRows;
    
    public TileManager() {
        MapParser mapParser = new MapParser();
        MapParser.MapData mapData = mapParser.parse(Config.getMapFilePath());
        
        this.mapTiles = mapData.tileNumbers();
        this.mapColumns = mapData.columns();
        this.mapRows = mapData.rows();
        
        TileLoader tileLoader = new TileLoader();
        this.tiles = tileLoader.load(Config.getMapFilePath(), Config.getMaxTiles());
    }
    
    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        int maxCol = (panelWidth + Config.getTileSize() - 1) / Config.getTileSize();
        int maxRow = (panelHeight + Config.getTileSize() - 1) / Config.getTileSize();
        
        int endCol = Math.min(maxCol, mapColumns);
        int endRow = Math.min(maxRow, mapRows);
        
        for (int row = 0; row < endRow; row++) {
            for (int col = 0; col < endCol; col++) {
                drawTile(g2, row, col);
            }
        }
    }
    
    private void drawTile(Graphics2D g2, int row, int col) {
        int tileId = mapTiles[row][col];
        int x = col * Config.getTileSize();
        int y = row * Config.getTileSize();
        
        if (isValidTile(tileId)) {
            Image image = tiles[tileId].getImage();
            if (image != null) {
                g2.drawImage(image, x, y, Config.getTileSize(), Config.getTileSize(), null);
                return;
            }
        }
        
        g2.setColor(Color.black);
        g2.fillRect(x, y, Config.getTileSize(), Config.getTileSize());
    }
    
    private boolean isValidTile(int tileId) {
        return tileId >= 0 && tileId < tiles.length && tiles[tileId] != null;
    }
    
    public boolean hasCollision(int tileId) {
        return isValidTile(tileId) && tiles[tileId].isCollision();
    }
    
    public int getTileAt(int row, int col) {
        if (row >= 0 && row < mapRows && col >= 0 && col < mapColumns) {
            return mapTiles[row][col];
        }
        return -1;
    }
}
