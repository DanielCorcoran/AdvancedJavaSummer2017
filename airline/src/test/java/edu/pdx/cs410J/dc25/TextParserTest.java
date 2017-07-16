package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.ParserException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link TextParser} class.
 */
public class TextParserTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void wrongFileGivenToConstructor() throws ParserException {
    thrown.expect(ParserException.class);
    thrown.expectMessage("Cannot find file to read from");

    String fileName = "not a valid file";
    TextParser parser = new TextParser(fileName);
    parser.parse();
  }

  @Test
  public void fileIsEmpty() throws ParserException {
    thrown.expect(ParserException.class);
    thrown.expectMessage("File not correctly formatted");

    String fileName = "test.txt";
    TextParser parser = new TextParser(fileName);
    parser.parse();
  }

  @Test
  public void notEnoughArgumentsInFile() throws ParserException {
    thrown.expect(ParserException.class);
    thrown.expectMessage("File not correctly formatted");

    String fileName = "test1.txt";
    TextParser parser = new TextParser(fileName);
    parser.parse();
  }

  @Test
  public void wrongNumberOfArgumentsInFile1Flight() throws ParserException {
    thrown.expect(ParserException.class);
    thrown.expectMessage("File not correctly formatted");

    String fileName = "test.txt";
    TextParser parser = new TextParser(fileName);
    parser.parse();
  }

  @Test
  public void flightNumberIsNotAnInteger() throws ParserException {
    thrown.expect(ParserException.class);
    thrown.expectMessage("File not correctly formatted");

    String fileName = "test.txt";
    TextParser parser = new TextParser(fileName);
    parser.parse();
  }
}
