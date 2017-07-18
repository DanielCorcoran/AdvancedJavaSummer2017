package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractFlight;

/**
 * The <code>Flight</code> class extends the <code>AbstractFlight</code> class and implements the abstract methods
 * found there.
 * This class has unique flight data for each object instantiated that is tied to an airline in the
 * {@link Airline} class.
 */
public class Flight extends AbstractFlight {
  private int flightNumber;
  private String source;
  private String destination;
  private String depart;
  private String arrival;

  /**
   * Creates a new <code>Flight</code>
   *
   * @param flightNumberIn
   *        Number of the flight
   * @param sourceIn
   *        3 letter code for the airport the flight departs from
   * @param departIn
   *        Time of departure
   * @param destinationIn
   *        3 letter code for the airport at which the flight lands
   * @param arrivalIn
   *        Time of arrival
   */
  Flight(int flightNumberIn, String sourceIn, String departIn, String destinationIn, String arrivalIn) {
    this.flightNumber = flightNumberIn;
    this.source = sourceIn;
    this.depart = departIn;
    this.destination = destinationIn;
    this.arrival = arrivalIn;
  }

  /**
   * @return Flight number
   */
  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  /**
   * @return Departing airport code
   */
  @Override
  public String getSource() {
    return this.source;
  }

  /**
   * @return Time of departure
   */
  @Override
  public String getDepartureString() {
    return this.depart;
  }

  /**
   * @return Code of arrival airport
   */
  @Override
  public String getDestination() {
    return this.destination;
  }

  /**
   * @return Time of arrival
   */
  @Override
  public String getArrivalString() {
    return this.arrival;
  }
}
