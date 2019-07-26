package pacman.utils;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ArgParser {
  private static final Logger log = Logger.getLogger(ArgParser.class);
  @Option(name="-f", aliases="--file", usage="Fully qualified path and name of level map txt file.")  
  private String fileName;   
  
  @Option(name="-t", aliases="--ticks", usage="Number of ticks to run the game")
  private int ticks = -1;

  public ArgParser(String... args) {
      CmdLineParser parser = new CmdLineParser(this);
      try {
          parser.parseArgument(args);
      } catch (CmdLineException e) {
          log.error(e.getMessage());
      }
  }
  
  public String getMapFile() {
    return this.fileName;
  }
  
  public boolean hasMapFile() {
    return (this.fileName != null);
  }
  
  public int getTicks() {
    return this.ticks;
  }
}
