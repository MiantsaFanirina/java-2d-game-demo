package Core.Moba.Units;

import Core.Moba.Combat.Stats;
import Core.Moba.World.Vec2;

import java.util.Objects;

public abstract class Unite {
    private Vec2 position;
    private final Stats stats;

    protected Unite(Vec2 position, Stats stats) {
        this.position = Objects.requireNonNull(position, "position");
        this.stats = Objects.requireNonNull(stats, "stats");
    }

    public Vec2 position() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = Objects.requireNonNull(position, "position");
    }

    public Stats stats() {
        return stats;
    }

    public boolean estMorte() {
        return stats.isDead();
    }

    public void subirDegats(int rawDamage) {
        // Simple damage model; can be expanded later to use defense, damage types, etc.
        stats.takeDamage(Math.max(0, rawDamage));
    }
}

