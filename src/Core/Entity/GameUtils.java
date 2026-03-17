package Core.Entity;

import Core.Config;

public class GameUtils {

    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double distanceSquared(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    public static double normalize(double value) {
        return value > 0 ? 1.0 : value < 0 ? -1.0 : 0.0;
    }

    public static double[] normalizeVector(double x, double y) {
        double length = Math.sqrt(x * x + y * y);
        if (length == 0) return new double[]{0, 0};
        return new double[]{x / length, y / length};
    }

    public static double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static boolean isNearlyEqual(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    public static int pixelToTileX(double pixelX) {
        return (int) (pixelX / Config.getTileSize());
    }

    public static int pixelToTileY(double pixelY) {
        return (int) (pixelY / Config.getTileSize());
    }

    public static double tileToPixelX(int tileX) {
        return tileX * Config.getTileSize();
    }

    public static double tileToPixelY(int tileY) {
        return tileY * Config.getTileSize();
    }

    public static double getTileCenterX(int tileX) {
        return tileX * Config.getTileSize() + Config.getTileSize() / 2.0;
    }

    public static double getTileCenterY(int tileY) {
        return tileY * Config.getTileSize() + Config.getTileSize() / 2.0;
    }

    public static double getPixelCenter(double pixel) {
        int tile = (int) (pixel / Config.getTileSize());
        return tile * Config.getTileSize() + Config.getTileSize() / 2.0;
    }

    public static boolean isValidTile(int col, int row, int columns, int rows) {
        return col >= 0 && col < columns && row >= 0 && row < rows;
    }

    public static int getTileFromMap(double pixel, int mapSize) {
        int tile = (int) (pixel / Config.getTileSize());
        return Math.max(0, Math.min(tile, mapSize - 1));
    }

    private GameUtils() {}
}
