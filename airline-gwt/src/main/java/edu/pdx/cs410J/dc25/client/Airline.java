package edu.pdx.cs410J.dc25.client;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The <code>Airline</code> class has a unique airline name and stores all of the flights for that airline.
 * It extends the <code>AbstractAirline</code> class and implements the abstract methods found there.
 * Individual flights are created in the {@link Flight} class.
 */
public class Airline extends AbstractAirline<Flight> {
  private Collection<Flight> flights = new ArrayList<>();
  private String airlineName;

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Airline() {

  }

  /**
   * Creates a new <code>Airline</code>
   *
   * @param nameIn
   *        The name of the airline
   */
  public Airline(String nameIn) {
    this.airlineName = nameIn;
  }

  /**
   * @return Name of airline
   */
  @Override
  public String getName() {
    return this.airlineName;
  }

  /**
   * Adds a flight to the list of <code>flights</code> if it is unique
   *
   * @param flight
   *        flight to add to the airline
   */
  @Override
  public void addFlight(Flight flight) {
    boolean toAdd = true;
    Flight[] flightArray = flights.toArray(new Flight[flights.size()]);

    for (int i = 0; i < flights.size(); ++i) {
      if (flight.compareTo(flightArray[i]) == 0) {
        toAdd = false;
      }
    }

    if (toAdd) {
      this.flights.add(flight);
    }
  }

  /**
   * @return List of flights.
   */
  @Override
  public ArrayList<Flight> getFlights() {
    if (flights.size() == 0) {
      return null;
    } else {
      return new ArrayList<>(this.flights);
    }
  }

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  /*
  public Airline() {

  }

  private Collection<Flight> flights = new ArrayList<>();

  @Override
  public String getName() {
    return "Air CS410J";
  }

  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  @Override
  public Collection<Flight> getFlights() {
    return this.flights;
  }
  */
}
