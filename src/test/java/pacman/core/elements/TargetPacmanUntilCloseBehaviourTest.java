package pacman.core.elements;

import org.junit.Test;
import pacman.core.Colour;
import pacman.core.Location;
import pacman.core.elements.ghostbehaviour.*;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TargetPacmanUntilCloseBehaviourTest {
    @Test
    public void targetPacmanWhenMoreThanEightSquaresAway() {
        Location pacmanLocation = new Location(1, 1);
        Location fixedLocation = new Location(10, 10);
        Pacman pacman = new Pacman(pacmanLocation, "<", null, null);
        TargetPacmanUntilCloseBehaviour testObject = new TargetPacmanUntilCloseBehaviour(pacman, fixedLocation, Colour.RED, Collections.emptySet());

        Location target = testObject.targetLocation(new Location(7, 7));

        assertThat(target, is(pacmanLocation));
    }

    @Test
    public void targetFixedLocationWhenLessThanEightSquaresAway() {
        Location pacmanLocation = new Location(1, 1);
        Location fixedLocation = new Location(10, 10);
        Pacman pacman = new Pacman(pacmanLocation, "<", null, null);
        TargetPacmanUntilCloseBehaviour testObject = new TargetPacmanUntilCloseBehaviour(pacman, fixedLocation, Colour.RED, Collections.emptySet());

        Location target = testObject.targetLocation(new Location(6, 6));

        assertThat(target, is(fixedLocation));
    }
}
