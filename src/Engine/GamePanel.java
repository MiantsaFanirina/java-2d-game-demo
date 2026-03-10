package Engine;

import Core.Config;
import Core.Entity.Player;
import Core.Tile.CollisionTable;
import Core.Tile.TileMap;
import Engine.Input.KeyHandler;
import Engine.Input.MouseHandler;
import Engine.Render.Camera;
import Engine.Render.ClickEffect;
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
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
    
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;
    private final Player player;
    private final TileRenderer tileRenderer;
    private final PlayerRenderer playerRenderer;
    private final Camera camera;
    private final List<ClickEffect> clickEffects = new ArrayList<>();
    private Thread gameThread;
    
    public GamePanel() {
        setPreferredSize(new Dimension(EngineConfig.getScreenWidth(), EngineConfig.getScreenHeight()));
        setBackground(Color.black);
        setDoubleBuffered(true);
        
        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();
        camera = new Camera(EngineConfig.getScreenWidth(), EngineConfig.getScreenHeight());
        mouseHandler.setCamera(camera);

        MapParser mapParser = new MapParser();
        MapParser.MapData mapData = mapParser.parse(EngineConfig.getMapFilePath());
        TileMap tileMap = new TileMap(mapData.tileNumbers(), mapData.columns(), mapData.rows());
        camera.setWorldSize(tileMap.getColumns() * Config.getTileSize(), tileMap.getRows() * Config.getTileSize());

        TileLoader tileLoader = new TileLoader();
        Tile[] tiles = tileLoader.load(EngineConfig.getMapFilePath(), EngineConfig.getMaxTiles());
        CollisionTable collisionTable = new CollisionTable(tileLoader.buildCollisionTable(tiles));

        tileRenderer = new TileRenderer(tileMap, tiles);
        player = new Player(keyHandler, mouseHandler, tileMap, collisionTable);
        playerRenderer = new PlayerRenderer(new PlayerSprites());
        
        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        setFocusable(true);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                
                int cols = width / Config.getTileSize();
                int rows = height / Config.getTileSize();
                
                EngineConfig.updateScreenSize(cols * Config.getTileSize(), rows * Config.getTileSize());
                camera.setViewportSize(width, height);
                
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
        camera.update(mouseHandler.getCurrentX(), mouseHandler.getCurrentY());
        camera.zoom(mouseHandler.getWheelRotation());
        
        if (mouseHandler.hasNewClick()) {
            clickEffects.add(new ClickEffect(mouseHandler.getLastClickWorldX(), mouseHandler.getLastClickWorldY()));
            mouseHandler.clearNewClick();
        }

        Iterator<ClickEffect> it = clickEffects.iterator();
        while (it.hasNext()) {
            ClickEffect effect = it.next();
            effect.update();
            if (effect.isDead()) {
                it.remove();
            }
        }

        player.update();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;

        // Apply camera transformations
        AffineTransform oldTransform = g2.getTransform();
        g2.scale(camera.getZoom(), camera.getZoom());
        g2.translate(-camera.getX(), -camera.getY());

        tileRenderer.draw(g2, camera, getWidth(), getHeight());
        playerRenderer.draw(g2, player);

        for (ClickEffect effect : clickEffects) {
            effect.draw(g2);
        }

        g2.setTransform(oldTransform);
        
        g2.dispose();
    }
}
