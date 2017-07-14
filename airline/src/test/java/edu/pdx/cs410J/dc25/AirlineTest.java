package edu.pdx.cs410J.dc25;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Airline} class.
 */
public class AirlineTest {

  private Airline getAirlineName(String name) {
    return new Airline(name);
  }

  @Test
  public void nameOfAirlineIsSameAsNameGivenToAirline() {
    String airlineName = "Baller Air";
    Airline airline = getAirlineName(airlineName);
    assertThat(airline.getName(), equalTo(airlineName));
  }

  @Test
  public void flightsAreNullWhenThereAreNoFlightsGiven() {
    Airline airline = getAirlineName("Name");
    assertThat(airline.getFlights(), equalTo(null));
  }

  @Test
  public void oneFlightInTheAirlineClassWhenOneFlightIsGiven() {
    Flight flight1 = new Flight(0, null, null, null, null);
    Airline airline = getAirlineName("Name");
    airline.addFlight(flight1);
    Collection<Flight> testCollection = new ArrayList<>();
    testCollection.add(flight1);
    assertThat(airline.getFlights(), is(testCollection));
  }
}