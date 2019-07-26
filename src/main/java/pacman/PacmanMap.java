package pacman;

import pacman.core.Location;
import pacman.core.Tickable;
import pacman.core.Renderable;
import pacman.core.CollisionEvent;
import pacman.core.GameMap;

import pacman.core.elements.GameElement;
import pacman.utils.PacmanMapFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.HashSet;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PacmanMap implements Renderable, Tickable, GameMap {
    private final Collection<GameElement> elements = new HashSet<>();
    private final PacmanMapFactory factory;

    public PacmanMap(PacmanMapFactory factory) {
        this.factory = factory;
    }

    public PacmanMap() {
        this.factory = null;
    }

    public void removeElement(GameElement element) {
        elements.remove(element);
    }

    public void addElement(GameElement element) {
        elements.add(element);
    }

    public Collection<GameElement> getElements(Location location) {
        return elements.stream()
            .filter(element -> element.getLocation().equals(location))
            .collect(Collectors.toSet());
    }

    public Collection<CollisionEvent> getCollidableElements(Location location) {
        return elements.stream()
            .filter(element -> element.getLocation().equals(location))
            .filter(element -> element instanceof CollisionEvent)
            .map(element -> (CollisionEvent) element )
            .collect(Collectors.toSet());
    }

    public void tick() {
        Collection<Tickable> tickables = elements.stream()
                                            .filter(element -> element instanceof Tickable)
                                            .map(element -> (Tickable)element)
                                            .collect(Collectors.toSet());
        for (Tickable element : tickables) {
            element.tick();
        }
    }

    public Collection<GameElement> elements() {
        BinaryOperator<GameElement> pickTickable = (a, b) -> (a instanceof Tickable) ? a : b;
        return elements.stream()
                .collect(Collectors.groupingBy(GameElement::getLocation))
                    .entrySet().stream()
                    .map(entry -> entry.getValue().stream().reduce(null, pickTickable))
                    .collect(Collectors.toList());
    }

    protected Collection<GameElement> getAllElements() {
        return elements.stream().collect(Collectors.toList());
    }

    public int getColumns() {
        Comparator<GameElement> horizontal = (a, b) -> a.getLocation().x - b.getLocation().x;
        Optional<GameElement> max = elements.stream().max(horizontal);
        return max.isPresent() ? max.get().getLocation().x + 1 : 0;
    }

    public int getRows() {
        Comparator<GameElement> vertical = (a, b) -> a.getLocation().y - b.getLocation().y;
        Optional<GameElement> max = elements.stream().max(vertical);
        return max.isPresent() ? max.get().getLocation().y + 1 : 0;
    }

    public void reset() {
        elements.clear();
        if (factory != null) {
            factory.populateMap(this);
        }
    }

    public Location nextLocation(Location location, Function<Location, Location> movementTransform) {
        Location newLocation = movementTransform.apply(location);
        return wrapped(newLocation);
    }

    private Location wrapped(Location loc) {
    return new Location(((loc.x + getColumns()) % getColumns()), loc.y);
    }
}