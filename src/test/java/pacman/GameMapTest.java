package pacman;

import pacman.utils.MapParser;
import pacman.utils.PacmanMapFactory;
import pacman.core.*;
import pacman.core.elements.*;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.*;

import java.util.HashSet;
import org.junit.Test;
import java.net.URL;
import java.util.Collection;

public class GameMapTest {  
  @Test
  public void testBadFileReturnsEmptyString() {
    PacmanMapFactory factory = new PacmanMapFactory(MapParser.parse("bad-file"), null, new MockTerminal());
    PacmanMap map = factory.createMap();
    assertEquals(0, map.elements().size());
  }
  
  @Test
  public void testNoFileReturnsEmptyString() {
    PacmanMapFactory factory = new PacmanMapFactory(MapParser.parse(""), null, new MockTerminal());
    PacmanMap map = factory.createMap();
    assertEquals(0, map.elements().size());
  }
  
  @Test
  public void testDimensionsOfMap() {
    URL fileToRead = GameMapTest.class.getResource("/data/hello.txt");
    PacmanMapFactory factory = new PacmanMapFactory(MapParser.parse(fileToRead.getPath()), null, new MockTerminal());
    PacmanMap level = factory.createMap();
    assertEquals(5, level.getColumns());
  }

  @Test
  public void testGetTwoElements() {
    PacmanMap level = new PacmanMap();
    Location loc = new Location(1,1);
    Pacman pacman = new Pacman(loc, null, level, null);
    Ghost ghost = new Ghost(loc, "M", null, null, null, null);
    level.addElement(pacman);
    level.addElement(ghost);
    assertThat(level.getElements(loc), containsInAnyOrder(pacman, ghost));
  }

  @Test
  public void testGetTwoOutOfThreeElements() {
    PacmanMap level = new PacmanMap();
    Location loc = new Location(1,1);
    Pacman pacman = new Pacman(loc, null, level, null);
    Ghost ghost1 = new Ghost(loc, "M", null, null, null, null);
    Ghost ghost2 = new Ghost(new Location(1,2), "M", null, null, null, null);
    level.addElement(pacman);
    level.addElement(ghost1);
    level.addElement(ghost2);
    assertThat(level.getElements(loc), containsInAnyOrder(pacman, ghost1));
  }

  @Test
  public void testGetDoesntGetRemovedElements() {
    PacmanMap level = new PacmanMap();
    Location loc = new Location(1,1);
    Pacman pacman = new Pacman(loc, null, level, null);
    Ghost ghost = new Ghost(loc, "M", null, null, null, null);
    level.addElement(pacman);
    level.addElement(ghost);
    level.removeElement(ghost);
    assertThat(level.getElements(loc), containsInAnyOrder(pacman));
  }

  @Test
  public void mapRendersMovableElementAtLocation() {
    Location location = new Location(0,0);
    PacmanMap level = new PacmanMap();
    Pacman pacman = new Pacman(location, null, level, null);
    Pill pill1 = new Pill(location, null, null);
    Pill pill2 = new Pill(location, null, null);
    level.addElement(pill1);
    level.addElement(pacman);
    level.addElement(pill2);

    Collection<GameElement> renderedElements = level.elements();

    assertThat(renderedElements, hasSize(1));
    assertThat(renderedElements, contains(pacman));
  }

  @Test
  public void getCollidables() {
    PacmanMap map = new PacmanMap();
    Location loc = new Location(1,1);
    Pacman pacman = new Pacman(loc, null, map, null);
    Ghost ghost = new Ghost(loc, map, null, null, null);
    Pill pill = new Pill(loc, map, null);
    map.addElement(pacman);
    map.addElement(ghost);
    map.addElement(pill);

    Collection<CollisionEvent> expected = new HashSet<>();
    expected.add(ghost);
    expected.add(pacman);
    expected.add(pill);

    for (CollisionEvent element: map.getCollidableElements(loc) ) {
      assertTrue(expected.contains(element));
    }
  }
}