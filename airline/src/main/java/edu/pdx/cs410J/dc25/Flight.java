package edu.pdx.cs410J.dc25;

import edu.pdx.cs410J.AbstractFlight;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
   * @return Date and time of departure
   */
  @Override
  public String getDepartureString() {
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    return df.format(getDeparture());
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
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    return df.format(getArrival());
  }

  /**
   * @return Date and time of departure as a date
   */
  @Override
  public Date getDeparture() {
    SimpleDateFormat parseFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    Date departAsDate = null;
    try {
      departAsDate = parseFormat.parse(depart);
    }catch (ParseException e) {
      System.out.println("Unparseable using " + parseFormat);
    }

    return departAsDate;
  }

  /**
   * @return Date and time of arrival as a date
   */
  @Override
  public Date getArrival() {
    SimpleDateFormat parseFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    Date arriveAsDate = null;
    try {
      arriveAsDate = parseFormat.parse(arrival);
    }catch (ParseException e) {
      System.out.println("Unparseable using " + parseFormat);
    }

    return arriveAsDate;
  }
}
