package pacman.core.elements;
import pacman.core.Location;
import pacman.core.Colour;
import pacman.core.Gateable;
import pacman.core.Collidable;
import pacman.core.CollisionEvent;

public class Gate extends CollidableElement implements CollisionEvent {
    private static final String ICON = "=";

    public Gate(Location location) {
        super(location, ICON);
        setColour(Colour.WHITE);
    }

    @Override
    public boolean isPassable(GameElement traveller) {
        if (traveller instanceof Gateable) {
            return ((Gateable) traveller).freeToPass(this);
        }
        return false;
    }

    public void accept(Collidable element) {
        element.collideWithGate(this);
    }

    @Override
    public void collideWithGhost(Ghost ghost) {
        ghost.passed(this);
    }

    public static boolean isGate(String icon) {
        return ICON.equals(icon);
    }
}
