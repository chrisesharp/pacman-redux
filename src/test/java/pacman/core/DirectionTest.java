package pacman.core;

import org.junit.Test;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pacman.core.Direction.*;
import java.util.function.Function;


public class DirectionTest {
    
    @Test
    public void leftOfLeftIsDown() {
        Direction direction = LEFT;
        assertThat(direction.turnLeft(), is(DOWN));
    }

    @Test
    public void rightOfLeftIsUp() {
        Direction direction = LEFT;
        assertThat(direction.turnRight(), is(UP));
    }

    @Test
    public void oppositeOfUpIsDown() {
        Direction direction = UP;
        assertThat(direction.turnBack(), is(DOWN));
    }

    @Test
    public void leftOf_1_1_is_0_1() {
        Location location = new Location(1,1);
        Direction direction = LEFT;
        Function<Location, Location> transform = direction.movement();
        Location expected = new Location(0,1);
        assertThat((transform.apply(location)), is(expected));
    }

    @Test
    public void applyingAllMovementReturnsToOrigin() {
        Location start = new Location(0,0);
        Location expected = new Location(0,0);
        Location step1 = LEFT.movement().apply(start);
        Location step2 = RIGHT.movement().apply(step1);
        Location step3 = UP.movement().apply(step2);
        Location end = DOWN.movement().apply(step3);
        assertThat(end, is(expected));
    }
}