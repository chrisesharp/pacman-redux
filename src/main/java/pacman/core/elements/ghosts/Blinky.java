package pacman.core.elements.ghosts;

import pacman.core.elements.Ghost;
import pacman.core.elements.Pacman;
import pacman.core.Location;
import pacman.core.Colour;
import pacman.core.GameMap;
import pacman.core.elements.ghostbehaviour.*;
import java.util.Collection;

public final class Blinky extends Ghost {
    public Blinky(Location location, String icon, GameMap map, Pacman pacman, Collection<Ghost> ghosts)  {
        super(location,
            icon, 
            map, 
            new FixedLocationGhostBehaviour(new Location(0, 0), Colour.RED, ghosts),
            new TargetPacmanBehaviour(pacman, Colour.RED, ghosts),
            new GhostPanicBehaviour());
    }  
}