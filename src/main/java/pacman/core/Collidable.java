package pacman.core;

import pacman.core.elements.Pacman;
import pacman.core.elements.Ghost;
import pacman.core.elements.Pill;
import pacman.core.elements.Tunnel;
import pacman.core.elements.Gate;

public interface Collidable {
    public void collideWithPacman(Pacman element);
    public void collideWithGhost(Ghost element);
    public void collideWithPill(Pill element);
    public void collideWithTunnel(Tunnel element);
    public void collideWithGate(Gate element);
    public void kill();
}