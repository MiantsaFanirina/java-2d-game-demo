package Core.Entity;

/**
 * Directions cardinales du jeu - utilisée pour l'orientation des sprites.
 * 
 * Concepts clés pour un débutant:
 * - enum = énumération: une liste fixe de valeurs (comme un menu déroulant)
 * - Chaque direction correspond à une animation de sprite différente
 * - fromDelta(dx, dy) convertit un déplacement en direction
 *   Exemple: dx=10, dy=0 → RIGHT (on bouge plus à droite qu'en haut/bas)
 * - fromString permet de convertir un texte en direction (pour charger depuis un fichier)
 */
public enum Direction {
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right");
    
    private final String name;
    
    Direction(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static Direction fromString(String str) {
        for (Direction dir : values()) {
            if (dir.name.equals(str)) {
                return dir;
            }
        }
        return DOWN;
    }
    
    public static Direction fromDelta(int dx, int dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? RIGHT : LEFT;
        } else if (Math.abs(dy) > Math.abs(dx)) {
            return dy > 0 ? DOWN : UP;
        } else if (dx != 0) {
            return dx > 0 ? RIGHT : LEFT;
        }
        return DOWN;
    }
}
