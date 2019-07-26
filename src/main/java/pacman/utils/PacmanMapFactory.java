package pacman.utils;

import pacman.PacmanMap;
import pacman.PacmanStatus;
import pacman.core.PlayerStatus;
import pacman.core.Location;
import pacman.core.GameMap;
import pacman.core.elements.ghosts.*;
import pacman.core.elements.*;
import pacman.core.terminal.Keyboard;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.lang.reflect.Constructor;


public class PacmanMapFactory {
  private final MapElements mapElements;
  private final PacmanStatus status;
  private final Keyboard terminal;

  private static final List<Class<? extends Ghost>> ghostCharacterClasses = new ArrayList<> (
    Arrays.asList(
      Blinky.class,
      Pinky.class,
      Inky.class,
      Clyde.class
    )
  );

  public PacmanMapFactory(MapElements mapElements, PacmanStatus status, Keyboard terminal) {
    this.mapElements = mapElements;
    this.status = status;
    this.terminal = terminal;
  }

  public PacmanMap createMap() {
      PacmanMap map = new PacmanMap(this);
      populateMap(mapElements, status, terminal, map);
      return map;
  }

  public void populateMap(PacmanMap map) {
    populateMap(mapElements, status, terminal, map);
  }

  private void populateMap(MapElements mapElements, PacmanStatus status, Keyboard terminal, PacmanMap map) {
      Collection<Ghost> ghosts = new HashSet<>();
      Iterator<Class<? extends Ghost>> ghostClasses = ghostCharacterClasses.iterator();

      addElements(mapElements.walls, mapElement -> new Wall(mapElement.location, mapElement.icon), map);

      Pacman pacman = newPacman(mapElements, map, status);
      terminal.addKeyHandler(pacman);

      addElements(mapElements.gates, mapElement -> new Gate(mapElement.location), map);
      addElements(mapElements.tunnels, mapElement -> new Tunnel(mapElement.location), map);
      addElements(mapElements.pills, mapElement -> new Pill(mapElement.location, map, status), map);
      addElements(mapElements.ghosts, mapElement -> newGhost(ghostClasses.next(), mapElement.location, mapElement.icon, map, pacman, ghosts), map);
      addElements(mapElements.powerPills, mapElement -> new PowerPill(mapElement.location, map, status, ghosts), map);
  }

  private static Pacman newPacman(MapElements mapElements, PacmanMap map, PlayerStatus status) {
    Pacman pacman;
    if (!mapElements.pacman.isEmpty()) {
      MapElements.MapElement pacmanElement = mapElements.pacman.iterator().next();
      pacman = new Pacman(pacmanElement.location, pacmanElement.icon, map, status);
      map.addElement(pacman);
    } else {
      pacman = null;
    }
    return pacman;
  }

  private static Ghost newGhost(Class<? extends Ghost> ghostClass, Location location, String icon, GameMap map, Pacman pacman, Collection<Ghost> ghosts) {
        Ghost ghost;
        try {
          Constructor<? extends Ghost> constructor = ghostClass.getConstructor(Location.class, String.class, GameMap.class, Pacman.class, Collection.class);
          ghost = constructor.newInstance(
            Arrays.asList(location, icon, map, pacman, ghosts).toArray()
          );
          ghosts.add(ghost);
        } catch (Exception e) {
          ghost = null;
        }
        return ghost;
  }
  private static void addElements(Collection<MapElements.MapElement> mapElements, Function<MapElements.MapElement, GameElement> mapper, PacmanMap map) {
      mapElements.stream()
              .map(mapper)
              .forEach(map::addElement);
  }
}