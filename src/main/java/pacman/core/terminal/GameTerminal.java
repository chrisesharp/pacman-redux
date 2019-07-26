package pacman.core.terminal;

import javax.swing.JFrame;

public interface GameTerminal extends Display, Keyboard {
    public void start();
    public void stop();
    public JFrame getSwingTerminalFrame();
}