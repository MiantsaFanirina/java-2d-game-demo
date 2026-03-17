package Engine.UI.HeroSelection;

import Core.Database.model.Hero;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 * Controller for hero selection: handles input events and updates model.
 */
public class HeroSelectionController {
    
    private final HeroSelectionModel model;
    private final HeroSelectionUI ui;
    private SelectionListener listener;
    
    public interface SelectionListener {
        void onHeroSelected(Hero hero);
        void onGoBack();
    }
    
    public HeroSelectionController(HeroSelectionModel model, HeroSelectionUI ui) {
        this.model = model;
        this.ui = ui;
    }
    
    public void setSelectionListener(SelectionListener listener) {
        this.listener = listener;
    }
    
    public void handleMouseClick(int x, int y, int button) {
        // Check category tabs
        if (ui.isOverTab(x, y)) {
            String category = ui.getCategoryAt(x, y);
            if (category != null) {
                model.selectCategory(category);
                ui.repaint();
            }
            return;
        }
        
        // Check hero cards
        int clickedIndex = ui.getHeroIndexAt(x, y);
        if (clickedIndex >= 0) {
            model.selectHero(clickedIndex);
            ui.repaint();
            return;
        }
        
        // Check buttons
        Rectangle backButton = ui.getBackButtonBounds();
        if (backButton != null && backButton.contains(x, y)) {
            if (listener != null) {
                listener.onGoBack();
            }
            return;
        }
        
        Rectangle startButton = ui.getStartButtonBounds();
        if (startButton != null && startButton.contains(x, y)) {
            Hero selected = model.getSelectedHero();
            if (selected != null && listener != null) {
                listener.onHeroSelected(selected);
            }
        }
    }
    
    public void handleMouseMove(int x, int y) {
        ui.updateHoverState(x, y);
        ui.repaint();
    }
    
    public void handleMouseWheel(int rotation) {
        ui.scroll(rotation);
        ui.repaint();
    }
    
    public void handleKeyNav(int keyCode) {
        int filteredCount = model.getFilteredHeroCount();
        if (filteredCount == 0) return;
        
        int columns = ui.calculateColumns();
        int currentIndex = model.getSelectedIndex();
        int newIndex = currentIndex;
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                newIndex = Math.max(0, currentIndex - columns);
                break;
            case KeyEvent.VK_DOWN:
                newIndex = Math.min(filteredCount - 1, currentIndex + columns);
                break;
            case KeyEvent.VK_LEFT:
                newIndex = Math.max(0, currentIndex - 1);
                break;
            case KeyEvent.VK_RIGHT:
                newIndex = Math.min(filteredCount - 1, currentIndex + 1);
                break;
            case KeyEvent.VK_ENTER:
                Hero selected = model.getSelectedHero();
                if (selected != null && listener != null) {
                    listener.onHeroSelected(selected);
                }
                return;
        }
        
        if (newIndex != currentIndex) {
            model.selectHero(newIndex);
            ui.ensureVisible(newIndex);
            ui.repaint();
        }
    }
}