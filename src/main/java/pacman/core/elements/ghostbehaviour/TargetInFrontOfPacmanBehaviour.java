package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Pacman;
import pacman.core.elements.Ghost;
import pacman.core.Colour;
import pacman.core.Direction;
import pacman.core.Location;

import java.util.Collection;

public class TargetInFrontOfPacmanBehaviour extends GhostCalmBehaviour {
    private final Pacman pacman;
    public TargetInFrontOfPacmanBehaviour(Pacman pacman, Colour colour, Collection<Ghost> allGhosts) {
        super(colour, allGhosts);
        this.pacman = pacman;
    }

    public Location targetLocation(Location ghostLocation) {
        Location location = pacman.getLocation();
        Direction direction = pacman.getDirection();
        for (int i = 0 ; i < 4 ; i++) {
            location = direction.movement().apply(location);
        }
        return location;
    }
}
