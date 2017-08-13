package edu.pdx.cs410J.dc25.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote service that returns a dummy airline
 */
@RemoteServiceRelativePath("airline")
public interface AirlineService extends RemoteService {

  /**
   * Returns the current date and time on the server
   */
  Airline getAirline() throws Exception;

  /**
   * Adds an <code>Airline</code> to the server
   * @param airlineName
   *        Name of the <code>Airline</code> to create
   * @throws Exception
   *         If airline already exists on the server
   */
  void addAirlineToServer(String airlineName) throws Exception;

  void addFlightToServer(int flightNumber, String source, String departDateTime, String dest, String arriveDateTime)
          throws Exception;

  Airline searchServerForFlights(String source, String destination) throws Exception;
}
