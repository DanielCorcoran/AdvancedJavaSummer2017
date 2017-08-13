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

  @Override
  public Airline getAirline() throws Exception {
    if (this.airline == null) {
      throw new Exception("There is no airline on the server");
    } else if (this.airline.getFlights() == null) {
      throw new Exception("Airline " + this.airline.getName() + " does not have any flights");
    } else {
      return this.airline;
    }
  }

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
