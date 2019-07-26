package pacman;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import pacman.core.PlayerStatus;
import pacman.core.Renderable;
import pacman.core.elements.GameElement;
import pacman.core.terminal.GameTerminal;
import pacman.core.terminal.GameKeyHandler;
import javax.swing.JFrame;

public class PacmanEngineTest {
    private class MockTerminal implements GameTerminal {
        public boolean started = false;
        public boolean stopped = false;
        @Override
        public void display(Renderable... elements) {

        }

        @Override
        public void addKeyHandler(GameKeyHandler keyHandler) {

        }

        @Override
        public void getKeyPress() {

        }

        @Override
        public void start() {
            started = true;
        }

        @Override
        public void stop() {
            stopped = true;
        }

		@Override
		public JFrame getSwingTerminalFrame() {
			return null;
		}
    }

    private class MockStatus implements PlayerStatus, Renderable {
        public boolean gameOver = false;
        public int ticksUsed = -1;
        @Override
        public void addScore(int points) {

        }

        @Override
        public void loseLife() {

        }

        @Override
        public boolean gameOver() {
            ticksUsed++;
            return gameOver;
        }

        @Override
        public boolean levelOver() {
            return false;
        }

        @Override
        public void addResource(GameElement element) {

        }

        @Override
        public void deleteResource(GameElement element) {

        }

        @Override
        public int lives() {
            return 0;
        }

        @Override
        public int score() {
            return 0;
        }

        @Override
        public Collection<GameElement> elements() {
			return null;
		}

    }

    @Test
    public void testEngineStopsAfterTicksZero() {
        MockStatus status = new MockStatus();
        MockTerminal terminal = new MockTerminal();
        PacmanMap map = new PacmanMap();
        Renderable mockScreen = null;
        PacmanEngine<MockStatus,PacmanMap> engine = new PacmanEngine<>(status, map, terminal, mockScreen);
        engine.FRAMEDELAY = 0;
        engine.play(3);
        assertTrue(terminal.started);
        assertTrue(terminal.stopped);
        assertThat(status.ticksUsed, is(3));
    }

    @Test
    public void testEngineStopsAfterGameOver() {
        MockStatus status = new MockStatus();
        status.gameOver = true;
        MockTerminal terminal = new MockTerminal();
        PacmanMap map = new PacmanMap();
        Renderable mockScreen = null;
        PacmanEngine<MockStatus,PacmanMap> engine = new PacmanEngine<>(status, map, terminal, mockScreen);
        engine.FRAMEDELAY = 0;
        engine.play(3);
        assertTrue(terminal.started);
        assertTrue(terminal.stopped);
        assertThat(status.ticksUsed, is(0));
    }
}