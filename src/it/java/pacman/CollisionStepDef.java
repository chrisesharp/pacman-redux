package pacman;

import static org.junit.Assert.*;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import cucumber.api.DataTable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import pacman.utils.MapParser;
import pacman.utils.PacmanMapFactory;
import pacman.core.*;
import pacman.core.elements.*;
import pacman.core.elements.ghostbehaviour.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class CollisionStepDef {
    private int initialScore = 0;
    private int initialLives = 3;
    private final int cols = 10;
    private PacmanMap map;
    private Pacman pacman;
    private File rawMapFile;
    private PacmanStatus status;
    private Collection<Ghost> ghosts;
    private PacmanEngine<PacmanStatus,PacmanMap> game;

    @Before
    public void initialStatusIsThreeLives() {
        status = new PacmanStatus(initialLives, initialScore, cols);
        map = new PacmanMap();
        ghosts = new HashSet<>();       
    }

    // Given steps

    @Given("^a pacman facing \"([^\"]*)\" at (\\d+),(\\d+) with starting position (\\d+),(\\d+)$")
    public void a_pacmanFacingRightAtWithStartingPosition(String direction, int x1, int y1, int x0, int y0) {
        String icon;
        switch(direction) {
            case "left":
                icon = ">";
                break;
            case "right":
                icon = "<";
                break;
            case "up":
                icon = "V";
                break;
            case "down":
                icon = "Λ";
                break;
            default:
                icon = "*";
                break;
        }
        Location origin = new Location(x0,y0);
        Location location = new Location(x1,y1);
        pacman = new Pacman(origin, icon, map, status);
        pacman.setLocation(location);
        map.addElement(pacman);
    }

    @Given("^a pill at (\\d+),(\\d+)$")
    public void aPillAt(int x, int y) {
        Location location = new Location(x,y);
        Pill pill = new Pill(location, map, status);
        map.addElement(pill);
    }

    @Given("^a power pill at (\\d+),(\\d+)$")
    public void aPowerPillAt(int x, int y) {
        Location location = new Location(x,y);
        PowerPill pill = new PowerPill(location, map, status, ghosts );
        map.addElement(pill);
    }

    @Given("^a ghost at (\\d+),(\\d+) with starting position (\\d+),(\\d+)$")
    public void aGhostAt(int x1, int y1, int x0, int y0) {
        Location origin = new Location(x0, y0);
        Location location = new Location(x1, y1);
        Ghost ghost = new Ghost(origin, map, new TargetPacmanBehaviour(pacman, Colour.RED, ghosts), new TargetPacmanBehaviour(pacman, Colour.RED, ghosts), new GhostPanicBehaviour());
        ghost.setLocation(location);
        ghosts.add(ghost);
        map.addElement(ghost);
    }

    @Given("^a ghost at (\\d+),(\\d+)$")
    public void aGhostAt(int x, int y) {
        Location location = new Location(x,y);
        Ghost ghost = new Ghost(location, map, new TargetPacmanBehaviour(pacman, Colour.RED, ghosts), new TargetPacmanBehaviour(pacman, Colour.RED, ghosts), new GhostPanicBehaviour());
        ghosts.add(ghost);
        map.addElement(ghost);
    }

    @Given("^walls at the following places:$")
    public void wallsAtTheFollowingPlaces(DataTable data) {
      List<List<String>> wallList = data.cells(1);
        for (List<String> wallspec: wallList ) {
          String icon = wallspec.get(0);
          int x = Integer.parseInt(wallspec.get(1));
          int y = Integer.parseInt(wallspec.get(2));
          Wall wall = new Wall(new Location(x,y), icon);
          map.addElement(wall); 
        }
    }

    @Given("^a starting map:$")
    public void aStartingMap(String rawMap) throws IOException {
        rawMapFile = File.createTempFile("pacman-test-map", ".txt");
        rawMapFile.deleteOnExit();
        BufferedWriter writer = new BufferedWriter(new FileWriter(rawMapFile.getPath()));
        writer.write(rawMap);

        writer.close();
    }

    // When steps

    @When("^we parse the map$")
    public void weParseTheMap() {
        PacmanMapFactory factory = new PacmanMapFactory(MapParser.parseFile(rawMapFile.getPath()), status, new MockTerminal());
        map = factory.createMap();
    }

    @When("^we advance the game by (\\d+) tick$")
    public void weAdvanceTheGameByTick(int ticks) {
        for (int i=0; i < ticks; i++) {
            map.tick();
        }
    }

    @When("^we play the game for (\\d+) ticks$")
    public void wePlayTheGameForTicks(int ticks) {
        game = new PacmanEngine<>(status, map, new MockTerminal(), null);
        game.FRAMEDELAY = 0;
        game.play(ticks);
    }
    
    // Then steps
    
    @Then("^there should be a ghost at (\\d+),(\\d+)$")
    public void thereShouldBeAGhostAt(int x, int y) {
        Collection<GameElement> elements = map.getElements(new Location(x,y));
        for (GameElement element: elements) {
            assertTrue(element instanceof Ghost);
        }
    }

    @Then("^there should be a pill at (\\d+),(\\d+)$")
    public void thereShouldBeAPillAt(int x, int y) {
        Collection<GameElement> elements = map.getElements(new Location(x,y));
        for (GameElement element: elements) {
            assertTrue(element instanceof Pill);
        }
    }

    @Then("^there should not be a pill at (\\d+),(\\d+)$")
    public void thereShouldNotBeAPillAt(int x, int y) {
        Collection<GameElement> elements = map.getElements(new Location(x,y));
        for (GameElement element: elements) {
            assertFalse(element instanceof Pill);
        }
    }

    @Then("^there should not be a pacman at (\\d+),(\\d+)$")
    public void thereShouldNotBeAPacmanAt(int x, int y) {
        Collection<GameElement> elements = map.getElements(new Location(x,y));
        for (GameElement element: elements) {
            assertFalse(element instanceof Pacman);
        }
    }

    @Then("^the score should increase by (\\d+) points$")
    public void theScoreShouldIncreaseByPoints(int score) {
        assertThat(status.score(), is(initialScore + score));
    }

    @Then("^the lives should decrease by (\\d+)$")
    public void theLivesShouldDecreaseBy(int lives) {
        assertThat(status.lives(), is(initialLives - lives));
    }

    @Then("^there should be a calm ghost at (\\d+),(\\d+)$")
    public void thereShouldBeACalmGhostAt(int x, int y) {
        Location loc = new Location(x,y);
        Collection<GameElement> elements = map.getElements(loc);
        for (GameElement element: elements) {
            if (element instanceof Ghost) {
                Ghost ghost = (Ghost) element;
                assertEquals("M", ghost.getIcon());
            }
        }
    }

    @Then("^there should be a panicked ghost at (\\d+),(\\d+)$")
    public void thereShouldBeAPanickedGhostAt(int x, int y) {
        Collection<GameElement> elements = map.getElements(new Location(x,y));
        for (GameElement element: elements) {
            if (element instanceof Ghost) {
                Ghost ghost = (Ghost) element;
                assertEquals("W", ghost.getIcon());
            }
        }
    }

    @Then("^the level should be over$")
    public void theLevelShouldBeOver() {
        assertTrue(status.levelOver());
    }
}