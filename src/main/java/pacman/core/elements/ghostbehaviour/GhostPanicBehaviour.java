package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Pacman;
import pacman.core.elements.Ghost;
import pacman.core.Location;
import pacman.core.Colour;

import java.util.Random;

public class GhostPanicBehaviour implements GhostBehaviour {
    private final Random random = new Random();

    public Location targetLocation(Location ghostLocation) {
        return new Location(random.nextInt(), random.nextInt());
    }

    public void collide(Ghost ghost, Pacman pacman) {
        pacman.eat(ghost);
    }
    public Colour getColour() {
        return Colour.BLUE;
    }
    public boolean isSlow() {
        return true;
    }
    public String getIcon() {
        return "W";
    }
}