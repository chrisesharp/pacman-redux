package pacman.core.terminal;

public interface GameKeyHandler {
    public enum KeyEvent {
        UP, RIGHT, DOWN, LEFT, ESC, CHR;
    }
    
    public void keyPressed(KeyEvent key);
}