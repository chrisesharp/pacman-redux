package pacman.core;

import pacman.core.elements.Gate;

public interface Gateable {
    public boolean freeToPass(Gate gate);
    public void passed(Gate gate);
}