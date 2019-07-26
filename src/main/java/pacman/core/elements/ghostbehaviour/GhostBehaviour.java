package pacman.core.elements.ghostbehaviour;

import pacman.core.elements.Pacman;
import pacman.core.elements.Ghost;
import pacman.core.Location;
import pacman.core.Colour;

public interface GhostBehaviour {
    public Location targetLocation(Location ghostLocation);
    public void collide(Ghost ghost, Pacman pacman);
    public Colour getColour();
    public boolean isSlow();
    public String getIcon();
}