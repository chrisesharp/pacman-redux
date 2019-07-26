package pacman.core;

import java.util.function.UnaryOperator;

public enum Direction {
    LEFT(location -> new Location(location.x - 1, location.y)),
    UP(location -> new Location(location.x, location.y - 1)),
    RIGHT(location -> new Location(location.x + 1, location.y)),
    DOWN(location -> new Location(location.x, location.y + 1));
    
    private UnaryOperator<Location> movementTransform;
    private static Direction[] vals = values();
    
    private Direction(UnaryOperator<Location> movementTransform) {
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

    public UnaryOperator<Location> movement() {
        return movementTransform;
    }
}