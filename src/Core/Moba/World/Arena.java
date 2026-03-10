package Core.Moba.World;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class Arena {
    private final Map<Voie, List<Vec2>> lanesWaypoints;
    private final List<Vec2> jungleCampPositions;

    public Arena() {
        lanesWaypoints = new EnumMap<>(Voie.class);
        for (Voie v : Voie.values()) {
            lanesWaypoints.put(v, new ArrayList<>());
        }
        jungleCampPositions = new ArrayList<>();
    }

    public List<Vec2> voieWaypoints(Voie voie) {
        return lanesWaypoints.get(voie);
    }

    public List<Vec2> jungleCampPositions() {
        return jungleCampPositions;
    }
}

