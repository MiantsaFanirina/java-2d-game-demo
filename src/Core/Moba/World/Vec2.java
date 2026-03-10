package Core.Moba.World;

public record Vec2(double x, double y) {
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 normalized() {
        double len = length();
        if (len == 0) return new Vec2(0, 0);
        return new Vec2(x / len, y / len);
    }
}

