package pacman.core.terminal;

import pacman.core.elements.GameElement;
import pacman.core.elements.TextElement;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Ignore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import pacman.core.*;

public class GameScreenTest {
    class MockGame implements GameKeyHandler {
        KeyEvent key = KeyEvent.DOWN;

        public void keyPressed(KeyEvent key) {
            this.key = key;
        }
      }

    @Ignore
    @Test
    public void testOpenTerminal() {
        ByteArrayInputStream input = new ByteArrayInputStream("a".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String[] display;
        try {
            GameScreen screen = new GameScreen(input, output, 10, 10);
            screen.start();
            GameElement hello = new TextElement(new Location(0,0), "Hello");
            GameElement world = new TextElement(new Location(0,2), "World");
            final List<GameElement> elements = new ArrayList<>();
            elements.add(hello);
            elements.add(world);
            screen.display(() -> elements);
            screen.stop();
            display = screen.toString().split("\n"); 
        } catch (IOException e) {
            display = null;
        }
        assertEquals("Hello", display[1].substring(0, 5));
        assertEquals("World", display[3].substring(0, 5));
    }

    @Ignore
    @Test
    public void testTerminalKeypress() {
        ByteArrayInputStream input = new ByteArrayInputStream("a".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MockGame mock = new MockGame();
        try {
            GameScreen screen = new GameScreen(input, output, 10, 10);
            screen.addKeyHandler(mock);
            screen.start();
            screen.getKeyPress();
            screen.stop();
        } catch (IOException e) { }
        assertEquals(GameKeyHandler.KeyEvent.LEFT, mock.key);
    }
}