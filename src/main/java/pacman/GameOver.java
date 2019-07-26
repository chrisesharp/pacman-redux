package pacman;

import pacman.core.Renderable;
import pacman.core.Location;
import pacman.core.elements.GameElement;
import pacman.core.elements.TextElement;
import java.util.Collection;

public class GameOver implements Renderable {
    private final PacmanMap map;
    private final int midPtX;
    private final int midPtY;
    private static final String GAME = "GAME";
    private static final String OVER = "OVER";

    public GameOver(PacmanMap map) {
        this.map = map;
        midPtX = (int) map.getColumns() / 2;
        midPtY = (int) Math.floor( map.getRows() / 2 );
    }

    public Collection<GameElement> elements() {
        Collection<GameElement> screen = map.elements();
        screen.add(textElement(midPtY, GAME));
        screen.add(textElement(midPtY + 1, OVER));
        return screen;
    }

    private GameElement textElement(int y, String text) {
        int x = midPtX - text.length() / 2;
        return new TextElement(new Location(x, y), text);
    }
}