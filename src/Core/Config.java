package Core;

public class Config {
    
    private static final int ORIGINAL_TILE_SIZE = 16;
    private static final int SCALE = 3;
    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    
    private static final int MAX_FPS = 60;
    private static final double NANOSECONDS_PER_FRAME = 1_000_000_000.0 / MAX_FPS;
    
    private static final int PLAYER_DEFAULT_SPEED = 4;
    private static final int PLAYER_DEFAULT_X = 480;
    private static final int PLAYER_DEFAULT_Y = 240;
    
    private static final int SPRITE_ANIMATION_SPEED = 10;
    
    // Camera settings
    private static final int CAMERA_EDGE_THRESHOLD = 80; // pixels
    private static final int CAMERA_SPEED = 20;
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 3.0f;
    private static final float ZOOM_STEP = 0.1f;
    
    public static int getTileSize() {
        return TILE_SIZE;
    }
    
    public static double getNanosecondsPerFrame() {
        return NANOSECONDS_PER_FRAME;
    }
    
    public static int getPlayerDefaultSpeed() {
        return PLAYER_DEFAULT_SPEED;
    }
    
    public static int getPlayerDefaultX() {
        return PLAYER_DEFAULT_X;
    }
    
    public static int getPlayerDefaultY() {
        return PLAYER_DEFAULT_Y;
    }
    
    public static int getSpriteAnimationSpeed() {
        return SPRITE_ANIMATION_SPEED;
    }
    
    public static int getCameraEdgeThreshold() {
        return CAMERA_EDGE_THRESHOLD;
    }
    
    public static int getCameraSpeed() {
        return CAMERA_SPEED;
    }
    
    public static float getMinZoom() {
        return MIN_ZOOM;
    }
    
    public static float getMaxZoom() {
        return MAX_ZOOM;
    }
    
    public static float getZoomStep() {
        return ZOOM_STEP;
    }
    
    private Config() {}
}
