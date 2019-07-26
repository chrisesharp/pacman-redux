package pacman.core.elements;

import org.junit.Test;
import pacman.core.Colour;
import pacman.core.Location;
import pacman.core.elements.ghostbehaviour.*;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TargetPacmanBehaviourTest {
    @Test
    public void targetLocationIsPacmansLocation() {
        Location pacmanLocation = new Location(1, 1);
        Pacman pacman = new Pacman(pacmanLocation, "<", null, null);
        TargetPacmanBehaviour testObject = new TargetPacmanBehaviour(pacman, Colour.RED, Collections.emptySet());

        Location target = testObject.targetLocation(null);

        assertThat(target, is(pacmanLocation));
    }
}
