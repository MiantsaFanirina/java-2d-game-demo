package Main;

import Entity.Player;
import Tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel implements Runnable {
    // SCREEM SETTINGS
    static final int originalTileSize = 16;
    static final int scale = 3;
    public static final int tileSize = originalTileSize * scale;
    public static int maxScreenCol = 22;
    public static int maxScreenRow = 16;
    public static int screenWidth = tileSize * maxScreenCol;
    public static int screenHeight = tileSize * maxScreenRow;

    final int MAX_FPS = 120 ;


    // TILE MANAGER
    TileManager tileManager = new TileManager(this);

    // PLAYER ENTITY
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        // KEY LISTENER
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScreenSize();
            }
        });
    }

    private void updateScreenSize() {
        maxScreenCol = getWidth() / tileSize;
        maxScreenRow = getHeight() / tileSize;
        screenWidth = maxScreenCol * tileSize;
        screenHeight = maxScreenRow * tileSize;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / MAX_FPS;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;

            if (elapsedTime >= drawInterval) {

                lastTime = currentTime;

                update();
                repaint();

            }

        }

    }

    public void update() {
        player.update();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileManager.draw(g2);
        player.draw(g2);

        g2.dispose();
    }


}
