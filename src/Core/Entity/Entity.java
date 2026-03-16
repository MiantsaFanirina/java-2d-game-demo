package Core.Entity;

/**
 * Classe de base pour toutes les entités du jeu.
 * 
 * Concepts clés pour un débutant:
 * - Une "entité" est tout objet qui a une position et peut être dessiné
 * - Cette classe définit les propriétés de base: position (x, y), vitesse, direction
 * - spriteCounter et spriteNum gèrent l'animation des sprites (images)
 * - updateSpriteAnimation() fait tourner les frames d'animation pour créer un mouvement
 * 
 * Héritage: Player, Tour (tour), Minion, Heros, etc. étendent tous cette classe
 * Cela permet de traiter tous ces objets de la même manière dans le code!
 */
public class Entity {
    
    private double x;
    private double y;
    private int speed;
    private Direction direction;
    
    private int spriteCounter = 0;
    private int spriteNum = 1;
    
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    
    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }
    
    public int getSpriteCounter() { return spriteCounter; }
    public void setSpriteCounter(int spriteCounter) { this.spriteCounter = spriteCounter; }
    
    public int getSpriteNum() { return spriteNum; }
    public void setSpriteNum(int spriteNum) { this.spriteNum = spriteNum; }
    
    public void updateSpriteAnimation(int animationSpeed) {
        spriteCounter++;
        if (spriteCounter > animationSpeed) {
            spriteNum = spriteNum % 6 + 1;  // Cycle through frames 1-6
            spriteCounter = 0;
        }
    }
}
