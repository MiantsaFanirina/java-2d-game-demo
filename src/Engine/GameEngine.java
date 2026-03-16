package Engine;

import Core.Entity.Player;
import Core.Moba.Units.Tour;
import Core.Moba.Units.TowerProjectile;
import Core.Moba.Units.Unite;
import Core.Moba.World.Arena;
import Core.Tile.CollisionTable;
import Core.Tile.TileMap;
import Engine.Input.KeyHandler;
import Engine.Input.MouseHandler;
import Engine.Render.Camera;
import Engine.Render.ClickEffect;
import Core.Config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Moteur principal du jeu - gère la boucle de jeu (game loop).
 * 
 * Concepts clés pour un débutant:
 * - Une "game loop" est une boucle infinie qui met à jour le jeu ~60 fois par seconde
 * - update() met à jour tous les éléments du jeu (joueur, tours, caméra, etc.)
 * - Le panneau de dessin (GamePanel) dessine le jeu indépendamment (séparation rendu/logique)
 * - Thread: le jeu tourne sur un fil d'exécution séparé pour ne pas bloquer l'interface
 */
public class GameEngine {

    private final Player player;
    private final Camera camera;
    private final MouseHandler mouseHandler;
    private final Arena arena;
    private final List<ClickEffect> clickEffects = new ArrayList<>();
    private final List<TowerProjectile> tousProjectiles = new ArrayList<>();

    private Thread gameThread;
    private boolean running = false;
    private boolean paused = false;

    public GameEngine(Player player, Camera camera, MouseHandler mouseHandler, Arena arena) {
        this.player = player;
        this.camera = camera;
        this.mouseHandler = mouseHandler;
        this.arena = arena;
    }

    public void start() {
        if (!running) {
            running = true;
            gameThread = new Thread(this::gameLoop);
            gameThread.start();
        }
    }

    public void stop() {
        running = false;
        gameThread = null;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    private void gameLoop() {
        long lastTime = System.nanoTime();

        while (running) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;

            if (elapsedTime >= Config.getNanosecondsPerFrame()) {
                lastTime = currentTime;
                if (!paused) {
                    update();
                }
            }
            
            if (paused) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private void update() {
        double deltaSeconds = 1.0 / 60.0;
        
        if (player.justRespawned()) {
            camera.centerOn((float) player.getX(), (float) player.getY());
            player.clearJustRespawned();
        }
        
        camera.updatePlayerPosition((float) player.getX(), (float) player.getY());
        updateCamera();
        updateClickEffects();
        updateTowers(deltaSeconds);
        player.update();
    }

    private void updateTowers(double deltaSeconds) {
        List<Object> unites = arena.unites();
        
        for (Tour tour : arena.tours()) {
            tour.ai().mettreAJour(deltaSeconds, unites);
            
            if (tour.ai().doitAttaquer(deltaSeconds)) {
                int degats = tour.ai().calculerDegats();
                Object cible = tour.ai().cible();
                if (cible != null) {
                    System.out.println("Tour " + tour.equipe().couleur() + " at " + tour.position() + " firing at " + cible.getClass().getSimpleName());
                     TowerProjectile projectile = new TowerProjectile(tour, cible, degats);
                     tour.ajouterProjectile(projectile);
                     tousProjectiles.add(projectile);
                     // Reset tower attack ready flag after firing, animation continues to frame 20 then returns to idle automatically
                     tour.resetAttackReady();
                }
            }
            
            tour.mettreAJourProjectiles(deltaSeconds);
        }
        
        tousProjectiles.removeIf(p -> p.aFini());
    }

    private void updateCamera() {
        boolean isMovingWithWASD = player.isMovingWithWASD();
        if (isMovingWithWASD) {
            camera.setFollowPlayer(true);
        } else {
            camera.setFollowPlayer(false);
        }
        camera.update(mouseHandler.getCurrentX(), mouseHandler.getCurrentY());
        camera.zoom(mouseHandler.getWheelRotation());
    }

    private void updateClickEffects() {
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
    }

    public List<ClickEffect> getClickEffects() {
        return clickEffects;
    }

    public List<TowerProjectile> getProjectiles() {
        return tousProjectiles;
    }
    
    public void centerCameraOnPlayer() {
        camera.centerOn((float) player.getX(), (float) player.getY());
    }
}
