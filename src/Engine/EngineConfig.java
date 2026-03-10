package Engine;

import Core.Config;

import java.awt.Dimension;
import java.awt.Toolkit;

public class EngineConfig {
    private static int screenWidth;
    private static int screenHeight;

    private static final String PLAYER_IMAGE_PATH = "src/Resource/Player/";
    private static final String MAP_FILE_PATH = "src/Data/Map.txt";
    private static final int MAX_TILES = 50;

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxScreenCol = screenSize.width / Config.getTileSize();
        int maxScreenRow = screenSize.height / Config.getTileSize();
        screenWidth = maxScreenCol * Config.getTileSize();
        screenHeight = maxScreenRow * Config.getTileSize();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void updateScreenSize(int width, int height) {
        int maxScreenCol = width / Config.getTileSize();
        int maxScreenRow = height / Config.getTileSize();
        screenWidth = maxScreenCol * Config.getTileSize();
        screenHeight = maxScreenRow * Config.getTileSize();
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

    private EngineConfig() {}
}

