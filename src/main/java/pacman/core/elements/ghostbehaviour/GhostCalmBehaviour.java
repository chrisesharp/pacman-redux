package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Ghost;
import pacman.core.elements.Pacman;
import pacman.core.Colour;

import java.util.Collection;
import java.util.Optional;

public abstract class GhostCalmBehaviour implements GhostBehaviour {
    private final Colour colour;
    private final Collection<Ghost> allGhosts;

    public GhostCalmBehaviour(Colour colour, Collection<Ghost> allGhosts) {
        this.colour = colour;
        this.allGhosts = allGhosts;
    }

    public void collide(Ghost ghost, Pacman pacman) {
        pacman.kill();
        allGhosts.stream().forEach(Ghost::resetLocation);
    }

    public Colour getColour() {
        return colour;
    }

    public boolean isSlow() {
        return false;
    }

    public String getIcon() {
        return "M";
    }
    public Optional<Ghost> redGhost() {
        return allGhosts.stream().filter((Ghost ghost) -> Colour.RED.equals(ghost.getColour())).findFirst();
    }
}