package edu.pdx.cs410J.dc25;

import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for the {@link TextDumper} class.
 */
public class TextDumperTest {

  @Test
  public void createAndWriteToAFileWhenNoneExistsOneFlight() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight =
            new Flight(1, "aaa", "depart time", "bbb", "arrive time");
    airline.addFlight(flight);
    TextDumper dumper = new TextDumper("testwrite.txt");
    dumper.dump(airline);
  }

  @Test
  public void createAndWriteToAFileWhenNoneExistsTwoFlights() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight =
            new Flight(1, "aaa", "depart time", "bbb", "arrive time");
    airline.addFlight(flight);
    airline.addFlight(flight);
    TextDumper dumper = new TextDumper("testwrite.txt");
    dumper.dump(airline);
  }

  @Test
  public void createAndWriteToAFileWhenOneExistsOneFlight() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight =
            new Flight(1, "aaa", "depart time", "bbb", "arrive time");
    airline.addFlight(flight);
    TextDumper dumper = new TextDumper("testwrite.txt");
    dumper.dump(airline);
  }
}
