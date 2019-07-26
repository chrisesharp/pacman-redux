package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Ghost;
import pacman.core.Colour;
import pacman.core.Location;

import java.util.Collection;

public class FixedLocationGhostBehaviour extends GhostCalmBehaviour {

    private final Location location;
    public FixedLocationGhostBehaviour(Location location, Colour colour, Collection<Ghost> allGhosts) {
        super(colour, allGhosts);
        this.location = location;
    }

    @Override
    public Location targetLocation(Location ghostLocation) {
        return this.location;
    }
}
