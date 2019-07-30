package pacman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import pacman.core.Renderable;
import pacman.core.terminal.GameScreen;
import pacman.core.terminal.GameTerminal;

import pacman.utils.ArgParser;
import pacman.utils.MapElements;
import pacman.utils.MapParser;
import pacman.utils.PacmanMapFactory;

public final class PacmanGame {
    private static final Logger log = Logger.getLogger(PacmanGame.class);
    private PacmanEngine<PacmanStatus,PacmanMap> theGame;
    private GameTerminal terminal;

    protected PacmanGame(String filename) {
        this(filename, null, null);
    }

    protected PacmanGame(String filename, InputStream in, OutputStream out) {
        MapElements mapElements = MapParser.parseFile(filename);
        PacmanStatus status = new PacmanStatus(3, 0, mapElements.width);
        if (in == null || out == null) {
            terminal = new GameScreen(mapElements.height + 1, mapElements.width);
        } else {
            try {
                terminal = new GameScreen(in, out, mapElements.height + 1, mapElements.width);
            } catch (IOException e) {
                log.error(e);
                System.exit(-1);
            }
        }
        createEngine(mapElements, status, terminal);
    }

    private void createEngine(MapElements mapElements, PacmanStatus status, GameTerminal terminal) {
        terminal.addKeyHandler(status);
        PacmanMapFactory factory = new PacmanMapFactory(mapElements, status, terminal);
        PacmanMap map = factory.createMap();
        Renderable gameOverScreen = new GameOver(map);
        theGame = new PacmanEngine<>(status, map, terminal, gameOverScreen);
    }

    protected void run(int ticks) {
        theGame.play(ticks);
    }

    @Override
    public String toString() {
        return terminal.toString();
    }

    public static void main(String[] args) {
        ArgParser parser = new ArgParser(args);
        String file = (parser.hasMapFile()) ? parser.getMapFile() : "target/classes/data/pacman.txt";
        int ticks = parser.getTicks();
        PacmanGame game = new PacmanGame(file);
        game.run(ticks);
    }
}
