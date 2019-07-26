package pacman.core.elements;

import pacman.core.elements.ghostbehaviour.*;
import org.junit.Test;
import pacman.core.Colour;
import pacman.core.Location;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TargetInFrontOfPacmanFollowingRedGhostBehaviourTest {

    @Test
    public void targetsTwoSpacesInFrontOfPacmanPlusDistanceToRedGhost() {
        Pacman pacman = new Pacman(new Location(10, 10), ">", null, null);
        Ghost redGhost = new Ghost(new Location(7, 11), null, new FixedLocationGhostBehaviour(null, Colour.RED, null), null, null);
        TargetInFrontOfPacmanFollowingRedGhostBehaviour testObject = new TargetInFrontOfPacmanFollowingRedGhostBehaviour(pacman, Colour.BLUE, Collections.singleton(redGhost));

        Location target = testObject.targetLocation(null);

        assertThat(target, is(new Location(9, 9)));
    }

    @Test
    public void targetsOrthogonalSitesToRedGhost() {
        Pacman pacman = new Pacman(new Location(10, 10), ">", null, null);
        Ghost redGhost = new Ghost(new Location(8, 8), null, new FixedLocationGhostBehaviour(null, Colour.RED, null), null, null);
        TargetInFrontOfPacmanFollowingRedGhostBehaviour testObject = new TargetInFrontOfPacmanFollowingRedGhostBehaviour(pacman, Colour.BLUE, Collections.singleton(redGhost));

        Location target = testObject.targetLocation(null);

        assertThat(target, is(new Location(8, 12)));
    }

    @Test
    public void targetsParallelSitesToRedGhost() {
        Pacman pacman = new Pacman(new Location(10, 10), ">", null, null);
        Ghost redGhost = new Ghost(new Location(6, 10), null, new FixedLocationGhostBehaviour(null, Colour.RED, null), null, null);
        TargetInFrontOfPacmanFollowingRedGhostBehaviour testObject = new TargetInFrontOfPacmanFollowingRedGhostBehaviour(pacman, Colour.BLUE, Collections.singleton(redGhost));

        Location target = testObject.targetLocation(null);

        assertThat(target, is(new Location(10, 10)));
    }
}
