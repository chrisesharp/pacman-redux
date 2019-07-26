package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Pacman;
import pacman.core.elements.Ghost;
import pacman.core.Colour;
import pacman.core.Location;

import java.util.Collection;

public class TargetPacmanUntilCloseBehaviour extends GhostCalmBehaviour {
    private final Pacman pacman;
    private final Location runAwayLocation;
    public TargetPacmanUntilCloseBehaviour(Pacman pacman, Location runAwayLocation, Colour colour, Collection<Ghost> allGhosts) {
        super(colour, allGhosts);
        this.pacman = pacman;
        this.runAwayLocation = runAwayLocation;
    }

    @Override
    public Location targetLocation(Location ghostLocation) {
        if (8.0 > ghostLocation.distance(pacman.getLocation())) {
            return runAwayLocation;
        } else {
            return pacman.getLocation();
        }
    }
}
