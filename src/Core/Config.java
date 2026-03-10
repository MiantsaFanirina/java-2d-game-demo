package Core;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Config {
    
    private static final int ORIGINAL_TILE_SIZE = 16;
    private static final int SCALE = 3;
    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    
    private static final int DEFAULT_MAX_SCREEN_COL = 22;
    private static final int DEFAULT_MAX_SCREEN_ROW = 16;
    
    private static int maxScreenCol;
    private static int maxScreenRow;
    private static int screenWidth;
    private static int screenHeight;
    
    private static final int MAX_FPS = 60;
    private static final double NANOSECONDS_PER_FRAME = 1_000_000_000.0 / MAX_FPS;
    
    private static final int PLAYER_DEFAULT_SPEED = 4;
    private static final int PLAYER_DEFAULT_X = 480;
    private static final int PLAYER_DEFAULT_Y = 240;
    
    private static final int SPRITE_ANIMATION_SPEED = 10;
    
    private static final String PLAYER_IMAGE_PATH = "src/Resource/Player/";
    private static final String MAP_FILE_PATH = "src/Data/Map.txt";
    
    private static final int MAX_TILES = 50;
    
    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        maxScreenCol = screenSize.width / TILE_SIZE;
        maxScreenRow = screenSize.height / TILE_SIZE;
        screenWidth = maxScreenCol * TILE_SIZE;
        screenHeight = maxScreenRow * TILE_SIZE;
    }
    
    public static int getTileSize() {
        return TILE_SIZE;
    }
    
    public static int getMaxScreenCol() {
        return maxScreenCol;
    }
    
    public static int getMaxScreenRow() {
        return maxScreenRow;
    }
    
    public static int getScreenWidth() {
        return screenWidth;
    }
    
    public static int getScreenHeight() {
        return screenHeight;
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
    
    public static String getPlayerImagePath() {
        return PLAYER_IMAGE_PATH;
    }
    
    public static String getMapFilePath() {
        return MAP_FILE_PATH;
    }
    
    public static int getMaxTiles() {
        return MAX_TILES;
    }
    
    public static void updateScreenSize(int width, int height) {
        maxScreenCol = width / TILE_SIZE;
        maxScreenRow = height / TILE_SIZE;
        screenWidth = maxScreenCol * TILE_SIZE;
        screenHeight = maxScreenRow * TILE_SIZE;
    }
    
    private Config() {}
}
