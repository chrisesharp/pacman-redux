package pacman;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;
import pacman.core.PlayerStatus;
import pacman.core.Location;
import pacman.core.elements.GameElement;
import pacman.core.elements.TextElement;
import pacman.core.elements.Pill;
import pacman.core.terminal.GameKeyHandler.KeyEvent;

import java.util.List;

public class PacmanStatusTest {

    @Test
    public void defaultStatus() {
        PlayerStatus status = new PacmanStatus();
        assertThat(status.lives(), is(3));
        assertThat(status.score(), is(0));
        assertFalse(status.gameOver());
    }

    @Test
    public void scoreIncreasesByTenPoints() {
        int initialScore = 20;
        int points = 10;
        PlayerStatus status = new PacmanStatus(3, initialScore, 0);
        status.addScore(points);
        assertThat(status.score(), is(initialScore + points));
    }

    @Test
    public void gameOverWhenLivesAreZero() {
        int initialLives = 2;
        PlayerStatus status = new PacmanStatus(initialLives, 0, 0);
        status.loseLife();
        status.loseLife();
        assertThat(status.lives(), is(0));
        assertTrue(status.gameOver());
    }

    @Test
    public void gameOverWhenEscPressed() {
        int initialLives = 2;
        PacmanStatus status = new PacmanStatus(initialLives, 0, 0);
        status.keyPressed(KeyEvent.ESC);
        assertTrue(status.gameOver());
    }

    @Test
    public void gameNotOverWhenOtherKeyPressed() {
        int initialLives = 2;
        PacmanStatus status = new PacmanStatus(initialLives, 0, 0);
        status.keyPressed(KeyEvent.DOWN);
        assertFalse(status.gameOver());
    }

    @Test
    public void statusRendersCorrectly() {
        int initialLives = 2;
        int initialScore = 50;
        int initialColumns = 10;
        PacmanStatus status = new PacmanStatus(initialLives, initialScore, initialColumns);
        List<GameElement> elements = status.elements();
        TextElement lives = new TextElement(new Location(0,0), Integer.toString(initialLives));
        TextElement score = new TextElement(new Location(8,0), Integer.toString(initialScore));
        assertThat(elements.get(0).getIcon(), is(lives.getIcon()));
        assertThat(elements.get(0).getLocation(), is(lives.getLocation()));
        assertThat(elements.get(1).getIcon(), is(score.getIcon()));
        assertThat(elements.get(1).getLocation(), is(score.getLocation()));
    }

    @Test
    public void levelNotOverWhenResourcesPresent() {
        int initialLives = 2;
        PacmanStatus status = new PacmanStatus(initialLives, 0, 0);
        Pill pill = new Pill(new Location(0,0), null, status);
        status.addResource(pill);
        assertFalse(status.levelOver());
    }

    @Test
    public void levelOverWhenResourcesAllGone() {
        int initialLives = 2;
        PacmanStatus status = new PacmanStatus(initialLives, 0, 0);
        Pill pill = new Pill(new Location(0,0), null, status);
        status.addResource(pill);
        status.deleteResource(pill);
        assertTrue(status.levelOver());
    }
}