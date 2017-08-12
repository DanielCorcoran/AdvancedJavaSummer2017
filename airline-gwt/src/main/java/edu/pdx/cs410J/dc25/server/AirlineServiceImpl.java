package edu.pdx.cs410J.dc25.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.dc25.client.Airline;
import edu.pdx.cs410J.dc25.client.Flight;
import edu.pdx.cs410J.dc25.client.AirlineService;

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
  public void throwUndeclaredException() {
    throw new IllegalStateException("Expected undeclared exception");
  }

  @Override
  public void throwDeclaredException() throws IllegalStateException {
    throw new IllegalStateException("Expected declared exception");
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
