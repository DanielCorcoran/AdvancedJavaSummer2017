package edu.pdx.cs410J.dc25;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Integration test that tests the REST calls made by {@link AirlineRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AirlineRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AirlineRestClient newAirlineRestClient() {
    int port = Integer.parseInt(PORT);
    return new AirlineRestClient(HOSTNAME, port);
  }

  @Test
  public void test0RemoveAirline() throws IOException {
    AirlineRestClient client = newAirlineRestClient();
    client.removeAirline();
  }

  @Test
  public void test1AddOneFlight() throws IOException {
    AirlineRestClient client = newAirlineRestClient();

    String airlineName = "My Airline";
    Flight flight = new Flight(123,"PDX", "1/1/2017 12:00 AM", "LAX", "1/1/2017 12:30 AM");
    client.addFlightToServer(airlineName, flight);
  }

  @Test
  public void test2AddMoreFlights() throws IOException {
    AirlineRestClient client = newAirlineRestClient();

    String airlineName = "My Airline";
    client.addFlightToServer(airlineName, new Flight(234, "PDX", "1/1/2017 12:01 AM", "LAS", "1/1/2017 12:30 AM"));
    client.addFlightToServer(airlineName, new Flight(345, "PDX", "1/1/2017 12:02 AM", "LAX", "1/1/2017 12:30 AM"));
  }

  @Test
  public void test3PrettyPrintFlightsFromPDXToLAX() throws IOException {
    AirlineRestClient client = newAirlineRestClient();

    String airlineName = "My Airline";
    String pretty = client.searchForFlights(airlineName, "PDX", "LAX");

    assertThat(pretty, containsString("123"));
    assertThat(pretty, containsString("345"));
    assertThat(pretty, not(containsString("LAS")));

  }

}