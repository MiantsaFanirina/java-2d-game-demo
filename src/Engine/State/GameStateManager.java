package Engine.State;

import javax.swing.*;
import java.awt.*;

/**
 * Manages game state transitions and panel visibility.
 * Separates state management from UI rendering concerns.
 */
public class GameStateManager {
    
    public enum GameState {
        MAIN_MENU,
        HERO_SELECTION,
        PLAYING,
        PAUSED
    }
    
    private GameState currentState = GameState.MAIN_MENU;
    private final JPanel container;
    
    // UI Panels
    private JPanel mainPanel;
    private JPanel heroSelectionPanel;
    private JPanel pauseMenu;
    
    public GameStateManager(JPanel container) {
        this.container = container;
    }
    
    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }
    
    public void setHeroSelectionPanel(JPanel heroSelectionPanel) {
        this.heroSelectionPanel = heroSelectionPanel;
    }
    
    public void setPauseMenu(JPanel pauseMenu) {
        this.pauseMenu = pauseMenu;
    }
    
    public GameState getCurrentState() {
        return currentState;
    }
    
    public boolean isState(GameState state) {
        return currentState == state;
    }
    
    public void showMainMenu() {
        hideAllPanels();
        if (mainPanel != null) {
            mainPanel.setVisible(true);
            mainPanel.requestFocusInWindow();
        }
        currentState = GameState.MAIN_MENU;
        container.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        container.revalidate();
        container.repaint();
    }
    
    public void showHeroSelection() {
        hideAllPanels();
        if (heroSelectionPanel != null) {
            heroSelectionPanel.setVisible(true);
            heroSelectionPanel.requestFocusInWindow();
        }
        currentState = GameState.HERO_SELECTION;
        container.revalidate();
        container.repaint();
    }
    
    public void showGame() {
        hideAllPanels();
        currentState = GameState.PLAYING;
        container.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        container.revalidate();
        container.repaint();
    }
    
    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
            if (pauseMenu != null) {
                pauseMenu.setVisible(true);
                pauseMenu.requestFocusInWindow();
                // Bring to front
                container.setComponentZOrder(pauseMenu, 0);
            }
            container.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            container.repaint();
        }
    }
    
    public void resumeGame() {
        if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
            if (pauseMenu != null) {
                pauseMenu.setVisible(false);
            }
            container.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            container.repaint();
        }
    }
    
    public void returnToMainMenu() {
        showMainMenu();
    }
    
    private void hideAllPanels() {
        if (mainPanel != null) mainPanel.setVisible(false);
        if (heroSelectionPanel != null) heroSelectionPanel.setVisible(false);
        if (pauseMenu != null) pauseMenu.setVisible(false);
    }
    
    public void updateCursor(boolean shouldShowPointer) {
        if (shouldShowPointer) {
            container.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            container.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}