package pacman;

import pacman.core.PlayerStatus;
import pacman.core.Location;
import pacman.core.Renderable;
import pacman.core.elements.GameElement;
import pacman.core.elements.TextElement;
import pacman.core.terminal.GameKeyHandler;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class PacmanStatus implements PlayerStatus, Renderable, GameKeyHandler {
    private int lives;
    private int score;
    private int columns;
    private boolean gameOver = false;
    private Collection<GameElement> levelResources = new HashSet<>();

    public PacmanStatus() {
        lives = 3;
        score = 0;
        columns = 5;
    }

    public PacmanStatus(int lives, int score, int columns) {
        this.lives = lives;
        this.score = score;
        this.columns = columns;
    }

    public void loseLife() {
        lives--;
        if (lives == 0) {
            gameOver = true;
        }
    }

    public void addScore(int points) {
        score += points;
    }

    public int lives() {
        return lives;
    }

    public int score() {
        return score;
    }

    public boolean gameOver() {
        return gameOver;
    }

    private void endGame() {
        gameOver = true;
    }

    public List<GameElement> elements() {
        final String LIVES = ""+lives;
        final String SCORE = ""+score;
        final int score_pos = columns - LIVES.length() - SCORE.length() + 1;
        List<GameElement> elements = new ArrayList<>();
        elements.add(new TextElement(new Location(0, 0), LIVES));
        elements.add(new TextElement(new Location(score_pos, 0), SCORE));
        return elements;
    }

    @Override
    public void keyPressed(KeyEvent key) {
        if (key == KeyEvent.ESC) {
            endGame();
        }
    }

    public void addResource(GameElement element) {
        levelResources.add(element);
    }
    
    public void deleteResource(GameElement element) {
        levelResources.remove(element);
    }

    public boolean levelOver() {
        return levelResources.isEmpty();
    }
}