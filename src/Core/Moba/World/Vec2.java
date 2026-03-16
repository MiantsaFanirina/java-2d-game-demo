package Core.Moba.World;

/**
 * Vecteur 2D représentant une position ou un déplacement dans le jeu.
 * 
 * Concepts clés pour un débutant:
 * - "record" = classe immuable (comme un tuple, ses valeurs ne changent jamais)
 * - x, y = coordonnées (souvent en tuiles, pas en pixels)
 * - add() = addition vectorielle (déplacement)
 * - sub() = soustraction vectorielle
 * - length() = longueur du vecteur (distance depuis l'origine 0,0)
 * - distanceTo() = distance entre deux points (Pythagore)
 * - normalized() = vecteur de longueur 1 (direction uniquement)
 * 
 * Exemple: Si A=(5,5) et B=(8,7), distance = sqrt((8-5)² + (7-5)²) = sqrt(9+4) = √13
 */
public record Vec2(double x, double y) {
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double distanceTo(Vec2 other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public Vec2 normalized() {
        double len = length();
        if (len == 0) return new Vec2(0, 0);
        return new Vec2(x / len, y / len);
    }
}

