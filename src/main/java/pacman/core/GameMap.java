package pacman.core;

import java.util.Collection;
import java.util.function.UnaryOperator;

import pacman.core.elements.GameElement;

public interface GameMap {
    public void removeElement(GameElement element);
    public Collection<GameElement> getElements(Location location);
    public Collection<CollisionEvent> getCollidableElements(Location location);
    public Location nextLocation(Location location, UnaryOperator<Location> movement);
    public void reset();
    public int getRows();
    public int getColumns();
}