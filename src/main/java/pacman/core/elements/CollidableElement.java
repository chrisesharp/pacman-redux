package pacman.core.elements;

import pacman.core.Collidable;
import pacman.core.Location;

public class CollidableElement extends GameElement implements Collidable {
    public CollidableElement(Location location, String icon) {
        super(location, icon);
    }
    public void collideWithPacman(Pacman pacman) {}
    public void collideWithGhost(Ghost ghost) {}
    public void collideWithPill(Pill pill) {}
    public void collideWithGate(Gate gate) {}
    public void collideWithTunnel(Tunnel tunnel) {}
    public void kill() {}
    public void reset() {}
}