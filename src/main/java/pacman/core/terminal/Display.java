package pacman.core.terminal;

import pacman.core.Renderable;

public interface Display {
    void display(Renderable... elements);
}