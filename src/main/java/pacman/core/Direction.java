package pacman.core;

import java.util.function.Function;

public enum Direction {
    LEFT(location -> new Location(location.x - 1, location.y)),
    UP(location -> new Location(location.x, location.y - 1)),
    RIGHT(location -> new Location(location.x + 1, location.y)),
    DOWN(location -> new Location(location.x, location.y + 1));
    
    private Function<Location, Location> movementTransform;
    private static Direction[] vals = values();
    
    private Direction(Function<Location, Location> movementTransform) {
        this.movementTransform = movementTransform;
    }

    public Direction turnRight() {
        return vals[(this.ordinal()+1) % vals.length];
    }
    public Direction turnLeft() {
        return vals[(this.ordinal()+3) % vals.length];
    }
    public Direction turnBack() {
        return vals[(this.ordinal()+2) % vals.length];
    }

    public Function<Location, Location> movement() {
        return movementTransform;
    }
}