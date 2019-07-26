package pacman.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArgParserTest {  
  @Test
  public void test_no_args() {
    ArgParser args = new ArgParser("");
    assertFalse(args.hasMapFile());
    assertEquals(-1, args.getTicks());
  }

  @Test
  public void test_file_arg() {
    ArgParser args = new ArgParser("-f=foo.txt");
    assertTrue(args.hasMapFile());
    assertEquals(-1, args.getTicks());
  }

  @Test
  public void test_tick_arg() {
    ArgParser args = new ArgParser("-t=5");
    assertFalse(args.hasMapFile());
    assertEquals(5, args.getTicks());
  }

  @Test
  public void test_tick_and_file_args() {
    ArgParser args = new ArgParser("-t=2","-f=bar.txt");
    assertEquals("bar.txt", args.getMapFile());
    assertEquals(2, args.getTicks());
  }
}