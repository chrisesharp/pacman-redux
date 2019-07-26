package pacman;

import org.junit.Test;
import pacman.utils.MapElements;
import pacman.utils.MapParser;
import static pacman.utils.MapParser.filter;
import pacman.core.elements.*;
import pacman.core.Location;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class PacmanMapParserTest {
    @Test
    public void mapParserGivesLocationsOfAllGameElementTypes() throws IOException {
        String inputMapWithAllGameElements = "=WM<>ΛV.o#+-|";
        File mapFile = File.createTempFile("pacman-test-map", ".txt");
        mapFile.deleteOnExit();
        BufferedWriter writer = new BufferedWriter(new FileWriter(mapFile.getPath()));
        writer.write(inputMapWithAllGameElements);
        writer.close();

        MapElements mapElements = MapParser.parse(mapFile.getPath());

        assertThat(filter(mapElements.elements, Gate::isGate), hasSize(1));
        assertThat(filter(mapElements.elements, Gate::isGate), containsInAnyOrder(new MapElements.MapElement(new Location(0,0), "=")));
        assertThat(filter(mapElements.elements, Ghost::isGhost), hasSize(2));
        assertThat(filter(mapElements.elements, Ghost::isGhost), containsInAnyOrder(new MapElements.MapElement(new Location(1,0), "W"),
                new MapElements.MapElement(new Location(2,0), "M")));
        assertThat(filter(mapElements.elements, Pacman::isPacman), hasSize(4));
        assertThat(filter(mapElements.elements, Pacman::isPacman), containsInAnyOrder(new MapElements.MapElement(new Location(3,0), "<"),
                new MapElements.MapElement(new Location(4,0), ">"),
                new MapElements.MapElement(new Location(5,0), "Λ"),
                new MapElements.MapElement(new Location(6,0), "V")));
        assertThat(filter(mapElements.elements, Pill::isPill), hasSize(1));
        assertThat(filter(mapElements.elements, Pill::isPill), containsInAnyOrder(new MapElements.MapElement(new Location(7,0), ".")));
        assertThat(filter(mapElements.elements, PowerPill::isPowerPill), hasSize(1));
        assertThat(filter(mapElements.elements, PowerPill::isPowerPill), containsInAnyOrder(new MapElements.MapElement(new Location(8,0), "o")));
        assertThat(filter(mapElements.elements, Tunnel::isTunnel), hasSize(1));
        assertThat(filter(mapElements.elements, Tunnel::isTunnel), containsInAnyOrder(new MapElements.MapElement(new Location(9,0), "#")));
        assertThat(filter(mapElements.elements, Wall::isWall), hasSize(3));
        assertThat(filter(mapElements.elements, Wall::isWall), containsInAnyOrder(new MapElements.MapElement(new Location(10,0), "+"),
                new MapElements.MapElement(new Location(11,0), "-"),
                new MapElements.MapElement(new Location(12,0), "|")));
        assertThat(mapElements.height, is(1));
        assertThat(mapElements.width, is(13));
    }
}
