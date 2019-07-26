package pacman.core.elements;

import org.junit.Test;
import static org.junit.Assert.*;

import pacman.core.Collidable;
import pacman.core.CollisionEvent;
import pacman.core.GameMap;
import pacman.core.Location;
import pacman.core.terminal.GameKeyHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PacmanTest {

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
            Collection<CollisionEvent> all = new HashSet<>();
            for (CollisionEvent thing: collidables) {
              all.add(thing);  
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

    private class MockCollidable extends Pill implements Collidable, CollisionEvent {
        private boolean collided = false;
        public MockCollidable() {
            super(null, null, null);
        }
        public void kill() {

        }
        public void accept(Collidable element) {
            element.collideWithPill((Pill)this);
        }
        public void collideWithPacman(Pacman pacman) {
            collided = true;
        }
        public void collideWithGhost(Ghost ghost) {}
        public void collideWithPill(Pill pill) {}
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
    private class UnpassableElement extends GameElement {
        public UnpassableElement() {
            super(null, null);
        }

        @Override
        public boolean isPassable(GameElement element) {
            return false;
        }
    }
    @Test
    public void directionChangesOnKeyPressIfNewDirectionIsPassable() {
        GameMap passableMap = new MockMap(Collections.singleton(new PassableElement()));
        Pacman testObject = new Pacman(new Location(0, 0), "<", passableMap, null);

        testObject.keyPressed(GameKeyHandler.KeyEvent.LEFT);

        assertThat(testObject.getIcon(), is(">"));
    }
    @Test
    public void directionDoesNotChangesOnKeyPressIfNewDirectionIsNotPassable() {
        GameMap passableMap = new MockMap(Collections.singleton(new UnpassableElement()));
        Pacman testObject = new Pacman(new Location(0, 0), "<", passableMap, null);

        testObject.keyPressed(GameKeyHandler.KeyEvent.LEFT);

        assertThat(testObject.getIcon(), is("<"));
    }
    @Test
    public void movesWhenPassable() {
        GameMap passableMap = new MockMap(Collections.singleton(new PassableElement()));
        Pacman testObject = new Pacman(new Location(0, 0), "<", passableMap, null);

        testObject.tick();

        assertThat(testObject.getLocation(), is(new Location(1, 0)));
    }
    @Test
    public void stopsWhenBlocked() {
        GameMap passableMap = new MockMap(Collections.singleton(new UnpassableElement()));
        Pacman testObject = new Pacman(new Location(0, 0), "<", passableMap, null);

        testObject.tick();

        assertThat(testObject.getLocation(), is(new Location(0, 0)));
    }

    @Test
    public void collidesWithCollidable() {
        MockCollidable mock = new MockCollidable();
        GameMap passableMap = new MockMap(Collections.singleton(new PassableElement()), Collections.singleton(mock));
        Pacman pacman = new Pacman(new Location(0, 0), "<", passableMap, null);
        pacman.tick();

        assertTrue(mock.collided);
    }
}
