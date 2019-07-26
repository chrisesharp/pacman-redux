package pacman.core.elements;

import pacman.core.elements.ghostbehaviour.*;
import pacman.core.GameMap;
import pacman.core.Gateable;
import pacman.core.Location;
import pacman.core.Colour;
import pacman.core.Tickable;
import pacman.core.Collidable;
import pacman.core.CollisionEvent;
import pacman.core.Direction;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Comparator;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

public class Ghost extends CollidableElement implements Tickable, CollisionEvent, Gateable {
    private static final Collection<String> ghostTokens = Arrays.asList(
        "M",
        "W"
    );

    private static class BehaviourTiming {
        final int timing;
        final GhostBehaviour behaviour;

        public BehaviourTiming(int timing, GhostBehaviour behaviour) {
            this.timing = timing;
            this.behaviour = behaviour;
        }
    }

    private static class TimedBehaviour implements GhostBehaviour {
        private Stack<BehaviourTiming> strategies;
        private GhostBehaviour behaviour;
        private int ticksLeft;

        public TimedBehaviour(int totalTicks, GhostBehaviour behaviour) {
            strategies = new Stack<>();
            this.behaviour = behaviour;
            this.ticksLeft = totalTicks;
        }

        public void addBehaviour(int totalTicks, GhostBehaviour behaviour) {
            this.strategies.push(new BehaviourTiming(ticksLeft, this.behaviour));
            this.behaviour = behaviour;
            this.ticksLeft = totalTicks;
        }
        

        @Override
        public String getIcon() {
            return behaviour.getIcon();
        }

        @Override
        public Colour getColour() {
            return behaviour.getColour();
        }

        @Override
        public boolean isSlow() {
            return behaviour.isSlow();
        }

        @Override
        public Location targetLocation(Location ghostLocation) {
            return behaviour.targetLocation(ghostLocation);
        }

        @Override
        public void collide(Ghost ghost, Pacman pacman) {
            behaviour.collide(ghost, pacman);
        }

        public Direction nextDirection(Direction currentDirection) {
            Direction direction = currentDirection;
            ticksLeft--;
            if (ticksLeft <= 0) {
                direction = next(currentDirection);
            }
            return direction;
        }

        public Direction next(Direction curDirection) {
            BehaviourTiming strategy = strategies.pop();
            behaviour = strategy.behaviour;
            ticksLeft = strategy.timing;
            return curDirection.turnBack();
        }
    }

    private final GameMap map;
    private Direction direction;
    private final Location startingLocation;
    private boolean passedGate = false;
    protected boolean inTunnel = false;
    private boolean movedLastTick = false;
    private final GhostBehaviour panicBehaviour;
    private TimedBehaviour behaviour;

    private static final int POINTS = 200;

    public Ghost(Location location, GameMap map, GhostBehaviour scatterBehaviour, GhostBehaviour chaseBehaviour, GhostBehaviour panicBehaviour) {
        this(location, "M", map, scatterBehaviour, chaseBehaviour, panicBehaviour);
    }

    public Ghost(Location location, String icon, GameMap map, GhostBehaviour scatterBehaviour, GhostBehaviour chaseBehaviour, GhostBehaviour panicBehaviour) {
        super(location, icon);
        this.map = map;
        this.startingLocation = location;
        direction = Direction.LEFT;
        this.panicBehaviour = panicBehaviour;
        this.behaviour = newBehaviourStrategy(chaseBehaviour, scatterBehaviour);
        if ("W".equals(icon)) {
            panic();
        }
    }

    private TimedBehaviour newBehaviourStrategy(GhostBehaviour chase, GhostBehaviour scatter) {
        TimedBehaviour behaviourStrategy = new TimedBehaviour(Integer.MAX_VALUE, chase);
        behaviourStrategy.addBehaviour(33, scatter);
        behaviourStrategy.addBehaviour(133, chase);
        behaviourStrategy.addBehaviour(33, scatter);
        behaviourStrategy.addBehaviour(133, chase);
        behaviourStrategy.addBehaviour(47,scatter);
        behaviourStrategy.addBehaviour(133, chase);
        behaviourStrategy.addBehaviour(47, scatter);
        return behaviourStrategy;
    }
    public void tick() {
        boolean shouldMove = !isSlow() || !movedLastTick;
        if (shouldMove) {
            movedLastTick = true;
            chooseNextLocation();
        } else {
            movedLastTick = false;
        }
        checkForCollisions();
        direction = behaviour.nextDirection(direction);
    }

    private boolean isSlow() {
        return behaviour.isSlow() || inTunnel;
    }

    protected void chooseNextLocation() {
        Collection<Direction> options = gatherOptions();
        Comparator<Direction> distance = (a, b) -> {
            Location target = behaviour.targetLocation(getLocation());
            Location option1 = map.nextLocation(getLocation(), a.movement());
            Location option2 = map.nextLocation(getLocation(), b.movement());
            int dist = (int) round(option1.distance(target) - option2.distance(target));
            return max(-1, min(1, dist));
        };
        direction = options.stream().min(distance).orElse(direction.turnBack());
        if (canMoveTo(map.nextLocation(getLocation(), direction.movement()))) {
            move();
        }
    }

    private void move() {
        setLocation(map.nextLocation(getLocation(), direction.movement()));
    }

    private Collection<Direction> gatherOptions() {
        Collection<Direction> options = new ArrayList<>();
        Collection<Direction> possibleRoutes = Arrays.asList(direction,
                                                      direction.turnLeft(),
                                                      direction.turnRight());
        for (Direction dir: possibleRoutes) {
            Location nextLocation = map.nextLocation(getLocation(), dir.movement());
            if (canMoveTo(nextLocation)) {
                options.add(dir);
          }
        }
        return options;
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

    private void checkForCollisions() {
        inTunnel = false;
        for (CollisionEvent element: map.getCollidableElements(getLocation())) {
            element.accept(this);
        }
    }

    public void accept(Collidable element) {
        element.collideWithGhost(this);
    }

    @Override
    public void collideWithTunnel(Tunnel element) {
        inTunnel = true;
    }

    @Override
    public void collideWithGate(Gate gate) {
        passedGate = true;
    }

    @Override
    public void collideWithPacman(Pacman pacman) {
        behaviour.collide(this, pacman);
    }

    @Override
    public void kill() {
        direction = behaviour.next(direction);
        passedGate = false;
        reset();
    }

    @Override
    public void reset() {
        setLocation(startingLocation);
    }

    public int points() {
        return POINTS;
    }

    public void panic() {
        behaviour.addBehaviour(50, this.panicBehaviour);
    }

    public boolean freeToPass(Gate gate) {
        return !passedGate;
    }

    public void passed(Gate gate) {
        passedGate = true;
    }

    @Override
    public Colour getColour() {
        return behaviour.getColour();
    }

    @Override
    public String getIcon() {
        return behaviour.getIcon();
    }

    public static boolean isGhost(String icon) {
        return ghostTokens.contains(icon);
    }
    public void resetLocation() {
        this.setLocation(startingLocation);
        passedGate = false;
    }
}
