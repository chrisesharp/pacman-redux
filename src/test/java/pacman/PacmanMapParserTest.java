package pacman;

import org.junit.Test;
import pacman.utils.MapElements;
import pacman.utils.MapParser;
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

        assertThat(mapElements.gates, hasSize(1));
        assertThat(mapElements.gates, containsInAnyOrder(new MapElements.MapElement(new Location(0,0), "=")));
        assertThat(mapElements.ghosts, hasSize(2));
        assertThat(mapElements.ghosts, containsInAnyOrder(new MapElements.MapElement(new Location(1,0), "W"),
                new MapElements.MapElement(new Location(2,0), "M")));
        assertThat(mapElements.pacman, hasSize(4));
        assertThat(mapElements.pacman, containsInAnyOrder(new MapElements.MapElement(new Location(3,0), "<"),
                new MapElements.MapElement(new Location(4,0), ">"),
                new MapElements.MapElement(new Location(5,0), "Λ"),
                new MapElements.MapElement(new Location(6,0), "V")));
        assertThat(mapElements.pills, hasSize(1));
        assertThat(mapElements.pills, containsInAnyOrder(new MapElements.MapElement(new Location(7,0), ".")));
        assertThat(mapElements.powerPills, hasSize(1));
        assertThat(mapElements.powerPills, containsInAnyOrder(new MapElements.MapElement(new Location(8,0), "o")));
        assertThat(mapElements.tunnels, hasSize(1));
        assertThat(mapElements.tunnels, containsInAnyOrder(new MapElements.MapElement(new Location(9,0), "#")));
        assertThat(mapElements.walls, hasSize(3));
        assertThat(mapElements.walls, containsInAnyOrder(new MapElements.MapElement(new Location(10,0), "+"),
                new MapElements.MapElement(new Location(11,0), "-"),
                new MapElements.MapElement(new Location(12,0), "|")));
        assertThat(mapElements.height, is(1));
        assertThat(mapElements.width, is(13));
    }
}
