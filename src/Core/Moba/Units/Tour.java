package Core.Moba.Units;

import Core.Moba.Combat.Stats;
import Core.Moba.World.Vec2;

public final class Tour extends Unite {
    private final double portee;

    public Tour(Vec2 position, int maxHp, int attack, int defense, double portee) {
        super(position, new Stats(maxHp, 0, attack, defense, 0));
        this.portee = Math.max(0, portee);
    }

    public double portee() {
        return portee;
    }
}

