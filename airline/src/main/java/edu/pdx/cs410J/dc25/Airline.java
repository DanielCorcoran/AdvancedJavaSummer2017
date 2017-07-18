package edu.pdx.cs410J.dc25;

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
   * Adds a flight to the list of <code>flights</code>
   *
   * @param flight
   *        list of flights for the airline
   */
  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  /**
   * @return List of flights.  Currently, the list size can only have a maximum of one flight,
   * so the method returns either null (no flights) or the singular flight stored in the <code>flights</code> variable.
   */
  @Override
  public Collection<Flight> getFlights() {
    if (flights.size() == 0) {
      return null;
    } else {
      return this.flights;
    }
  }
}
