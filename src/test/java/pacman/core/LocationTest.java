package pacman.core;

import org.junit.Test;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public class LocationTest {
    
    @Test
    public void locationsAreNotEqualToNonLocations() {
        Location loc1 = new Location(1,1);
        Set<Integer> otherThing = new HashSet<>(Arrays.asList(1, 2));
        assertThat(loc1, is(not(otherThing)));
    }

    @Test
    public void twoSameLocationsAreEqual() {
        Location loc1 = new Location(1,1);
        Location loc2 = new Location(1,1);
        assertThat(loc1, is(loc2));
    }

    @Test
    public void twoOtherDifferentLocationsAreNotEqual() {
        Location loc1 = new Location(1,1);
        Location loc2 = new Location(2,2);
        assertThat(loc1, is(not(loc2)));
    }

    @Test
    public void distanceBetweenSamePointIsZero() {
        Location loc1 = new Location(1,1);
        Location loc2 = new Location(1,1);
        assertThat(loc1.distance(loc2), is(0.0));
    }

    @Test
    public void distanceBetweenTwoAdjacentPointsIsPythagorean() {
        Location loc1 = new Location(0,0);
        Location loc2 = new Location(4,3);
        assertThat(loc1.distance(loc2), is(5.0));
    }

    @Test
    public void distanceBetweenTwoAdjacentPointsReflexive() {
        Location loc1 = new Location(1,1);
        Location loc2 = new Location(4,5);
        assertThat(loc1.distance(loc2), is(loc2.distance(loc1)));
    }

    @Test
    public void locationWorksAsHashEntry() {
        Location loc1 = new Location(1,1);
        Location loc2 = new Location(1,2);
        Map<Location,Boolean> locs = new HashMap<>();
        locs.put(loc1, true);
        locs.put(loc2, false);
        assertTrue(locs.get(loc1));
        assertFalse(locs.get(loc2));
    }

    @Test
    public void stringRepresentation() {
        Location loc = new Location(2, 3);
        String expected = "Location{x=2, y=3}";
        assertThat(""+loc, is(expected));
    }
}