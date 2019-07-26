package pacman.core.elements;
import pacman.core.Location;
import pacman.core.Colour;
import java.util.List;
import java.util.Arrays;

public class Wall extends GameElement {
    private static final List<String> wallTokens = Arrays.asList(
        "+",
        "|",
        "-"
    );

    public Wall(Location location, String icon) {
        super(location, icon);
        setColour(Colour.BLUE);
    }

    @Override
    public boolean isPassable(GameElement element) {
        return false;
    }

    public static boolean isWall(String icon) {
        return wallTokens.contains(icon);
    }
}
