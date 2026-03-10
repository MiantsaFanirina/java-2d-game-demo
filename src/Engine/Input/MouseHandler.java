package Engine.Input;

import Core.Input.TargetInput;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter implements TargetInput {
    
    private int targetX = -1;
    private int targetY = -1;
    private boolean hasTarget = false;
    
    public int getTargetX() {
        return targetX;
    }
    
    public int getTargetY() {
        return targetY;
    }
    
    public boolean hasTarget() {
        return hasTarget;
    }
    
    public void clearTarget() {
        hasTarget = false;
        targetX = -1;
        targetY = -1;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            targetX = e.getX();
            targetY = e.getY();
            hasTarget = true;
        }
    }
}
