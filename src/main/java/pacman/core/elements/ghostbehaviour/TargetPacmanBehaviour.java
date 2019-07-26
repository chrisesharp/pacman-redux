package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Pacman;
import pacman.core.elements.Ghost;
import pacman.core.Colour;
import pacman.core.Location;

import java.util.Collection;

public class TargetPacmanBehaviour extends GhostCalmBehaviour {
    private final Pacman pacman;
    public TargetPacmanBehaviour(Pacman pacman, Colour colour, Collection<Ghost> allGhosts) {
        super(colour, allGhosts);
        this.pacman = pacman;
    }

    public Location targetLocation(Location ghostLocation) {
        return pacman.getLocation();
    }
}
