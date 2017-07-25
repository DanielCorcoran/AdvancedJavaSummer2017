package edu.pdx.cs410J.dc25;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Flight} class.
 */
public class FlightTest {
  
  @Test
  public void getArrivalStringReturnsTheArrivalTimePassedInToTheConstructor() {
    String arrival = "1/1/17 12:00 AM";
    Flight flight = new Flight(0, null, null, null, arrival);
    assertThat(flight.getArrivalString(), equalTo(arrival));
  }

  @Test
  public void getDepartureStringReturnsTheDepartureTimePassedInToTheConstructor() {
    String depart = "1/1/17 12:00 AM";
    Flight flight = new Flight(0, null, depart, null, null);
    assertThat(flight.getDepartureString(), equalTo(depart));
  }

  @Test
  public void flightNumberIsEqualToFlightNumberPassedInToConstructor() {
    int flightNumber = 2112;
    Flight flight = new Flight(flightNumber, null, null, null, null);
    assertThat(flight.getNumber(), equalTo(flightNumber));
  }

  @Test
  public void getSourceReturnsTheAirportThatThePlaneIsDepartingFrom() {
    String source = "Source Airport";
    Flight flight = new Flight(0, source, null, null, null);
    assertThat(flight.getSource(), equalTo(source));
  }

  @Test
  public void getDestinationReturnsTheAirportThatThePlaneIsArrivingAt() {
    String destination = "Destination Airport";
    Flight flight = new Flight(0, null, null, destination, null);
    assertThat(flight.getDestination(), equalTo(destination));
  }

  @Test
  public void getDateStringReturnsShortDateAndTime() {
    Flight flight = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    assertThat(flight.getDepartureString(), is("11/11/17 12:00 AM"));
  }

  @Test
  public void getDepartureParsesSingleDigitMonthsCorrectly() {
    Flight flight = new Flight(2112, "PDX", "1/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    assertThat(flight.getDepartureString(), is("1/11/17 12:00 AM"));
  }

  @Test
  public void getDepartureParsesSingleDigitDaysCorrectly() {
    Flight flight = new Flight(2112, "PDX", "11/1/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    assertThat(flight.getDepartureString(), is("11/1/17 12:00 AM"));
  }

  @Test
  public void getDepartureParsesSingleDigitDaysAndMonthsTogether() {
    Flight flight = new Flight(2112, "PDX", "1/1/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    assertThat(flight.getDepartureString(), is("1/1/17 12:00 AM"));
  }

  @Test
  public void getArrivalParsesSingleDigitMonthsCorrectly() {
    Flight flight = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "1/11/2017 12:01 am");
    assertThat(flight.getArrivalString(), is("1/11/17 12:01 AM"));
  }

  @Test
  public void getArrivalParsesSingleDigitDaysCorrectly() {
    Flight flight = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/1/2017 12:01 am");
    assertThat(flight.getArrivalString(), is("11/1/17 12:01 AM"));
  }

  @Test
  public void getArrivalParsesSingleDigitDaysAndMonthsTogether() {
    Flight flight = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "1/1/2017 12:01 am");
    assertThat(flight.getArrivalString(), is("1/1/17 12:01 AM"));
  }

  @Test
  public void whenFlightOneDepartAirportIsLessThanFlight2Negative1IsReturned() {
    Flight flight1 = new Flight(2112, "DEN", "11/11/2017 12:00 am", "PDX",
            "11/11/2017 12:01 am");
    Flight flight2 = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    assertThat(flight1.compareTo(flight2), is(-1));
  }

  @Test
  public void whenFlightOneDepartAirportIsGreaterThanFlight2Positive1IsReturned() {
    Flight flight1 = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    Flight flight2 = new Flight(2112, "DEN", "11/11/2017 12:00 am", "PDX",
            "11/11/2017 12:01 am");
    assertThat(flight1.compareTo(flight2), is(1));
  }

  @Test
  public void whenFlightOneDepartTimeIsLessThanFlight2Negative1IsReturned() {
    Flight flight1 = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    Flight flight2 = new Flight(2112, "PDX", "11/11/2017 12:01 am", "DEN",
            "11/11/2017 12:02 am");
    assertThat(flight1.compareTo(flight2), is(-1));
  }

  @Test
  public void whenFlightOneDepartTimeIsGreaterThanFlight2Positive1IsReturned() {
    Flight flight1 = new Flight(2112, "PDX", "11/11/2017 12:01 am", "DEN",
            "11/11/2017 12:02 am");
    Flight flight2 = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    assertThat(flight1.compareTo(flight2), is(1));
  }

  @Test
  public void whenFlightsAreEqual0IsReturned() {
    Flight flight1 = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    Flight flight2 = new Flight(2112, "PDX", "11/11/2017 12:00 am", "DEN",
            "11/11/2017 12:01 am");
    assertThat(flight1.compareTo(flight2), is(0));
  }
}
