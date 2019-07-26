package pacman.core.terminal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JFrame;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import pacman.core.Colour;
import pacman.core.terminal.GameKeyHandler.KeyEvent;
import pacman.core.Renderable;
import pacman.core.elements.GameElement;
import pacman.core.Location;

public class GameScreen implements GameTerminal, Runnable {
    private Screen screen;
    private SwingTerminalFrame terminal;
    private Thread listener;
    private volatile boolean running = true;

    private Collection<GameKeyHandler> keyHandlers = new HashSet<>();
    private static final TextCharacter DEFAULT = new TextCharacter(' ', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK);
    private static final Map<Colour,TextColor> COLOURMAP = new EnumMap<>(Colour.class);
    static {
        COLOURMAP.put(Colour.BLACK,     TextColor.ANSI.BLACK);
        COLOURMAP.put(Colour.RED,       TextColor.ANSI.RED);
        COLOURMAP.put(Colour.GREEN,     TextColor.ANSI.GREEN);
        COLOURMAP.put(Colour.YELLOW,    TextColor.ANSI.YELLOW);
        COLOURMAP.put(Colour.BLUE,      TextColor.ANSI.BLUE);
        COLOURMAP.put(Colour.CYAN,      TextColor.ANSI.CYAN);
        COLOURMAP.put(Colour.WHITE,     TextColor.ANSI.WHITE);
        COLOURMAP.put(Colour.MAGENTA,   TextColor.ANSI.MAGENTA);
        COLOURMAP.put(Colour.MAGENTA,   TextColor.ANSI.MAGENTA);
        COLOURMAP.put(Colour.DEFAULT,   TextColor.ANSI.WHITE);
    }
    private static final Map<Colour,SGR> EFFECTMAP = new EnumMap<>(Colour.class);
    static {
        EFFECTMAP.put(Colour.BLINK,     SGR.BLINK);
        EFFECTMAP.put(Colour.BLANK,     SGR.BORDERED);
        EFFECTMAP.put(Colour.REVERSE,   SGR.REVERSE);
        EFFECTMAP.put(Colour.DEFAULT,   SGR.BOLD);
    }

    private static final Map<KeyType, KeyEvent> KEYMAP = new EnumMap<>(KeyType.class);
    static {
        KEYMAP.put(KeyType.Escape,      KeyEvent.ESC);
        KEYMAP.put(KeyType.ArrowUp,     KeyEvent.UP);
        KEYMAP.put(KeyType.ArrowRight,  KeyEvent.RIGHT);
        KEYMAP.put(KeyType.ArrowDown,   KeyEvent.DOWN);
        KEYMAP.put(KeyType.ArrowLeft,   KeyEvent.LEFT);
        KEYMAP.put(KeyType.Character,   KeyEvent.CHR);
    }

    private static final Map<Character, KeyEvent> CHARMAP = new HashMap<>();
    static {
        CHARMAP.put('w', KeyEvent.UP);
        CHARMAP.put('d', KeyEvent.RIGHT);
        CHARMAP.put('s', KeyEvent.DOWN);
        CHARMAP.put('a', KeyEvent.LEFT);
    }

    public GameScreen(int rows, int cols) {
        terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(cols,rows)).createSwingTerminal();
        createScreen(terminal);
    }

    public GameScreen(InputStream in, OutputStream out, int rows, int cols) throws IOException {
        Terminal terminal = new DefaultTerminalFactory(out, in, Charset.defaultCharset())
                    .setInitialTerminalSize(new TerminalSize(cols,rows))
                    .createTerminal();
        createScreen(terminal);
    }

    protected void createScreen(Terminal terminal) {
        try {
            screen = new TerminalScreen(terminal, DEFAULT);
        } catch (IOException e) {
            System.out.println("Failed to create screen!");
            System.exit(-1);
        }
        listener = new Thread(this);
    }

    @Override
    public void run() {
        while (running) {
            getKeyPress();
        }
    }

    @Override
    public void display(Renderable... elements) {
        clear();
        int heightOffset = 0;
        for (Renderable element : elements) {
            heightOffset += render(heightOffset, element);
        }
        refresh();
    }

    public void addKeyHandler(GameKeyHandler keyHandler) {
        this.keyHandlers.add(keyHandler);
    }

    public void start() {
        try {
            screen.startScreen();
            screen.setCursorPosition(null);
            if (terminal != null) {
                terminal.setVisible(true);
            }
            screen.clear();
        }
        catch (IOException e) {
            System.out.println("Failed to open terminal!");
            System.exit(-1);
        }
        if (listener != null) {
            listener.start();
        } 
    }

    public void stop() {
        running = false;
        if (listener != null) {
            listener.interrupt();
        }
        try {
            screen.stopScreen();
        }
        catch (IOException e) {
            System.out.println("Failed to close terminal!");
            System.exit(-1);
        }
    }

    private void clear() {
        screen.clear();
    }

    private void refresh() {
        try {
            screen.refresh();
        }
        catch (IOException e) {
            System.out.println("Failed to refresh terminal!");
            System.exit(-1);
        }
    }

    public void getKeyPress() {
        try {
            KeyStroke key = screen.pollInput();
            if (key != null) {
                KeyType type = key.getKeyType();
                KeyEvent event = KEYMAP.get(type);
                if (event == KeyEvent.CHR) {
                    event = CHARMAP.get(key.getCharacter());                     
                }
                if (event != null) {
                    for (GameKeyHandler handler : keyHandlers) {
                        handler.keyPressed(event);
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("Failed to get input");
            System.exit(-1);
        }
    }

    private int render(int heightOffset, Renderable item) {
        return render(heightOffset, item.elements());
    }

    private int render(int heightOffset, Collection<GameElement> elements) {
        int height = 0;
        for (GameElement element : elements) {
            height = Math.max(element.getLocation().y + 1, height);
            setElement(heightOffset, element);
        }
        return height;
    }

    public void setElement(int heightOffset, GameElement element) {
        Location loc = element.getLocation();
        int x = loc.x;
        int y = heightOffset + loc.y;
        Colour colour = element.getColour();
        Colour effect = element.getEffect();
        TextColor fg = COLOURMAP.get(colour);
        TextColor bg = TextColor.ANSI.BLACK;
        List<SGR> sgrs = new ArrayList<>();
        sgrs.add((EFFECTMAP.get(effect)));
        for (char c: element.getIcon().toCharArray()) {
            screen.setCharacter(x, y, new TextCharacter(c, fg, bg, sgrs.toArray(new SGR[0])));
            x++;
        }
        if (element.playSound()) {
            terminal.bell();
        }
    }

    @Override
    public String toString() {
        return screen.toString();
    }

    public JFrame getSwingTerminalFrame() {
        return terminal;
    }
}