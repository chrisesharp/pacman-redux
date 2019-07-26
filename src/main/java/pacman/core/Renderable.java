package pacman.core;

import java.util.Collection;
import pacman.core.elements.GameElement;

public interface Renderable {
    public Collection<GameElement> elements();
}