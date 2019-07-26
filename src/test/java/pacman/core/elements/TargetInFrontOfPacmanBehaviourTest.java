package pacman.core.elements;

import pacman.core.elements.ghostbehaviour.*;

import org.junit.Test;
import pacman.core.Colour;
import pacman.core.Location;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TargetInFrontOfPacmanBehaviourTest {
    @Test
    public void targetLocationIsPacmansLocation() {
        Location pacmanLocation = new Location(5, 1);
        Pacman pacman = new Pacman(pacmanLocation, ">", null, null);
        TargetInFrontOfPacmanBehaviour testObject = new TargetInFrontOfPacmanBehaviour(pacman, Colour.RED, Collections.emptySet());

        Location target = testObject.targetLocation(null);

        assertThat(target, is(new Location(1, 1)));
    }
}
