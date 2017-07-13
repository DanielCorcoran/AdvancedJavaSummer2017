package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;

public class Airline extends AbstractAirline<Flight> {
  private Collection<Flight> flights = new ArrayList<>();
  private String airlineName;

  public Airline(String nameIn) {
    this.airlineName = nameIn;
  }

  @Override
  public String getName() {
    return this.airlineName;
  }

  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  @Override
  public Collection<Flight> getFlights() {
    if (flights.size() == 0) {
      return null;
    } else {
      return this.flights;
    }
  }
}
