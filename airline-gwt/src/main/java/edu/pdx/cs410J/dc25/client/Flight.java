package edu.pdx.cs410J.dc25.client;

import edu.pdx.cs410J.AbstractFlight;

import java.util.Date;

/**
 * The <code>Flight</code> class extends the <code>AbstractFlight</code> class and implements the abstract methods
 * found there.  The Comparable interface has also been implemented to allow for the comparison of flights.
 * This class has unique flight data for each object instantiated that is tied to an airline in the
 * {@link Airline} class.
 */
public class Flight extends AbstractFlight implements Comparable<Flight> {
  private int flightNumber;
  private String source;
  private String destination;
  private Date depart;
  private Date arrival;

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Flight() {

  }

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
  public Flight(int flightNumberIn, String sourceIn, Date departIn, String destinationIn, Date arrivalIn) {
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
   * @return Date and time of departure
   */
  @Override
  public String getDepartureString() {
    return "DEPART" + getDeparture();
  }

  /**
   * @return Code of arrival airport
   */
  @Override
  public String getDestination() {
    return this.destination;
  }

  /**
   * @return Date and time of arrival
   */
  @Override
  public String getArrivalString() {
    return "ARRIVE" + getArrival();
  }

  /**
   * @return Date and time of departure as a date
   */
  @Override
  public Date getDeparture() {
    return this.depart;
  }

  /**
   * @return Date and time of arrival as a date
   */
  @Override
  public Date getArrival() {
    return this.arrival;
  }

  /**
   * Compares the <code>Flight</code> to another <code>Flight</code>.  Checks for equality and facilitates sorting.
   *
   * @param toCompare
   *        <code>Flight</code> to compare with this flight
   * @return
   *        Returns -1, 0, or 1 if this flight is less than, equal to, or greater than
   *        the flight to compare, respectively
   */
  public int compareTo(Flight toCompare) {
    if (this.source.compareTo(toCompare.source) < 0) {
      return -1;
    } else if (this.source.compareTo(toCompare.source) > 0) {
      return 1;
    } else {
      if (this.getDeparture().compareTo(toCompare.getDeparture()) < 0) {
        return -1;
      } else if (this.getDeparture().compareTo(toCompare.getDeparture()) > 0) {
        return 1;
      } else {
        return 0;
      }
    }
  }

}
