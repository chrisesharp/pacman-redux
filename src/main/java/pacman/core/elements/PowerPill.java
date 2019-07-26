package pacman.core.elements;

import pacman.core.Location;
import pacman.core.Colour;
import pacman.core.GameMap;
import pacman.core.PlayerStatus;
import java.util.Collection;

public class PowerPill extends Pill {
    private final int POINTS = 50;
    private static final String ICON = "o";
    private final Collection<Ghost> ghosts;
    
    public PowerPill(Location location, GameMap map, PlayerStatus status, Collection<Ghost> ghosts) {
        super(location, ICON, map, status);
        setColour(Colour.WHITE);
        this.ghosts = ghosts;
    }

    @Override
    public Colour getEffect() {
        return Colour.BLINK;
    }

    @Override
    public int points() {
        return POINTS;
    }

    @Override
    public void collideWithPacman(Pacman pacman) {
        this.status.addScore(this.points());
        kill();
        for (Ghost ghost : ghosts) {
            ghost.panic();            
        }
    }

    public static boolean isPowerPill(String icon) {
        return ICON.equals(icon);
    }
}
