package Engine.Input;

import Core.Input.MoveInput;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

/**
 * Gère les entrées clavier du joueur.
 * 
 * Concepts clés pour un débutant:
 * - KeyListener est une interface Java pour capter les événements clavier
 * - MoveInput est une interface qui définit comment le mouvement est géré
 * - VK = Virtual Key, les codes des touches (VK_W = W, VK_S = S, etc.)
 * - Le jeu détecte si le clavier est AZERTY ou QWERTY pour adapter les touches
 * - tabCallback et escapeCallback permettent d'assigner des actions à TAB et ÉCHAP
 */
public class KeyHandler implements KeyListener, MoveInput {
    
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private Consumer<Void> tabCallback;
    private Consumer<Void> escapeCallback;
    private final boolean isAzerty;
    
    public KeyHandler() {
        this.isAzerty = KeyboardLayoutDetector.isAzerty();
    }
    
    public void setTabCallback(Consumer<Void> callback) {
        this.tabCallback = callback;
    }
    
    public void setEscapeCallback(Consumer<Void> callback) {
        this.escapeCallback = callback;
    }
    
    public boolean isUpPressed() {
        return upPressed;
    }
    
    public boolean isDownPressed() {
        return downPressed;
    }
    
    public boolean isLeftPressed() {
        return leftPressed;
    }
    
    public boolean isRightPressed() {
        return rightPressed;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        updateKeyState(keyCode, true);
        
        if (keyCode == KeyEvent.VK_R) {
            e.consume();
            if (tabCallback != null) {
                tabCallback.accept(null);
            }
        }
        
        if (keyCode == KeyEvent.VK_ESCAPE) {
            e.consume();
            if (escapeCallback != null) {
                escapeCallback.accept(null);
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        updateKeyState(e.getKeyCode(), false);
    }
    
    private void updateKeyState(int keyCode, boolean pressed) {
        boolean isUp = isMovementKey(keyCode, true, false, false, false);
        boolean isDown = isMovementKey(keyCode, false, true, false, false);
        boolean isLeft = isMovementKey(keyCode, false, false, true, false);
        boolean isRight = isMovementKey(keyCode, false, false, false, true);
        
        if (isUp) upPressed = pressed;
        if (isDown) downPressed = pressed;
        if (isLeft) leftPressed = pressed;
        if (isRight) rightPressed = pressed;
    }
    
    private boolean isMovementKey(int keyCode, boolean checkUp, boolean checkDown, 
                                  boolean checkLeft, boolean checkRight) {
        if (checkUp) {
            return isAzerty ? keyCode == KeyEvent.VK_Z : keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_Z;
        }
        if (checkDown) {
            return keyCode == KeyEvent.VK_S;
        }
        if (checkLeft) {
            return isAzerty ? keyCode == KeyEvent.VK_Q : keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_Q;
        }
        if (checkRight) {
            return keyCode == KeyEvent.VK_D;
        }
        return false;
    }
}
