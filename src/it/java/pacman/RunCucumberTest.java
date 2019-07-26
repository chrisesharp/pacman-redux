package pacman;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/cucumber" },
        glue = "pacman",
        tags = "not @leave",
        features = "classpath:features"
)
public class RunCucumberTest {
}
