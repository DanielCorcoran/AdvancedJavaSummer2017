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
    String arrival = "Arrival Time";
    Flight flight = new Flight(0, null, null, null, arrival);
    assertThat(flight.getArrivalString(), equalTo(arrival));
  }

  @Test
  public void getDepartureStringReturnsTheDepartureTimePassedInToTheConstructor() {
    String depart = "Departure Time";
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
  public void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
    Flight flight = new Flight(0, null, null, null, null);
    assertThat(flight.getDeparture(), is(nullValue()));
  }
}
