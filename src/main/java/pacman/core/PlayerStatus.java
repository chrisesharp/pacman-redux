package pacman.core;

import pacman.core.elements.GameElement;

public interface PlayerStatus {
    void addScore(int points);
    void loseLife();
    boolean gameOver();
    boolean levelOver();
    void addResource(GameElement element);
    void deleteResource(GameElement element);
    int lives();
    int score();
}