package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Pacman;
import pacman.core.elements.Ghost;
import pacman.core.Colour;
import pacman.core.Direction;
import pacman.core.Location;

import java.util.Collection;
import java.util.Optional;

public class TargetInFrontOfPacmanFollowingRedGhostBehaviour extends GhostCalmBehaviour {
    private final Pacman pacman;
    public TargetInFrontOfPacmanFollowingRedGhostBehaviour(Pacman pacman, Colour colour, Collection<Ghost> allGhosts) {
        super(colour, allGhosts);
        this.pacman = pacman;
    }

    public Location targetLocation(Location ghostLocation) {
        Location location = pacman.getLocation();
        Direction direction = pacman.getDirection();
        for (int i = 0 ; i < 2 ; i++) {
            location = direction.movement().apply(location);
        }
        Optional<Ghost> redGhost = redGhost();
        if (redGhost.isPresent()) {
            Location redGhostLocation = redGhost.get().getLocation();
            int targetX = redGhostLocation.x + 2*(location.x - redGhostLocation.x);
            int targetY = redGhostLocation.y + 2*(location.y - redGhostLocation.y);
            location = new Location(targetX, targetY);
        }
        return location;
    }
}
