package edu.pdx.cs410J.dc25;

import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for the {@link PrettyPrinter} class.
 */
public class PrettyPrinterTest {

  @Test
  public void createAndWriteToAFileWhenNoneExistsOneFlight() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight =
            new Flight(1, "PDX", "1/1/2017 12:00 am", "DEN", "1/1/2017 12:01 am");
    airline.addFlight(flight);
    PrettyPrinter dumper = new PrettyPrinter("prettytestwrite.txt");
    dumper.dump(airline);
  }

  @Test
  public void createAndWriteToAFileWhenNoneExistsTwoFlights() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight =
            new Flight(1, "PDX", "1/1/2017 12:00 am", "DEN", "1/1/2017 12:01 am");
    airline.addFlight(flight);
    airline.addFlight(flight);
    PrettyPrinter dumper = new PrettyPrinter("prettytestwrite.txt");
    dumper.dump(airline);
  }

  @Test
  public void createAndWriteToAFileWhenOneExistsOneFlight() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight =
            new Flight(1, "PDX", "1/1/2017 12:00 am", "DEN", "1/1/2017 12:01 am");
    airline.addFlight(flight);
    PrettyPrinter dumper = new PrettyPrinter("prettytestwrite.txt");
    dumper.dump(airline);
  }

  @Test
  public void sortFlightsAndPrintInRightOrder() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight1 = new Flight(1, "PDX", "1/1/2017 12:05 am", "DEN", "1/1/2017 2:01 am");
    Flight flight2 = new Flight(2, "DEN", "1/4/2017 12:00 am", "PDX", "1/4/2017 2:01 am");
    Flight flight3 = new Flight(3, "PDX", "1/1/2017 12:00 am", "LAX", "1/1/2017 12:01 am");
    Flight flight4 = new Flight(4, "GRR", "1/5/2017 2:00 am", "LAG", "1/5/2017 5:01 am");
    Flight flight5 = new Flight(5, "LAX", "1/1/2017 12:00 am", "YYZ", "1/1/2017 4:01 am");
    airline.addFlight(flight1);
    airline.addFlight(flight2);
    airline.addFlight(flight3);
    airline.addFlight(flight4);
    airline.addFlight(flight5);
    PrettyPrinter dumper = new PrettyPrinter("prettytestwrite.txt");
    dumper.dump(airline);
  }

  @Test
  public void printsToStdOutWhenFileNameIsDash() throws IOException {
    Airline airline = new Airline("airline");
    Flight flight1 = new Flight(1, "PDX", "1/1/2017 12:05 am", "DEN", "1/1/2017 2:01 am");
    Flight flight2 = new Flight(2, "DEN", "1/4/2017 12:00 am", "PDX", "1/4/2017 2:01 am");
    Flight flight3 = new Flight(3, "PDX", "1/1/2017 12:00 am", "LAX", "1/1/2017 12:01 am");
    Flight flight4 = new Flight(4, "GRR", "1/5/2017 2:00 am", "LAG", "1/5/2017 5:01 am");
    Flight flight5 = new Flight(5, "LAX", "1/1/2017 12:00 am", "YYZ", "1/1/2017 4:01 am");
    airline.addFlight(flight1);
    airline.addFlight(flight2);
    airline.addFlight(flight3);
    airline.addFlight(flight4);
    airline.addFlight(flight5);
    PrettyPrinter dumper = new PrettyPrinter("-");
    dumper.dump(airline);
  }
}