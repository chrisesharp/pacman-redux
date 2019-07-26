package pacman;

import static org.junit.Assert.*;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
// import cucumber.api.DataTable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;


public class IntegrationStepDef {
    private PacmanGame game;
    private ByteArrayInputStream in;
    private ByteArrayOutputStream gameOutput;
    private int gameWidth = 6;
    private int gameHeight = 9;

    @Before
    public void initialized() {
        in = new ByteArrayInputStream(new byte[] {});
        gameOutput = new ByteArrayOutputStream();    
    }

    // Given steps

    @Given("^a pacman game using the file \"([^\"]*)\"$")
    public void a_pacman_game_using_the_file(String filename) {
        URL fileToRead = IntegrationStepDef.class.getResource("/data/" + filename);
        game = new PacmanGame(fileToRead.getPath(), in, gameOutput);

    }

    // When steps

    @When("^we run the game for (\\d+) ticks$")
    public void we_run_the_game_for_ticks(int ticks) {
        game.run(ticks);
    }
    
    // Then steps

    @Then("^the output should be:$")
    public void the_output_should_be(String expected) {
        assertThat(strippedGameOutput(), is(expected));
    }

    private String strippedGameOutput() {
        String output = game.toString();
        StringBuilder result = new StringBuilder();
        int lineNum = 0;
        for (String line: output.split("\n")) {
            if (lineNum > 0 && lineNum < gameHeight) {
                result.append(line.substring(0, gameWidth));
                result.append("\n");
            }
            lineNum++;
        }
        return result.toString();
    }
}