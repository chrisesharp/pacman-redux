package pacman;

import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import pacman.core.Renderable;
import pacman.core.terminal.GameKeyHandler;
import pacman.core.terminal.GameTerminal;

class MockTerminal implements GameTerminal {
    public void display(Renderable... stuff) {}
    public void getKeyPress() {}
    public void start() {}
    public void addKeyHandler(GameKeyHandler handler) {}
    public void stop() {}
    public SwingTerminalFrame getSwingTerminalFrame() { return null;}
}
