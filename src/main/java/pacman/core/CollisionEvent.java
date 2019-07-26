package pacman.core;

public interface CollisionEvent {
    public void accept(Collidable element);
}