package game.engine.rendering;

import Core.Config;
import Core.Entity.Player;
import Core.Moba.Units.CoreBase;
import Core.Moba.Units.Tour;
import Core.Moba.Units.TowerProjectile;
import Engine.Render.Camera;
import Engine.Render.ClickEffect;
import Engine.Render.HUD.HUDRenderer;
import Engine.Render.World.CoreBaseRenderer;
import Engine.Render.World.PlayerRenderer;
import Engine.Render.World.ProjectileRenderer;
import Engine.Render.World.TileRenderer;
import Engine.Render.World.TowerRenderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all rendering operations for the game.
 * Separates rendering concerns from game logic and state management.
 */
public class RenderingManager {
    
    private final TileRenderer tileRenderer;
    private final PlayerRenderer playerRenderer;
    private final TowerRenderer towerRenderer;
    private final CoreBaseRenderer coreBaseRenderer;
    private final ProjectileRenderer projectileRenderer;
    private final HUDRenderer hudRenderer;
    private final Camera camera;
    
    public RenderingManager(TileRenderer tileRenderer, 
                           PlayerRenderer playerRenderer,
                           TowerRenderer towerRenderer,
                           CoreBaseRenderer coreBaseRenderer,
                           ProjectileRenderer projectileRenderer,
                           HUDRenderer hudRenderer,
                           Camera camera) {
        this.tileRenderer = tileRenderer;
        this.playerRenderer = playerRenderer;
        this.towerRenderer = towerRenderer;
        this.coreBaseRenderer = coreBaseRenderer;
        this.projectileRenderer = projectileRenderer;
        this.hudRenderer = hudRenderer;
        this.camera = camera;
    }
    
    public void renderGameWorld(Graphics2D g2, int screenWidth, int screenHeight,
                               Player player, List<Tour> towers, List<CoreBase> coreBases,
                               List<TowerProjectile> projectiles, List<ClickEffect> clickEffects) {
        // Save original transform
        AffineTransform oldTransform = g2.getTransform();
        
        // Apply camera transformations
        g2.scale(camera.getZoom(), camera.getZoom());
        g2.translate(-camera.getX(), -camera.getY());
        
        // Draw tile map
        tileRenderer.draw(g2, camera, screenWidth, screenHeight);
        
        // Update tower animations
        for (Tour tour : towers) {
            tour.updateAnimation();
        }
        
        // Depth-sorted rendering
        renderDepthSorted(g2, player, towers, coreBases);
        
        // Draw projectiles
        for (TowerProjectile projectile : projectiles) {
            projectileRenderer.draw(g2, projectile, camera);
        }
        
        // Draw click effects
        for (ClickEffect effect : clickEffects) {
            effect.draw(g2);
        }
        
        // Restore transform
        g2.setTransform(oldTransform);
    }
    
    private void renderDepthSorted(Graphics2D g2, Player player, List<Tour> towers, List<CoreBase> coreBases) {
        List<RenderableEntity> entities = new ArrayList<>();
        int tileSize = Config.getTileSize();
        
        // Add towers
        for (Tour tour : towers) {
            double towerBaseY = (tour.position().y() + tour.height()) * tileSize;
            entities.add(new RenderableEntity(towerBaseY, RenderableEntity.Type.TOWER, tour));
        }
        
        // Add core bases
        for (CoreBase coreBase : coreBases) {
            double coreBaseBaseY = (coreBase.position().y() + coreBase.height()) * tileSize;
            entities.add(new RenderableEntity(coreBaseBaseY, RenderableEntity.Type.CORE_BASE, coreBase));
        }
        
        // Add player
        if (player.isAlive()) {
            double playerBaseY = player.getY() + tileSize;
            entities.add(new RenderableEntity(playerBaseY, RenderableEntity.Type.PLAYER, player));
        }
        
        // Sort by Y position (lower Y = behind, higher Y = in front)
        entities.sort(java.util.Comparator.comparingDouble(e -> e.renderY));
        
        // Draw in sorted order
        for (RenderableEntity entity : entities) {
            switch (entity.type) {
                case TOWER -> towerRenderer.draw(g2, (Tour) entity.entity, camera);
                case CORE_BASE -> coreBaseRenderer.draw(g2, (CoreBase) entity.entity, camera);
                case PLAYER -> playerRenderer.draw(g2, player);
            }
        }
    }
    
    public void renderUI(Graphics2D g2) {
        if (hudRenderer != null) {
            hudRenderer.render(g2);
        }
    }
    
    private static class RenderableEntity {
        enum Type { TOWER, CORE_BASE, PLAYER }
        final double renderY;
        final Type type;
        final Object entity;
        
        RenderableEntity(double renderY, Type type, Object entity) {
            this.renderY = renderY;
            this.type = type;
            this.entity = entity;
        }
    }
}