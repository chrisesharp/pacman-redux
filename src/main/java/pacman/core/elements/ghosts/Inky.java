package pacman.core.elements.ghosts;

import pacman.core.elements.Ghost;
import pacman.core.elements.Pacman;
import pacman.core.Location;
import pacman.core.Colour;
import pacman.core.GameMap;
import pacman.core.elements.ghostbehaviour.*;
import java.util.Collection;

public final class Inky extends Ghost {
    public Inky(Location location, String icon, GameMap map, Pacman pacman, Collection<Ghost> ghosts)  {
        super(location,
            icon, 
            map, 
            new FixedLocationGhostBehaviour(new Location(map.getRows(), 0), Colour.CYAN, ghosts),
            new TargetInFrontOfPacmanFollowingRedGhostBehaviour(pacman, Colour.CYAN, ghosts),
            new GhostPanicBehaviour());
    }  
}