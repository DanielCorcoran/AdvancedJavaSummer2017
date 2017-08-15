package edu.pdx.cs410J.dc25.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.dc25.client.Airline;
import edu.pdx.cs410J.dc25.client.Flight;
import edu.pdx.cs410J.dc25.client.AirlineService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The server-side implementation of the Airline service
 */
public class AirlineServiceImpl extends RemoteServiceServlet implements AirlineService
{
  private Airline airline = null;

  /**
   * Getter for the <code>Airline</code> on the server
   * @return
   *        Returns the <code>Airline</code> from the server
   * @throws Exception
   *        Throws exception if the server doesn't have an airline
   */
  @Override
  public Airline getAirline() throws Exception {
    if (this.airline == null) {
      throw new Exception("There is no airline on the server");
    } else {
      return this.airline;
    }
  }

  /**
   * Adds an <code>Airline</code> to the server
   * @param airlineName
   *        Name of the <code>Airline</code> to create
   * @throws Exception
   *        Throws exception if there is an airline already on the server
   */
  @Override
  public void addAirlineToServer(String airlineName) throws Exception {
    if (airlineName.length() == 0) {
      throw new Exception("Airline must have a name");
    } else if (this.airline == null) {
      this.airline = new Airline(airlineName);
    } else {
      throw new Exception("Could not add airline to server.  Make sure the server does not already have an airline.");
    }
  }

  /**
   * Creates a <code>Flight</code> from the parameters and adds it to the server if
   * an identical one doesn't already exist
   * @param flightNumber
   *        Number of the flight to be added
   * @param source
   *        Source airport code of the flight to be added
   * @param departDateTime
   *        Date and time of departure of flight to be added
   * @param dest
   *        Destination airport code of the flight to be added
   * @param arriveDateTime
   *        Date and time of arrival of flight to be added
   * @throws Exception
   *        Throws exception if the server doesn't have an airline
   */
  @Override
  public void addFlightToServer(int flightNumber, String source, String departDateTime,
                                String dest, String arriveDateTime) throws Exception {
    if (this.airline == null) {
      throw new Exception("There is no airline on the server");
    } else {
      Date departDateTimeAsDate = parseDateTimeString(departDateTime);
      Date arriveDateTimeAsDate = parseDateTimeString(arriveDateTime);
      Flight flight = new Flight(flightNumber, source, departDateTimeAsDate, dest, arriveDateTimeAsDate);
      airline.addFlight(flight);
    }
  }

  /**
   * Parses the date and time string that is passed in with a <code>Flight</code> to be added
   * @param toParse
   *        Date and time string to parse into a date
   * @return
   *        Returns date time string as a date
   */
  private Date parseDateTimeString(String toParse) {
    SimpleDateFormat parseFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    Date dateTimeAsDate = null;
    try {
      dateTimeAsDate = parseFormat.parse(toParse);
    }catch (ParseException e) {
      System.out.println("Unparseable using " + parseFormat);
    }

    return dateTimeAsDate;
  }

  /**
   * Creates a new airline and stores flights that meet the search criteria in that airline
   * @param source Airport the flight is departing from
   * @param destination Airport the flight is arriving at
   * @return Returns the {@link Airline} with all flights that match the search criteria
   */
  public Airline searchServerForFlights(String source, String destination) throws Exception {
    if (this.airline == null) {
      throw new Exception("There is no airline on the server");
    } else {
      Airline searchResult = new Airline(this.airline.getName());
      Flight[] storedFlights = this.airline.getFlights().toArray(new Flight[this.airline.getFlights().size()]);

      for (Flight storedFlight : storedFlights) {
        if (storedFlight.getSource().equals(source) && storedFlight.getDestination().equals(destination)) {
          searchResult.addFlight(storedFlight);
        }
      }

      if (searchResult.getFlights() == null) {
        throw new Exception("No flights match the search criteria");
      } else {
        return searchResult;
      }
    }
  }

  /**
   * Log unhandled exceptions to standard error
   *
   * @param unhandled
   *        The exception that wasn't handled
   */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }
}
