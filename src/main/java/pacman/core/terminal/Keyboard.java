package pacman.core.terminal;

public interface Keyboard {
    public void addKeyHandler(GameKeyHandler keyHandler);
    public void getKeyPress();
}