package pacman.core.elements;

import pacman.core.Location;
import pacman.core.Colour;
import pacman.core.Collidable;
import pacman.core.CollisionEvent;
import pacman.core.GameMap;
import pacman.core.PlayerStatus;

public class Pill extends CollidableElement implements CollisionEvent {
    private final int POINTS = 10;
    private static final String ICON = ".";
    protected final GameMap map;
    protected final PlayerStatus status;
    
    public Pill(Location location, GameMap map, PlayerStatus status) {
        this(location, ICON, map, status);
    }
    
    protected Pill(Location location, String icon, GameMap map, PlayerStatus status) {
        super(location, icon);
        setColour(Colour.WHITE);
        this.map = map;
        this.status = status;
        if (status != null) {
            this.status.addResource(this);
        }
    }
    public static boolean isPill(String icon) {
        return ICON.equals(icon);
    }

    public int points() {
        return POINTS;
    }

    public void accept(Collidable element) {
        element.collideWithPill(this);
    }

    @Override
    public void collideWithPacman(Pacman pacman) {
        status.addScore(this.points());
        kill();
    }

    @Override
    public void kill() {
        map.removeElement(this);
        if (status != null) {
            status.deleteResource(this);
        }
    }
}
