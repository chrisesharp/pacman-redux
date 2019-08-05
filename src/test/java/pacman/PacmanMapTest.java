package pacman;

import pacman.core.Collidable;
import pacman.core.CollisionEvent;
import pacman.core.Location;
import pacman.core.Direction;
import pacman.core.Tickable;
import pacman.core.elements.GameElement;
import pacman.core.elements.CollidableElement;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;
import java.util.Arrays;

public class PacmanMapTest {
    private class MockStaticElement extends GameElement {
        public boolean ticked = false;

        public MockStaticElement(Location loc) {
            super(loc, null);
        }
    }

    private class MockElement extends GameElement implements Tickable {
        public boolean ticked = false;

        public MockElement(Location loc) {
            super(loc, null);
        }

        public void tick() {
            ticked = true;
        }
    }

    private class MockCollidableElement extends CollidableElement implements CollisionEvent {
        public MockCollidableElement(Location loc) {
            super(loc, null);
        }

        public void accept(Collidable element) {
            
        }
    }

    @Test
    public void test_new_map_is_empty() {
        PacmanMap map = new PacmanMap();
        assertTrue(map.elements().isEmpty());
        assertThat(map.getColumns(), is(0));
        assertThat(map.getRows(), is(0));
        assertTrue(map.getElements(new Location(0,0)).isEmpty());
    }

    @Test
    public void test_tick_propagates() {
        MockElement element1 = new MockElement(new Location(1,1));
        MockStaticElement element2 = new MockStaticElement(new Location(2,1));
        PacmanMap map = new PacmanMap();
        map.addElement(element1);
        map.addElement(element2);
        map.tick();
        assertTrue(element1.ticked);
        assertFalse(element2.ticked);
    }

    @Test
    public void test_dimensions_of_smallest_map() {
        MockElement element = new MockElement(new Location(0,0));
        PacmanMap map = new PacmanMap();
        map.addElement(element);
        assertThat(map.getColumns(), is(1));
        assertThat(map.getRows(), is(1));
    }

    @Test
    public void test_dimensions_of_larger_map() {
        MockElement element1 = new MockElement(new Location(1,1));
        MockElement element2 = new MockElement(new Location(5,4));
        PacmanMap map = new PacmanMap();
        map.addElement(element1);
        map.addElement(element2);
        assertThat(map.getColumns(), is(6));
        assertThat(map.getRows(), is(5));
    }

    @Test
    public void test_all_elements_of_map() {
        MockElement element = new MockElement(new Location(5,4));
        PacmanMap map = new PacmanMap();
        map.addElement(element);
        assertTrue(map.getAllElements().contains(element));
    }

    @Test
    public void test_collidable_elements_of_map() {
        Location location = new Location(5,4);
        GameElement element1 = new MockElement(location);
        GameElement element2 = new MockCollidableElement(location);
        GameElement element3 = new MockStaticElement(location);
        PacmanMap map = new PacmanMap();
        map.addElement(element1);
        map.addElement(element2);
        map.addElement(element3);
        Collection<CollisionEvent> result = map.getCollidableElements(location);
        assertFalse(result.contains(element1));
        assertTrue(result.contains(element2));
        assertFalse(result.contains(element3));
        assertTrue(map.getElements(new Location(0,0)).isEmpty());
    }

    @Test
    public void test_elements_are_removed_from_map() {
        Location location = new Location(5,4);
        GameElement element = new MockElement(location);
        PacmanMap map = new PacmanMap();
        map.addElement(element);
        map.removeElement(element);
        assertTrue(map.getCollidableElements(location).isEmpty());
    }

    @Test
    public void test_tickable_elements_rather_than_static_elements_to_display() {
        GameElement element1 = new MockElement(new Location(1,4));
        GameElement element2 = new MockStaticElement(new Location(1,4));
        GameElement element3 = new MockCollidableElement(new Location(2,4));
        GameElement element4 = new MockStaticElement(new Location(3,4));
        PacmanMap map = new PacmanMap();
        map.addElement(element1);
        map.addElement(element2);
        map.addElement(element3);
        map.addElement(element4);
        Collection<GameElement> result = map.elements();
        assertTrue(result.containsAll(Arrays.asList(element1, element3, element4)));
    }

    @Test
    public void test_next_location_on_map() {
        GameElement element = new MockElement(new Location(4,4));
        PacmanMap map = new PacmanMap();
        map.addElement(element);
        assertThat(map.nextLocation(new Location(2,1), Direction.LEFT), is(new Location(1,1)));
    }
}