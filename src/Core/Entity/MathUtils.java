package Core.Entity;

/**
 * Utilitaires mathématiques - fonctions utilitaires pour les calculs géométriques.
 * 
 * Concepts clés pour un débutants:
 * - distance(): calcule la distance euclidienne entre deux points (Pythagore)
 * - distanceSquared(): distance au carré (plus rapide, utile pour des comparaisons)
 * - normalize(): converts a value to -1, 0, or 1
 * - normalizeVector(): rend un vecteur de longueur 1 (garde juste la direction)
 * - lerp(): interpolation linéaire - trouve une valeur entre deux autres (pour les animations)
 * - clamp(): limite une valeur entre un min et un max
 * 
 * Exemples:
 * - clamp(5, 0, 10) = 5 (dans les limites)
 * - clamp(-5, 0, 10) = 0 (trop petit, ramené à 0)
 * - clamp(15, 0, 10) = 10 (trop grand, ramené à 10)
 * - lerp(0, 10, 0.5) = 5 (à mi-chemin)
 */
public class MathUtils {

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

    private MathUtils() {}
}
