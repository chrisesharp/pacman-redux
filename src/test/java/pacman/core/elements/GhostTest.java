package pacman.core.elements;

import org.junit.Test;
import static org.junit.Assert.*;

import pacman.PacmanMap;
import pacman.core.*;
import pacman.core.elements.ghostbehaviour.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GhostTest {

    private class MockMap implements GameMap {
        Collection<GameElement> elements;
        Collection<CollisionEvent> collidables;
        public MockMap(Collection<GameElement> elements) {
            this.elements = elements;
            this.collidables = Collections.emptySet();
        }

        public MockMap(Collection<GameElement> elements, Collection<CollisionEvent> collidables) {
            this.elements = elements;
            this.collidables = collidables;
        }

        @Override
        public void removeElement(GameElement element) {

        }

        @Override
        public Collection<GameElement> getElements(Location location) {
            return elements;
        }

        @Override
        public Collection<CollisionEvent> getCollidableElements(Location location) {
            Collection<CollisionEvent> all = new HashSet<CollisionEvent>();
            for (CollisionEvent thing: collidables) {
              all.add((CollisionEvent) thing);  
            }
            return all;
        }

        @Override
        public Location nextLocation(Location location, Function<Location, Location> movementTransform) {
            Location newLocation = movementTransform.apply(location);
            return newLocation;
        }

        @Override
        public void reset() {}

        @Override
        public int getColumns() { return 0;}

        @Override
        public int getRows() { return 0; }
    }

    private class MockCollidable implements Collidable, CollisionEvent {

        private boolean collided = false;

        public MockCollidable() {}

        public void accept(Collidable element) {
            collideWithGhost((Ghost)element);
        }
        public void kill() {}
        public void collideWithGhost(Ghost ghost) {
            collided = true;
        }
        public void collideWithPacman(Pacman pacman) {}
        public void collideWithPill(Pill pill) {}
        public void collideWithTunnel(Tunnel tunnel) {}
        public void collideWithGate(Gate element) {}
    }
    private class PassableElement extends GameElement {
        public PassableElement() {
            super(null, null);
        }

        @Override
        public boolean isPassable(GameElement element) {
            return true;
        }
    }

    private class MockBehaviour implements GhostBehaviour {
        public Location location;
        private boolean collideCalled = false;
        private boolean slow = false;

        @Override
        public Location targetLocation(Location ghostLocation) {
            return location;
        }

        @Override
        public void collide(Ghost ghost, Pacman pacman) {
            collideCalled = true;
        }

        @Override
        public Colour getColour() {
            return null;
        }

        @Override
        public boolean isSlow() {
            return slow;
        }

        @Override
        public String getIcon() {
            return null;
        }
    }

    @Test
    public void collidesWithCollidable() {
        MockCollidable mock = new MockCollidable();
        GameMap passableMap = new MockMap(Collections.singleton(new PassableElement()), Collections.singleton(mock));
        Ghost ghost = new Ghost(new Location(0, 0), "M", passableMap, new FixedLocationGhostBehaviour(new Location(0,0), null, null), null, null);
        ghost.tick();

        assertTrue(mock.collided);
    }

    @Test
    public void ghostCharacterHandlesCollisionsWithPacman() {
        Pacman pacman = new Pacman(new Location(0,0),"<", null, null);
        MockBehaviour mock= new MockBehaviour();
        Ghost ghost = new Ghost(new Location(0,0), "M", null, mock, null, null);

        ghost.collideWithPacman(pacman);

        assertThat(mock.collideCalled, is(true));
    }

    @Test
    public void panickedGhostIconPanicsCharacter() {
        Ghost ghost = new Ghost(new Location(5,0), "W", new MockMap(Collections.emptySet()), new FixedLocationGhostBehaviour(new Location(0, 0), Colour.RED, null), null, new FixedLocationGhostBehaviour(new Location(10, 0), Colour.BLUE, null));

        ghost.tick();

        assertThat(ghost.getColour(), is(Colour.BLUE));
        // assertThat(ghost.getLocation(), is(new Location(6, 0)));
    }

    @Test
    public void calmGhostMovesTowardsTarget() {
        Location target = new Location(0,0);
        Ghost ghost = new Ghost(new Location(1,0), "M", new MockMap(Collections.emptySet()), new FixedLocationGhostBehaviour(target, null, null), null, null);
        ghost.tick();
        assertThat(ghost.getLocation(), is(target));
    }
    @Test
    public void calmGhostChangesDirectionToMoveTowardsTarget() {
        Location target = new Location(1,1);
        Ghost ghost = new Ghost(new Location(1,0), "M", new MockMap(Collections.emptySet()), new FixedLocationGhostBehaviour(target, null, null), null, null);

        ghost.tick();

        assertThat(ghost.getLocation(), is(target));
    }
    @Test
    public void calmGhostTurnsBackWhenStuck() {
        Location target = new Location(0,1);
        PacmanMap map = new PacmanMap();
        map.addElement(new Wall(new Location(1, 1), "+"));
        map.addElement(new Wall(new Location(2, 0), "+"));
        map.addElement(new Wall(new Location(2, 2), "+"));
        // Map the map wide enough to move right!
        map.addElement(new Wall(new Location(3, 2), "+"));
        Ghost ghost = new Ghost(new Location(2,1), "M", map, new FixedLocationGhostBehaviour(target, null, null), null, null);

        ghost.tick();

        assertThat(ghost.getLocation(), is(new Location(3, 1)));
    }
    @Test
    public void cagedGhostOscillatesAvailablePositions() {
        Location target = new Location(0,0);
        PacmanMap map = new PacmanMap();
        // Map is as follows (target is x):
        // x
        //  +++++
        //  +M  +
        //  +++++
        map.addElement(new Wall(new Location(1, 1), "+"));
        map.addElement(new Wall(new Location(2, 0), "+"));
        map.addElement(new Wall(new Location(2, 2), "+"));
        map.addElement(new Wall(new Location(3, 2), "+"));
        map.addElement(new Wall(new Location(3, 0), "+"));
        map.addElement(new Wall(new Location(4, 2), "+"));
        map.addElement(new Wall(new Location(4, 0), "+"));
        map.addElement(new Wall(new Location(5, 1), "+"));
        Ghost ghost = new Ghost(new Location(2,1), "M", map, new FixedLocationGhostBehaviour(target, null, null), null, null);

        ghost.tick();
        assertThat(ghost.getLocation(), is(new Location(3, 1)));
        ghost.tick();
        assertThat(ghost.getLocation(), is(new Location(4, 1)));
        ghost.tick();
        assertThat(ghost.getLocation(), is(new Location(3, 1)));
        ghost.tick();
        assertThat(ghost.getLocation(), is(new Location(2, 1)));
        ghost.tick();
        assertThat(ghost.getLocation(), is(new Location(3, 1)));
        ghost.tick();
        assertThat(ghost.getLocation(), is(new Location(4, 1)));
    }

    @Test
    public void ghostStaysPanickedForFiftyTurns() {
        PacmanMap map = new PacmanMap();
        Ghost ghost = new Ghost(new Location(0,0), "M", map, new FixedLocationGhostBehaviour(new Location(0, 0), Colour.WHITE, null), null, new FixedLocationGhostBehaviour(new Location(50, 0), Colour.BLACK, null));
        map.addElement(ghost);
        map.addElement(new Wall(new Location(100, 100), "+"));

        ghost.panic();

        for (int i = 0 ; i < 50 ; i++) {
            assertThat(ghost.getColour(), is(Colour.BLACK));
            ghost.tick();
        }
        assertThat(ghost.getColour(), is(Colour.WHITE));
    }

    @Test
    public void slowGhostsOnlyMovesEveryOtherTick() {
        PacmanMap map = new PacmanMap();
        MockBehaviour mock = new MockBehaviour();
        mock.slow = true;
        mock.location = new Location(0,0);
        Ghost ghost = new Ghost(new Location(3,0), "M", map, mock, null, null);
        map.addElement(ghost);

        ghost.tick();
        ghost.tick();

        assertThat(ghost.getLocation(), is(new Location(2, 0)));

        ghost.tick();
        ghost.tick();

        assertThat(ghost.getLocation(), is(new Location(1, 0)));
    }

    @Test
    public void ghostInTunnelMovesEveryOtherTick() {
        PacmanMap map = new PacmanMap();
        Ghost ghost = new Ghost(new Location(3,0), "M", map, new FixedLocationGhostBehaviour(new Location(0,0), null, null), null, null);
        map.addElement(ghost);
        map.addElement(new Tunnel(new Location(3, 0)));
        map.addElement(new Tunnel(new Location(2, 0)));
        map.addElement(new Tunnel(new Location(1, 0)));
        map.addElement(new Tunnel(new Location(0, 0)));

        ghost.tick();
        ghost.tick();

        assertThat(ghost.getLocation(), is(new Location(2, 0)));

        ghost.tick();
        ghost.tick();

        assertThat(ghost.getLocation(), is(new Location(1, 0)));
    }

    @Test
    public void ghostStartsScatteringThenChaesAfterFortySevenTurns() {
        PacmanMap map = new PacmanMap();
        Ghost ghost = new Ghost(new Location(0, 80), "M", map,
                new FixedLocationGhostBehaviour(new Location(0, 0), Colour.BLUE, null),
                new FixedLocationGhostBehaviour(new Location(0,100), Colour.BLUE, null),
                new FixedLocationGhostBehaviour(new Location(50, 100), Colour.BLUE, null));
        map.addElement(ghost);
        map.addElement(new Wall(new Location(100, 100), "+"));

        for (int i = 0 ; i < 47 ; i++) {
            ghost.tick();
        }
        assertThat(ghost.getLocation(), is(new Location(0, 33)));

        ghost.tick();
        assertThat(ghost.getLocation(), is(new Location(0, 34)));
    }
}
