package Engine;

import Core.Config;
import Core.Entity.Player;
import Core.Tile.TileManager;
import Engine.Input.KeyHandler;
import Engine.Input.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel implements Runnable {
    
    private final TileManager tileManager;
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;
    private final Player player;
    private Thread gameThread;
    
    public GamePanel() {
        setPreferredSize(new Dimension(Config.getScreenWidth(), Config.getScreenHeight()));
        setBackground(Color.black);
        setDoubleBuffered(true);
        
        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();
        tileManager = new TileManager();
        player = new Player(keyHandler, mouseHandler, tileManager);
        
        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        setFocusable(true);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                
                int cols = width / Config.getTileSize();
                int rows = height / Config.getTileSize();
                
                Config.updateScreenSize(cols * Config.getTileSize(), rows * Config.getTileSize());
                
                repaint();
            }
        });
    }
    
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        
        while (gameThread != null) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;
            
            if (elapsedTime >= Config.getNanosecondsPerFrame()) {
                lastTime = currentTime;
                
                update();
                repaint();
            }
        }
    }
    
    private void update() {
        player.update();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2, getWidth(), getHeight());
        player.draw(g2);
        
        g2.dispose();
    }
}
