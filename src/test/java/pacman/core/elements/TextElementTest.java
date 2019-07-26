package pacman.core.elements;

import org.junit.Test;
import static org.junit.Assert.*;

import pacman.PacmanMap;
import pacman.core.Location;
import pacman.core.Colour;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TextElementTest {

    @Test
    public void testTextIsPassableByText() {
        TextElement text = new TextElement(new Location(0,0),"Hello");
        assertTrue(text.isPassable(text));
    }

    @Test
    public void testTextIsMoveable() {
        Location start = new Location(0,0);
        Location end = new Location(1,1);
        PacmanMap map = new PacmanMap();
        TextElement text = new TextElement(start,"Hello");
        map.addElement(text);
        text.setLocation(end);
        assertTrue(map.getElements(start).isEmpty());
        assertTrue(map.getElements(end).contains(text));
    }

    @Test
    public void testTextIsColoured() {
        TextElement text = new TextElement(new Location(0,0),"Hello");
        text.setColour(Colour.BLUE);
        assertThat(text.getColour(), is(Colour.BLUE));
        assertThat(text.getEffect(), is(Colour.DEFAULT));
    }

    @Test
    public void testTextIsSilent() {
        TextElement text = new TextElement(new Location(0,0),"Hello");
        assertThat(text.playSound(), is(false));
    }
}