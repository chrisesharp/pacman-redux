package pacman.core.elements;

import java.util.Collection;

import pacman.core.Collidable;
import pacman.core.CollisionEvent;
import pacman.core.Direction;
import pacman.core.Location;
import pacman.core.PlayerStatus;
import pacman.core.Tickable;
import pacman.core.GameMap;
import pacman.core.Colour;
import pacman.core.terminal.GameKeyHandler;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class Pacman extends CollidableElement implements GameKeyHandler, Tickable, CollisionEvent {

    private Direction direction;
    private final Location startingLocation;
    private final GameMap map;
    private final PlayerStatus status;
    private boolean sound = false;

    private static BiMap<Direction, String> ourIcons =
                    new ImmutableBiMap.Builder<Direction, String>()
                        .put(Direction.LEFT,">")
                        .put(Direction.RIGHT,"<")
                        .put(Direction.UP,"V")
                        .put(Direction.DOWN,"Λ")
                        .build();

    public Pacman(Location location, String icon, GameMap map, PlayerStatus status) {
        super(location, icon);
        direction = ourIcons.inverse().get(icon);
        this.startingLocation = location;
        setColour(Colour.YELLOW);
        this.map = map;
        this.status = status;
    }

    public void keyPressed(KeyEvent key) {
        try {
            Direction newDirection = Direction.valueOf(key.name());
            if (canMoveTo(map.nextLocation(getLocation(), newDirection))) {
                this.direction = newDirection;
            }
        } catch (IllegalArgumentException e) {
            // Not a movement key
        }
    }

    public void tick() {
        Location nextLocation = map.nextLocation(getLocation(), direction);
        if (canMoveTo(nextLocation)) {
            this.setLocation(nextLocation);
        }
        checkForCollisions();
    }

    private boolean canMoveTo(Location nextLocation) {
        Collection<GameElement> elements = map.getElements(nextLocation);
        for (GameElement element : elements) {
            if (!element.isPassable(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getIcon() {
        return ourIcons.get(direction);
    }

    public PlayerStatus status() {
        return status;
    }

    private void checkForCollisions() {
        for (CollisionEvent element: map.getCollidableElements(getLocation())) {
            element.accept(this);
        }
    }

    public void accept(Collidable element) {
        element.collideWithPacman(this);
    }

    @Override
    public void collideWithGhost(Ghost ghost) {
        ghost.collideWithPacman(this);
    }

    @Override
    public void collideWithPill(Pill pill) {
        pill.collideWithPacman(this);
    }

    @Override
    public void kill() {
        status.loseLife();
        reset();        
    }

    @Override
    public void reset() {
        sound = true;
        this.setLocation(startingLocation);
    }

    public void eat(Ghost ghost) {
        status.addScore(ghost.points());
        ghost.kill();
    }

    public static boolean isPacman(String icon) {
        return ourIcons.containsValue(icon);
    }

    @Override
    public boolean playSound() {
        if (sound) {
            sound = false;
            return true;
        }
        return false;
    }

    public Direction getDirection() {
        return direction;
    }
}
