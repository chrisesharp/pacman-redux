package pacman.core.elements;

import pacman.core.Collidable;
import pacman.core.Location;

public class CollidableElement extends GameElement implements Collidable {
    public CollidableElement(Location location, String icon) {
        super(location, icon);
    }
    public void collideWithPacman(Pacman pacman) {
        // Override this if you care about Pacman
    }
    public void collideWithGhost(Ghost ghost) {
        // Override this if you care about Ghosts
    }
    public void collideWithPill(Pill pill) {
        // Override this if you care about Pills
    }
    public void collideWithGate(Gate gate) {
        // Override this if you care about Gates
    }
    public void collideWithTunnel(Tunnel tunnel) {
        // Override this if you care about Tunnels
    }
    public void kill() {
        // Override this if you can be killed
    }
    public void reset() {
        // Override this if you can be rest
    }
}