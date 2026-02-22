package Main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // SCREEM SETTINGS
    static final int originalTileSize = 16;
    static final int scale = 3;
    public static final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 22;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    final int MAX_FPS = 120 ;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    // PLAYER ENTITY
    Player player = new Player(this, keyHandler);


    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        // KEY LISTENER
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / MAX_FPS;
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

        player.draw(g2);

        g2.dispose();
    }


}
