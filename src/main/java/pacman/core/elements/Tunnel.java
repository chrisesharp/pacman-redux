package pacman.core.elements;

import pacman.core.Location;
import pacman.core.Collidable;
import pacman.core.CollisionEvent;
import pacman.core.Colour;

public class Tunnel extends CollidableElement implements CollisionEvent {
    private static final String ICON = "#";

    public Tunnel(Location location) {
        super(location, ICON);
        setColour(Colour.BLACK);
    }
    
    @Override
    public Colour getEffect() {
        return Colour.BLANK;
    }

    public void accept(Collidable element) {
        element.collideWithTunnel(this);
    }

    public static boolean isTunnel(String icon) {
        return ICON.equals(icon);
    }
}
