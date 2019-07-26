package pacman;

import pacman.core.GameMap;
import pacman.core.PlayerStatus;
import pacman.core.Renderable;
import pacman.core.Tickable;
import pacman.core.terminal.GameTerminal;

public class PacmanEngine <S extends Renderable & PlayerStatus, M extends Renderable & Tickable & GameMap> {
  private final M map;
  private final S status;
  private final GameTerminal terminal;
  private final Renderable gameOverScreen;
  protected int FRAME_DELAY = 150;

  public PacmanEngine(S status, M map, GameTerminal terminal, Renderable gameOverScreen) {
    this.status = status;
    this.map = map;
    this.terminal = terminal;
    this.gameOverScreen = gameOverScreen;
  }

  public void play(int ticks) {
    terminal.start();
    while (!status.gameOver() && (ticks-- != 0)) {
      terminal.display(status, map);
      if (status.levelOver()) {
        map.reset();
      }
      map.tick();
      pause(FRAME_DELAY);
    }
    endGame();
  }

  private void pause(int delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) { 
      Thread.currentThread().interrupt();
    }
  }

  private void endGame() {
    terminal.display(status, gameOverScreen);
    pause(FRAME_DELAY * 15);
    terminal.stop();
  }
}
