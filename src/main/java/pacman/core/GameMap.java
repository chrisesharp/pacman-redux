package pacman.core;

import java.util.Collection;

import pacman.core.elements.GameElement;

public interface GameMap {
    public void removeElement(GameElement element);
    public Collection<GameElement> getElements(Location location);
    public Collection<CollisionEvent> getCollidableElements(Location location);
    public Location nextLocation(Location location, Direction direction);
    public void reset();
    public int getRows();
    public int getColumns();
}