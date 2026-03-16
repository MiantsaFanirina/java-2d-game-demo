package Core.Tile;

/**
 * Table de collision - définit quelles tuiles sont bloquantes.
 * 
 * Concepts clés pour un débutant:
 * - collisionByTileId[tileId] = true signifie que la tuile est bloquante (on ne peut pas traverser)
 * - Cette table est utilisée par le CollisionDetector pour détecter les collisions
 * - Les murs, l'eau et certains obstacles sont des tuiles bloquantes
 * - L'herbe, le sable et les chemins sont des tuiles praticables
 * 
 * Exemple: si collisionByTileId[2] = true, alors la tuile d'ID 2 est un mur infranchissable
 */
public class CollisionTable {
    private final boolean[] collisionByTileId;

    public CollisionTable(boolean[] collisionByTileId) {
        this.collisionByTileId = collisionByTileId;
    }

    public boolean hasCollision(int tileId) {
        return tileId >= 0
                && tileId < collisionByTileId.length
                && collisionByTileId[tileId];
    }
}

