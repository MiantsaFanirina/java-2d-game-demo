package Core.Tile;

/**
 * Représente la carte du jeu sous forme de tuiles (tiles).
 * 
 * Concepts clés pour un débutant:
 * - tileNumbers[row][col]: tableau 2D contenant l'ID de chaque tuile
 * - Chaque ID correspond à un type de tuile différent (herbe, eau, mur, sable, etc.)
 * - La carte est organisée en rangées (rows) et colonnes (columns)
 * - getTileAt(row, col) retourne l'ID de la tuile à cette position
 * - setTileAt(row, col, tileId) modifie une tuile (utilisé pour remplacer les markers par le sol)
 * 
 * Exemple: si tileNumbers[5][3] = 18, la tuile à la rangée 5, colonne 3 est de type 18 (herbe)
 */
public class TileMap {
    private final int[][] tileNumbers;
    private final int columns;
    private final int rows;

    public TileMap(int[][] tileNumbers, int columns, int rows) {
        this.tileNumbers = tileNumbers;
        this.columns = columns;
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getTileAt(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < columns) {
            return tileNumbers[row][col];
        }
        return -1;
    }

    public void setTileAt(int row, int col, int tileId) {
        if (row >= 0 && row < rows && col >= 0 && col < columns) {
            tileNumbers[row][col] = tileId;
        }
    }
}

