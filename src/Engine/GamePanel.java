package Engine;

import Core.Config;
import Core.Entity.Player;
import Core.Tile.CollisionTable;
import Core.Tile.TileMap;
import Engine.Input.KeyHandler;
import Engine.Input.MouseHandler;
import Engine.Render.PlayerRenderer;
import Engine.Render.PlayerSprites;
import Engine.Tile.MapParser;
import Engine.Tile.Tile;
import Engine.Tile.TileLoader;
import Engine.Tile.TileRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel implements Runnable {
    
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;
    private final Player player;
    private final TileRenderer tileRenderer;
    private final PlayerRenderer playerRenderer;
    private Thread gameThread;
    
    public GamePanel() {
        setPreferredSize(new Dimension(EngineConfig.getScreenWidth(), EngineConfig.getScreenHeight()));
        setBackground(Color.black);
        setDoubleBuffered(true);
        
        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();

        MapParser mapParser = new MapParser();
        MapParser.MapData mapData = mapParser.parse(EngineConfig.getMapFilePath());
        TileMap tileMap = new TileMap(mapData.tileNumbers(), mapData.columns(), mapData.rows());

        TileLoader tileLoader = new TileLoader();
        Tile[] tiles = tileLoader.load(EngineConfig.getMapFilePath(), EngineConfig.getMaxTiles());
        CollisionTable collisionTable = new CollisionTable(tileLoader.buildCollisionTable(tiles));

        tileRenderer = new TileRenderer(tileMap, tiles);
        player = new Player(keyHandler, mouseHandler, tileMap, collisionTable);
        playerRenderer = new PlayerRenderer(new PlayerSprites());
        
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
                
                EngineConfig.updateScreenSize(cols * Config.getTileSize(), rows * Config.getTileSize());
                
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
        tileRenderer.draw(g2, getWidth(), getHeight());
        playerRenderer.draw(g2, player);
        
        g2.dispose();
    }
}
